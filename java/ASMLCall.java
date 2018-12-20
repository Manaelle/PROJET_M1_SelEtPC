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
public class ASMLCall implements ASMLExp {
    
    private String labelFonction;
    private ArrayList<ASMLOperande> parametres;

    public ASMLCall(String instruction){
        String[] donnees = instruction.split(" ");
        labelFonction = donnees[1]; // call XXXXX ... ...
        parametres = new ArrayList<>();
        for(int i = 2; i < donnees.length; i++){
            parametres.add(new ASMLOperande(donnees[i], 
                                            donnees[i].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR));
        }
    }
    
    @Override
    public void renommerVariable(String ancien, String nouveau) {
        for(ASMLOperande op : parametres){
            op.renommerVariable(ancien, nouveau);
        }
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        return parametres;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        for(ASMLOperande op : parametres){
            if(op.getType() == type){
                a.add(op);
            }
        }
        return a;
    }
    
    public String toString(){
        return "call " + this.labelFonction;
    }
    
    
}
