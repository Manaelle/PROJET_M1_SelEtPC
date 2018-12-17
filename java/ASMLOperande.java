/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

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
    
    @Override
    public void renommerVariable(String ancien, String nouveau) {
        if(nom.equals(ancien)){
            nom = nouveau;
        }
    }
    
}
