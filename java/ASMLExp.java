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
public interface ASMLExp {
 
    public void renommerVariable(String ancien, String nouveau);
    
    public ArrayList<ASMLOperande> getOperandes();
    
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type);
    

}
