/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

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
    
}
