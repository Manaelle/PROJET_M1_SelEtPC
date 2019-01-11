/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

import java.util.ArrayList;
import java.util.HashMap;
import prototypeasml.ASMLOperande.TypeOperande;

/**
 *
 * @author Pepefab
 */
public class ASMLFonction extends ASMLBranche implements ASMLFunDefs {

    private String nom;
    private ArrayList<ASMLOperande> parametres;
    private ArrayList<ASMLExp> expressions;
    
    private int cptPile;
    
    public ASMLFonction(String instruction){
        expressions = new ArrayList<>();
        parametres = new ArrayList<>();
        String[] donnees = instruction.split(" ");
        nom = donnees[1];
        for(int i = 2; i < donnees.length - 1; i++){
            parametres.add(new ASMLOperande(donnees[i], donnees[i].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR));
        }
    }
    
    
    public void allocationRegistre_Spill(){
        HashMap<String, String> allocateur = new HashMap<>(); // ancien nom -> nouveau nom
        int cptReg;
        int cptRegMax;
        
        // renommage des paramètres
        cptReg = 0;
        cptRegMax = 3;
        cptPile = 8;
        for(ASMLOperande p : parametres){
            if(cptReg <= cptRegMax){ // dans un registre
                allocateur.put(p.getNom(), "r" + cptReg);
                p.renommerVariable("r" + cptReg);
                cptReg += 1;
            } else { // sur la pile
                allocateur.put(p.getNom(), "[fp, #" + (cptPile) + "]");
                p.renommerVariable("[fp, #" + (cptPile) + "]");
                cptPile += 4;
            }
        }
        

        
        // récupération des opérandes (variables locales + paramètres) de la fonction
        ArrayList<ASMLOperande> ops = new ArrayList<>();
        for(ASMLExp exp : expressions){
            ops.addAll(exp.getOperandes(TypeOperande.VAR));
        }
        // récupération des noms d'opérandes uniques (variables locales + paramètres)
        ArrayList<String> nomsVariables = new ArrayList<>();
        for(ASMLOperande op : ops){ // pour chaque opérande, si c'est une variable et qu'elle n'est pas encore connue (nomsVariables), 
                                    // et que ce n'est pas un paramètre (allocateur), alors on l'ajoute à la liste
            if(! ( nomsVariables.contains(op.getNom()) || allocateur.containsKey(op.getNom()) ) ){
                nomsVariables.add(op.getNom());
            }
        }
        
        // renommage des variables
        cptReg = 4; // 4-10, puis pile
        if(nomsVariables.size() < 9){ // On garde juste le r12 pour le résultat d'une instruction
            cptRegMax = 10;
        } else { // on garde r12 pour le résultat, et r9 + r10 pour le chargement depuis la mémoire
            cptRegMax = 8;
        }
        cptPile = -4;
        for(String nomOp : nomsVariables){ // pour chaque variable unique
            if(cptReg <= cptRegMax){
                allocateur.put(nomOp, "r" + cptReg);
                cptReg += 1;
            } else {
                allocateur.put(nomOp, "[fp, #" + cptPile + "]");
                cptPile -= 4;
            }
        }   
        // on a remplis l'allocateur, on peut désormais renommer les variables (l'allocation)
        for(ASMLOperande op : ops){
            op.renommerVariable(allocateur.get(op.getNom()));
        }
    }
    
    public String genererAssembleur(){
        String code = nom + ":\n";
        if(nom == "_"){ // main
            code += "\tpush {fp, lr}\n";
            code += "\tadd fp, sp, #4\n";
        } else { // autres fonctions
            code += "\tstr fp, [sp, #-4]\n";
            code += "\tadd fp, sp, #0\n";
        }

        // sauvegarde des registres r4-r10 et r12-r13
        code += "\tpush {r4-r10,r12-r13}\n";
        
        // paramètres
        int cptParamsPile = parametres.size()-4;
        if(cptParamsPile > 0){ // trop de paramètres => sur la pile
            code += "\tsub sp, sp, #" + (4*cptParamsPile) + "\n";
        }
        
        
        // code à exécuter
        for(ASMLExp exp : this.expressions){
            code += exp.genererAssembleur();
        }
        
        // restauration des registres
        code += "\tpop {r4-r10,r12-r13}\n";
        
        // fin
        code += "\tbx lr\n";
        
        return code;
    }
    
    /*public String genererAssembleur(){
        String code = nom + ":\n";
        code += "@ sauvegarde des registres r4-r13\n";
        code += "\tpush {r4-r13}\n";
        /*for(int i = 4; i <= 13; i++){
            if(i != 11){
                code += "\tstr r" + i + ", r11, #" + cptPile + "\n";
                cptPile -= 4;               
            }
        }
        code += "\tstr r11, r11, #" + cptPile + "\n";
        
        code += "@ code de la fonction\n";
        for(ASMLExp exp : this.expressions){
            code += exp.genererAssembleur();
        }
        
        code += "@ resultat de la fonction\n";
        code += "\tmov r0, r12\n"; // résultat de la fonction
        
        code += "@ restauration des registres\n";
        code += "\tpop {r4-r13}\n";
        /*code += "\tldr r11, r11, #8\n";
        for(int i = 4; i <= 13; i++){
            if(i != 11){
                code += "\tldr r" + i + ", r11, #" + cptPile + "\n";
                cptPile += 4;
            }
        }
        return code;
    }*/

    @Override
    public void ajouterInstruction(ASMLExp expression) {
        this.expressions.add(expression);
    }
    
    
    public String toString(){
        String res = "FONCTION " + this.nom + "\n";
        res += "<paramètres> ";
        for(ASMLOperande op : parametres){
            res += op.getNom() + " ";
        }
        res += "\n<code>\n";
        for(ASMLExp e : expressions){
            res += e.toString() + "\n";
        }
        return res;
    }
    
}
