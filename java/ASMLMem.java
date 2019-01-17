/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Pepefab
 */
public class ASMLMem implements ASMLExp {
    
    // EX : mem(arr + i0) <- v0
    private ASMLOperande op1; // arr
    private ASMLOperande op2; // i0
    private ASMLOperande op3; // v0 (falcutatif)

    public ASMLMem(String instruction){
        String[] donnees = instruction.split("[ ()]");
        op1 = new ASMLOperande(donnees[1], TypeOperande.VAR);
        op2 = new ASMLOperande(donnees[3], donnees[3].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
        if(donnees.length == 7) { // il y a le <- v0
            op3 = new ASMLOperande(donnees[6], TypeOperande.VAR);
        }
    }
    

    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op1);
        a.addAll(op2.getOperandes());
        if(op3 != null){
            a.add(op3);
        }
        return a;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op1);
        a.addAll(op2.getOperandes(type));
        if(op3 != null){
            a.add(op3);
        }
        return a;
    }
    
    public String toString(){
        String res = "mem(" + op1 + " + " + op2 + ")";
        if(op3 != null){
            res += " <- " + op3;
        }
        return res;
    }

    @Override
    public String genererAssembleur() {
        String code = "";
        if(op1.getNom().startsWith("r") && op2.getNom().startsWith("r")){ // tout est dans les registres
            code += "\tadd r12, " + op1 + " " + op2 + "\n";
        } else {
            if(!op1.getNom().startsWith("r") && !op2.getNom().startsWith("r")){ // op1 et op2 doivent être chargé en registre
                if(op1.estVariable()){
                    code += "\tldr r9, " + op1 + "\n";
                } else {
                    code += "\tmov r9, " + op1 + "\n";
                }
                if(op2.estVariable()){
                    code += "\tldr r10, " + op2 + "\n";
                } else {
                    code += "\tmov r10, " + op2 + "\n";
                }
                code += "\tadd r12, r9, r10\n";
            } else if(!op1.getNom().startsWith("r")){ // op1 doit être chargé en mémoire
                if(op1.estVariable()){
                    code += "\tldr r9, " + op1 + "\n";
                } else {
                    code += "\tmov r9, " + op1 + "\n";
                }
                code += "\tadd r12, r10, " + op2 + "\n";
            } else { // op2 doit être chargé en mémoire
                if(op2.estVariable()){
                    code += "\tldr r10, " + op2 + "\n";
                } else {
                    code += "\tmov r10, " + op2 + "\n";
                }
                code += "\tadd r12, " + op1 + ", r10\n";
            }
        }
        // r12 contient l'adresse mémoire     
        
        if(op3 != null){ // on affecte la valeur à droite de la flèche à la zone mémoire
            if(op3.getNom().startsWith("r")){ // 
                code += "\tldr " + op3 + ", r4, r12\n"; // affectation
            } else {
                if(op3.estVariable()){ // en mémoire
                    code += "\tstr " + op2.getNom() + ", r4, r12\n"; // affectation
                } else { // valeur immédiate
                    code += "\tstr " + op3 + ", r4, r12\n"; // affectation
                }
            }
        }
        
        return code;
    }
    
}
