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
    
    @Override
    public void renommerVariable(String ancien, String nouveau) {
        if(nom.equals(ancien)){
            nom = nouveau;
        }
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
    
}
