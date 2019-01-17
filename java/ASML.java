/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author lefebfab
 */
public class ASML {
    
    private HashMap<String, ArrayList<String>> labels;
    
    public ASML(String code){
        genererLabels(code);
        immediateOptimization();
        allouerRegistres_Spill();
        genererAssembleur();
    }
    
    private void genererLabels(String code){
        this.labels = new HashMap<>();
        ArrayList<String> lignes = new ArrayList<>(Arrays.asList(code.split("\n")));
        
        String label = "";
        for(String instruction : lignes){
            instruction = instruction.trim();
            if(instruction.matches("let _[a-z0-9_]* =[ 0-9.]*")){ // label (= float ou fonction)
                label = instruction.substring(instruction.indexOf("let _") + 4,
                                              instruction.indexOf("=") - 1);
                labels.put(label, new ArrayList<String>());
                if(!(instruction.trim().endsWith("="))){ // fonction
                    labels.get(label).add(instruction);  
                } 
            } else {
                if(!(instruction.equals(")") || instruction.isEmpty())){
                    if(labels.containsKey(label)){
                        labels.get(label).add(instruction);
                    } else {
                        assert(false);
                    }    
                }
            }
        }
    }
    
    
    
    private void immediateOptimization(){
        
    } 
    
    private void allouerRegistres_Spill(){
        
    }
    
    private void genererAssembleur(){
        
    }
    
    public void afficherLabels(){
        for(String l : labels.keySet()){
            System.out.println("LABEL : " + l);
            for(String s : labels.get(l)){
                System.out.println(s);
            }
        }
    }
    
}
