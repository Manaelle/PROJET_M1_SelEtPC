/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boulakhf
 */
public class TypeFactory { // Useless mais à garder au cas ou
 
    
    public Type stringToType(String v){
        String[] newV = v.split("\\@");
        v = newV[0];
        switch(v){
            case "Int":
                return new TInt();
            case "Bool":
                return new TBool();
            case "Float":
                return new TFloat();
            case "Unit" : 
                return new TUnit();
            default:
                return Type.gen();        
        }
    }
}
