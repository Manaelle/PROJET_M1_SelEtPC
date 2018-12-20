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
    public void renommerVariable(String ancien, String nouveau) {
        op1.renommerVariable(ancien, nouveau);
        op2.renommerVariable(ancien, nouveau);
        if(op3 != null){
            op2.renommerVariable(ancien, nouveau);
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
    
}
