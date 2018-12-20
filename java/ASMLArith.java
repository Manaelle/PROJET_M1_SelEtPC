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
    public void renommerVariable(String ancien, String nouveau) {
        op1.renommerVariable(ancien, nouveau);
        op2.renommerVariable(ancien, nouveau);
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
    
}
