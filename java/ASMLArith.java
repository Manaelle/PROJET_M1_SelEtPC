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
public class ASMLArith implements ASMLExp {

    private String operateur; // ADD, SUB ...
    private ASMLOperande op1;
    private ASMLOperande op2;
    
    public ASMLArith(String instruction){
        String[] donnees = instruction.split(" ");
        operateur = donnees[0];
        op1 = new ASMLOperande(donnees[1], TypeOperande.VAR);
        op2 = new ASMLOperande(donnees[2], 
                               donnees[2].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
    }
    
    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op1);
        a.add(op2);
        return a;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op1);
        if(op2.getType() == type){
           a.add(op2);
        }
        return a;
    }
    
    public String toString(){
        String res = operateur + " " + op1 + " " + op2;
        return res;
    }

    @Override
    public String genererAssembleur() {
        String code = "";
        if(op1.getNom().startsWith("r") && op2.getNom().startsWith("r")){ // tout est dans les registres
            code += "\t" + operateur + " r12, " + op1 + " " + op2 + "\n";
        } else {
            if(!op1.getNom().startsWith("r") && !op2.getNom().startsWith("r")){ // op1 et op2 doivent être chargé en registre
                if(op1.estVariable()){
                    code += "\tldr r9, " + op1 + "\n";
                } else {
                    code += "`\tmov r9, " + op1 + "\n";
                }
                if(op2.estVariable()){
                    code += "\tldr r10, " + op2 + "\n";
                } else {
                    code += "\tmov r10, " + op2 + "\n";
                }
                code += "\t" + operateur + " r12, r9, r10\n";
            } else if(!op1.getNom().startsWith("r")){ // op1 doit être chargé en mémoire
                if(op1.estVariable()){
                    code += "\tldr r9, " + op1 + "\n";
                } else {
                    code += "`\tmov r9, " + op1 + "\n";
                }
                code += "\t" + operateur + " r12, r10, " + op2 + "\n";
            } else { // op2 doit être chargé en mémoire
                if(op2.estVariable()){
                    code += "\tldr r10, " + op2 + "\n";
                } else {
                    code += "\tmov r10, " + op2 + "\n";
                }
                code += "\t" + operateur + " r12, " + op1 + ", r10\n";
            }
        }
        return code;
    }
    
}
