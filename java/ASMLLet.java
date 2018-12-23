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

    @Override
    public String genererAssembleur() {
        String code = "";
        code += op2.genererAssembleur();
        if(op1.getNom().startsWith("r")){
            code += "LD " + op1 + ", r12\n";
        } else {
            // il faut mettre r12 à l'emplacement mémoire de op1
        }
        return code;
    }
    
}
