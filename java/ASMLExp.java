/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Pepefab
 */
public interface ASMLExp {
     
    public ArrayList<ASMLOperande> getOperandes();
    
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type);
    
    public String genererAssembleur();

}
