
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe pour la Construction d'un environnement de type
 * @author boulakhf
 */

//-------------------------------------------------------
//ENVIRONNEMENT : Construction d'un environnement de type

public class EnvironnementType implements Cloneable {
    private ArrayList<VarEnv> gamma; //liste des var dans l'env Gamma
    
    public EnvironnementType(){
        this.predef();
    }
    /**
     * 
     * @return renvoie l'objet cloné
     */
    public Object clone(){
        EnvironnementType newEnv = null;
		try {
			// On récupère l'instance à renvoyer par l'appel de la 
			// méthode super.clone()
			newEnv = (EnvironnementType) super.clone();
                        newEnv.gamma = (ArrayList<VarEnv>) gamma.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		// on renvoie le clone
        return newEnv;
    }

    public ArrayList<VarEnv> getGamma() {
        return gamma;
    }

    public void setGamma(ArrayList<VarEnv> gamma) {
        this.gamma = gamma;
    }
    
    public String toString() {
        String res = "Environnement : ";
        for(VarEnv v : this.gamma){
            res+=v.v + v.t.ToString() + " , ";
        }
        res+=".";
        return res;
    }
    
    /**
     * Permet d'ajouter une variable à l'environnement en vérifiant qu'il n'est pas déjà présent
     * @param C 
     */
    public void add(VarEnv C){
            this.gamma.add(C);
    } 
    
    
    
    /**
     * Vérifie si la variable est dans l'environnement
     * @param v
     * @return 
     */
    public Boolean check(String v){   
        Boolean found = false;
        for(VarEnv vEnv : gamma){
            if(vEnv.v.equals(v)){
                found = true; 
            }
        } 
        return found;
    }
    
    
    /**
     * Pour une variable v dans l'environnement renvoit le type associé
     * @param v
     * @return renvoie le type associé de chaque variable 
     */
    public Type correspondanceVarType(String v){ // A completer
        Type tResultat = null;
        for(VarEnv vEnv : gamma){
            if(vEnv.v.equals(v)){
                tResultat = vEnv.t; // VarEnv.t de type string => Pourquoi pas de type Type ?
            }
        } 
        return tResultat;
    }
    
    
    /**
     * des types prédéfinies dans l'environnement initial 
     */
    public void predef(){
        gamma = new ArrayList<VarEnv>();
        ArrayList<Type> arguments  = new ArrayList<Type>();
        TInt inte = new TInt();
        arguments.add(inte);
        gamma.add(new VarEnv("print_int",new TFun(arguments,new TUnit())));
        gamma.add(new VarEnv("float_of_int",new TFun(arguments,new TFloat()))); //fonction qui prend un entier et renvoie un float 
        arguments = new ArrayList<Type>();
        TFloat floatt = new TFloat();
        arguments.add(floatt);
        
        gamma.add(new VarEnv("int_of_float",new TFun(arguments,new TInt()))); //fonction qui prend un float et renvoie un entier 
        
        arguments = new ArrayList<Type>();
        arguments.add(new TFloat());
        
        gamma.add(new VarEnv("sin",new TFun(arguments, new TFloat()))) ;
        gamma.add(new VarEnv("cos",new TFun(arguments, new TFloat()))) ;
        gamma.add(new VarEnv("sqrt",new TFun(arguments, new TFloat()))) ;
        gamma.add(new VarEnv("sin",new TFun(arguments, new TFloat()))) ;
        gamma.add(new VarEnv("truncate",new TFun(arguments, new TInt()))) ;
        gamma.add(new VarEnv("abs_float",new TFun(arguments, new TFloat()))) ;
        arguments = new ArrayList<Type>();
        arguments.add(new TInt());
        arguments.add(new TUnit());
        gamma.add(new VarEnv("Array.create",new TFun(arguments, new TVar(new TUnit())))) ;
        arguments = new ArrayList<Type>();
        arguments.add(new TUnit());
        gamma.add(new VarEnv("print_newline",new TFun(arguments, new  TUnit()))) ;
        
        arguments = new ArrayList<Type>();
        arguments.add(new TUnit());
        gamma.add(new VarEnv("array_of",new TFun(arguments, new TArray(new TUnit())))) ;
        
        
        //init : A COMPLETER
        //print_newline : A COMPLETER
        //Array.create n 0,00 : A COMPLETER
        //make: A COMPLETER
        


    }

}
