/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

import java.util.ArrayList;

/**
 *
 * @author Pepefab
 */
public class ASMLLabel implements ASMLExp {

    private String nom;
    
    public ASMLLabel(String instruction){
        this.nom = instruction;
    }
    
    @Override
    public void renommerVariable(String ancien, String nouveau) {        
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        return new ArrayList<ASMLOperande>();
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(ASMLOperande.TypeOperande type) {
        return new ArrayList<ASMLOperande>();
    }
    
    public String toString(){
        return this.nom;
    }
    
}
