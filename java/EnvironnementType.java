
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

}
