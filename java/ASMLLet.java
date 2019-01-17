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
public class ASMLLet implements ASMLExp {
    
    private ASMLOperande op1;
    private ASMLExp op2;

    public ASMLLet(String instruction){
        String[] donnees = instruction.split(" ");
        op1 = new ASMLOperande(donnees[1], 
                               donnees[1].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
        
        String exp = "";
        for(int i = 3; i < donnees.length - 1; i++){
            exp = exp + donnees[i] + " ";
        }
        exp = exp.trim();
        switch(TypeInstruction.getTypeInstruction(exp)) {
            case INT:
                op2 = new ASMLOperande(exp, TypeOperande.IMM);
                break;
            case IDENT:
                op2 = new ASMLOperande(exp, TypeOperande.VAR);
                break;
            case CALL:
                op2 = new ASMLCall(exp);
                break;
            case MEM:
                op2 = new ASMLMem(exp);
                break;
            case ADD:
                op2 = new ASMLArith(exp);
                break;
            case SUB:
                op2 = new ASMLArith(exp);
                break;
            case FSUB:
                op2 = new ASMLArith(exp);
                break;
            case FADD:
                op2 = new ASMLArith(exp);
                break;
            case LABEL: 
                op2 = new ASMLLabel(exp);
                break;
            case NEW:
                op2 = new ASMLNew(exp);
                break;
            default:
                System.out.println("Erreur : " + instruction);
                break;
        }
    }
   

    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op1);
        a.addAll(op2.getOperandes());
        return a;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op1);
        a.addAll(op2.getOperandes(type));
        return a;
    }
    
    public String toString(){
        String res = "let " + op1 + " = " + op2;
        return res;
    }

    
    // amélioration possible : 
    // actuellement on calcule le résultat de l'expression à droite et on le met dans r12, puis on déplace ce résultat.
    // il serait bien de mettre le résultat directement au bon endroit
    @Override
    public String genererAssembleur() {
        String code = "";
        code += op2.genererAssembleur();
        if(op1.getNom().startsWith("r")){
            code += "\tldr " + op1 + ", r12\n";
        } else {
            // il faut mettre r12 à l'emplacement mémoire de op1
            String[] donnees = op1.getNom().replace("[", "").replace("]","").split(", ");
            code += "\tstr r12, " + "fp" + ", " + donnees[1] + "\n";
        }
        return code;
    }
    
}
