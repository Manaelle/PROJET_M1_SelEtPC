/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

/**
 *
 * @author Pepefab
 */
public class ASMLMem implements ASMLExp {
    
    private ASMLOperande op1;
    private ASMLOperande op2;
    private ASMLOperande op3; // falcutatif

    @Override
    public void renommerVariable(String ancien, String nouveau) {
        op1.renommerVariable(ancien, nouveau);
        op2.renommerVariable(ancien, nouveau);
        if(op3 != null){
            op2.renommerVariable(ancien, nouveau);
        }
    }
    
    
}
