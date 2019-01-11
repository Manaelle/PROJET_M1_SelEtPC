/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

import java.util.ArrayList;

/**
 *
 * @author Pepefab
 */
public class ASMLOperande implements ASMLExp {


    public enum TypeOperande{
        VAR,
        IMM
    };
    
    private String nom; // nom si variable / valeur si valeur immédiate
    private TypeOperande type;
    
    public ASMLOperande(String nom, TypeOperande type){
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public boolean estVariable() {
        return type == TypeOperande.VAR;
    }
    
    public TypeOperande getType(){
        return this.type;
    }
    

    public void renommerVariable(String nouveau){
        this.nom = nouveau;
    }
    
    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(this);
        return a;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        if(this.type == type){
            a.add(this);
        }
        return a;
    }
    
    public String toString(){
        return this.nom;
    }
    
    @Override
    public String genererAssembleur() {
        if(nom.startsWith("r")){
            return "\tLDR r12, " + nom + "\n";
        } else { // gérer si valeur immédiate ou en mémoire
            if(nom.startsWith("[")){ // mémoire
                String[] donnees = nom.replace("[", "").replace("]","").split(", ");
                return "\tLDR r12, " + "???" + ", " + donnees[1] + "\n"; 
            } else { // valeur immédiate
                return "\tMOV r12, #" + nom + "\n";
            }
        }
        
    }
    
}
