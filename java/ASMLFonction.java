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
public class ASMLFonction extends ASMLBranche implements ASMLLabel {

    private ArrayList<ASMLExp> expressions;
    
    public ASMLFonction(String instruction){
        expressions = new ArrayList<>();
    }
    
    public void renommerVariable(String ancien, String nouveau){
        for(ASMLExp exp : expressions){
            exp.renommerVariable(ancien, nouveau);
        }
    }

    @Override
    public void ajouterInstruction(ASMLExp expression) {
        this.expressions.add(expression);
    }
    
    
}
