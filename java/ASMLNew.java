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
public class ASMLNew implements ASMLExp{
    
    private ASMLOperande op;
    
    public ASMLNew(String exp) {
        String donnee = exp.split(" ")[1];
        op = new ASMLOperande(donnee, donnee.matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
    }

    @Override
    public void renommerVariable(String ancien, String nouveau) {
        op.renommerVariable(ancien, nouveau);
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op);
        return a;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(ASMLOperande.TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        if(op.getType() == type) {
            a.add(op);
        }
        return a;
    }
    
    public String toString(){
        return "new " + this.op;
    }
    
}
