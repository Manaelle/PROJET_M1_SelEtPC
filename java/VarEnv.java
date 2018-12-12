/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author benaissm
 */

//-------------------------------------------------------
//Tuple <variable, type> à ajouter dans l'environnement
public class VarEnv {
    String v; //variable v de type T
    String t; //type de v
    
    VarEnv(String v, String t){
        this.v = v;
        this.t = t;
    }    
    
    String getVar(){
      return this.v;  
    }
    
    String getType(){
        return this.t;
    }
    
}
