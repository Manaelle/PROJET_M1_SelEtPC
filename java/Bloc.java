/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypecfg;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Un Bloc (Basic Block) est une portion de code source servant au CFG
 * @author lefebfab
 */
public class Bloc {
    
    private int profondeur;
    private ArrayList<String> instructions;
    private Bloc blocFaux;
    private Bloc blocVrai;
    
    public Bloc(int profondeur){
        this.profondeur = profondeur;
        this.instructions = new ArrayList<String>();
    }
    
    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void addInstruction(String instruction) {
        this.instructions.add(instruction);
    }

    public Bloc getBlocFaux() {
        return blocFaux;
    }

    public void setBlocFaux(Bloc blocFaux) {
        this.blocFaux = blocFaux;
    }

    public Bloc getBlocVrai() {
        return blocVrai;
    }

    public void setBlocVrai(Bloc blocVrai) {
        this.blocVrai = blocVrai;
    }

    public int getProfondeur() {
        return profondeur;
    }
    
    public String toString(){
        String prefix = String.join("",Collections.nCopies(this.profondeur,".")) ;
        String res = prefix + "NOUVEAU BLOC\n";
        for(String instr : this.instructions){
            res += prefix + instr + "\n";
        }
        if(this.blocVrai != null){
            res += this.blocVrai.toString();
            res += this.blocFaux.toString();
        }
        return res;
    }
       
}
