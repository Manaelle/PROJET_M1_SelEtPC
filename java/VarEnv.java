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
    String v; 
    Type t; //type de v
    
    VarEnv(String v, Type t){
        this.v = v;
        this.t = t;
    }    
    
    String getVar(){
      return this.v;  
    }
    
    Type getType(){
        return this.t;
    }
    
}
