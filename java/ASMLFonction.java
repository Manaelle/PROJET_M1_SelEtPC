/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

import java.util.ArrayList;
import prototypeasml.ASMLOperande.TypeOperande;

/**
 *
 * @author Pepefab
 */
public class ASMLFonction extends ASMLBranche implements ASMLFunDefs {

    private String nom;
    private ArrayList<ASMLOperande> parametres;
    private ArrayList<ASMLExp> expressions;
    
    public ASMLFonction(String instruction){
        expressions = new ArrayList<>();
        parametres = new ArrayList<>();
        String[] donnees = instruction.split(" ");
        nom = donnees[1];
        for(int i = 2; i < donnees.length; i++){
            parametres.add(new ASMLOperande(donnees[i], donnees[i].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR));
        }
    }
    
    // Pas utile ?
    public void renommerVariable(String ancien, String nouveau){
        for(ASMLOperande op : parametres){
            op.renommerVariable(ancien, nouveau);
        }
        for(ASMLExp exp : expressions){
            exp.renommerVariable(ancien, nouveau);
        }
    }
    
    
    // TODO : MODIFIER CAS PILE
    public void allocationRegistre_Spill(){
        int cptReg;
        int cptPile;
        // renommage des paramètres
        cptReg = 0;
        cptPile = -4;
        for(ASMLOperande p : parametres){
            if(cptReg < 4){ // dans un registre
                p.renommerVariable(p.getNom(), "r" + cptReg);
                cptReg += 1;
            } else { // sur la pile
                p.renommerVariable(p.getNom(), "[fp, #" + (cptPile) + "]");
                cptPile -= 4;
            }
        }
        // récupération des opérandes de la fonction
        ArrayList<ASMLOperande> ops = new ArrayList<>();
        for(ASMLExp exp : expressions){
            ops.addAll(exp.getOperandes(TypeOperande.VAR));
        }
        // récupération des noms d'opérandes uniques
        ArrayList<String> nomsOperandes = new ArrayList<>();
        for(ASMLOperande op : ops){
            if(!(nomsOperandes.contains(op.getNom()))){
                nomsOperandes.add(op.getNom());
            }
        }
        // renommage des variables
        cptReg = 4; // 4-12 sauf 11, puis pile
        for(String nomOp : nomsOperandes){ // pour chaque variable unique
            if(cptReg == 11){
                cptReg += 1;
            }
            for(ASMLOperande op : ops){
                if(cptReg <= 12){
                    op.renommerVariable(nomOp, "r" + cptReg);
                } else {
                    op.renommerVariable(nomOp, "[fp, #" + cptPile + "]");
                    cptPile -= 4;
                }
            }
            if(cptReg <= 12){
                cptReg += 1;
            } else {
                cptPile -= 4;
            }
        }   
    }

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
