/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pepefab
 */
public class ASMLFloat implements ASMLFunDefs {
    
    private ASMLOperande op;
    private float valeur;

    public ASMLFloat(String instruction){
        String[] donnees = instruction.split(" ");
        this.op = new ASMLOperande(donnees[1],TypeOperande.VAR);
        //this.valeur = Float.parseFloat(donnees[3]);            
    }
    
    public String toString(){
        return "FLOAT : let " + this.op + " = " + this.valeur + "\n";
    }

    @Override
    public String genererAssembleur() {
        return "FLOAT NON IMPLEMENTE";
    }
    
}
