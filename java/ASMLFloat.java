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
public class ASMLFloat implements ASMLLabel {
    
    private ASMLOperande op;
    private float valeur;

    public ASMLFloat(String instruction){
        this.op = new ASMLOperande(instruction.substring(instruction.indexOf(" _"),instruction.indexOf(" =")),
                                   TypeOperande.IMM);
        this.valeur = Float.parseFloat(instruction.substring(instruction.indexOf(" = ") + 3));
    }
    
}
