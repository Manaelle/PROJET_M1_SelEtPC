
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boulakhf
 */

//-------------------------------------------------------
//ENVIRONNEMENT : Construction d'un environnement de type
public class EnvironnementType {
    private ArrayList<VarEnv> gamma; //liste des var dans l'env Gamma
    
    public EnvironnementType(){
        this.predef();
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
    
    //Permet d'ajouter une variable à l'environnement en vérifiant qu'il n'est pas déjà présent
    public void add(VarEnv C){
        
        Boolean ok = true; //restera vrai si C peut être ajouté à Gamma
        
        for  (VarEnv x : this.gamma) {
            if (x.getVar().equals(C.getVar())){
                if(!x.getType().toString().equals(C.getType().toString())){
                    System.out.println("ERREUR TYPAGE: Variable "+C.getVar()+" déjà définie avec le type "+C.getType().ToString());
                }
                ok = false; //inutile de l'ajouter si il est déjà présent
            }
        }
        
        if(ok){
            this.gamma.add(C);
        } 
    }
    
    //Vérifie si la variable est dans l'environnement
    public Boolean check(String v){   
        Boolean found = false;
        for(VarEnv vEnv : gamma){
            if(vEnv.v.equals(v)){
                found = true; 
            }
        } 
        return found;
    }
    
    //Pour une variable v dans l'environnement renvoit le type associé
    public Type correspondanceVarType(String v){ // A completer
        Type tResultat = null;
        for(VarEnv vEnv : gamma){
            if(vEnv.v.equals(v)){
                tResultat = vEnv.t; // VarEnv.t de type string => Pourquoi pas de type Type ?
            }
        } 
        return tResultat;
    }
    
    // des types prédéfinies dans l'env
    public void predef(){
        gamma = new ArrayList<VarEnv>();
        ArrayList<Type> arguments  = new ArrayList<Type>();
        TInt inte = new TInt();
        arguments.add(inte);
        gamma.add(new VarEnv("print_int",new TFun(arguments,new TUnit())));
        //fonction qui prend un entier et renvoie un float 
        gamma.add(new VarEnv("float_of_int",new TFun(arguments,new TFloat())));
        
        arguments = new ArrayList<Type>();
        TFloat floatt = new TFloat();
        arguments.add(floatt);
        //fonction qui prend un float et renvoie un entier 
        gamma.add(new VarEnv("int_of_float",new TFun(arguments,new TInt())));
         
    
    }

}