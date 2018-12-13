
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
    ArrayList<VarEnv> gamma; //liste des var dans l'env Gamma
    
    public EnvironnementType(){
        gamma = new ArrayList();
    }
    
    //Permet d'ajouter une variable à l'environnement en vérifiant qu'il n'est pas déjà présent
    public void add(String v, Type t){
        
        VarEnv C = new VarEnv(v, t);
        
        Boolean ok = true; //restera vrai si C peut être ajouté à Gamma
        
        for  (VarEnv x : gamma) {
            if (x.getVar().equals(C.getVar())){
                if(!x.getType().toString().equals(C.getType().toString())){
                    System.out.println("ERREUR TYPAGE: Variable "+C.getVar()+" déjà définie avec le type "+C.getType());
                }
                ok = false; //inutile de l'ajouter si il est déjà présent
            }
        }
        
        if(ok){
            gamma.add(C);
        } 
    }
    
    //Vérifie si la variable est dans l'environnement
    public Boolean check(String v){   
        return true;
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
