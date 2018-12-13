/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypecfg;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 *	Crée un graphe de flot de controle facilitant l'optimisation
 * @author lefebfab
 */
public class CFG {
    
    private enum TypeInstruction{
        IF, // If => 2 nouveaux blocs
        ELSE,
        NVL_FCT, // nouvelle fonction
        FLT, // definition de float
        AUTRES; // Autres (affectation, calcul...) => dans le bloc actuel
    }

    private HashMap<String, Bloc> labels; // Permet  de récuperer la ligne d'un label

    public CFG(String code){
        labels = new HashMap<>();
        ArrayList<String> instructions = new ArrayList<>(Arrays.asList(code.split("\n")));
        instructions.removeAll(Collections.singleton(""));
        
        String instr;
        Bloc b;
        while(!(instructions.isEmpty())){ // Pour chaque fonction
            instr = instructions.get(0).trim(); // let ... = 
            b = new Bloc(1); // Bloc entrant de la fonction
            labels.put(instr.substring(4,instr.indexOf(" =")), b); // Ajout du lien label -> fonction
            if(instr.substring(instr.indexOf("=")+1).isEmpty()){ // c'est une fonction
                instructions.remove(0); // Retrait de l'instructions définissant la fonction
            } else {
            } // c'est un float
            instructions = construireCFG(instructions, b); // Génération du CFG de la fonction
        }
    }
    
    
    /**
     * Fonction récursive permettant de créer le CFG de la fonction/flottant actuelle.
     * @param instructions Les instructions à lire
     * @param blocActuel Le bloc du CFG dans lequel on se situe à l'exécution de la fonction
     * @return Les instructions restantes après ce traitement
    */
    private ArrayList<String> construireCFG(ArrayList<String> instructions, Bloc blocActuel){
        if(!(instructions.isEmpty())){
            String instr = instructions.get(0).trim();
            switch(getTypeInstruction(instr)){
            case IF: 
                // Création des 2 blocs                
                blocActuel.setBlocVrai(new Bloc(blocActuel.getProfondeur() + 1));
                blocActuel.setBlocFaux(new Bloc(blocActuel.getProfondeur() + 1));
                blocActuel.addInstruction(instr.substring(3,instr.indexOf("then")) + "?");
                instructions.remove(0); 
                instructions = construireCFG(instructions, blocActuel.getBlocVrai());
                instructions = construireCFG(instructions, blocActuel.getBlocFaux());
                break;
            case ELSE:
                instructions.remove(0);
                break;
            case NVL_FCT:
                break;
            case FLT:
                blocActuel.addInstruction(instr);
                instructions.remove(0);
                break;
            case AUTRES:
                if(!instr.equals(")")){ 
                    blocActuel.addInstruction(instr);
                    instructions.remove(0);
                    construireCFG(instructions, blocActuel);
                } else { // fin d'un if then else : ce n'est pas une instruction donc on ne la garde pas
                    instructions.remove(0);
                }
                break;
            }
        }
        return instructions;
    }
    
    /**
    * Permet de connaître le type d'une fonction fournie.
    * /!\ Ne doit pas être utilisé pour autre chose que construireCFG() /!\
    * @param instruction L'instruction dont il faut trouver le type
    * @return Le type de l'instruction
    **/
    private TypeInstruction getTypeInstruction(String instruction){
        if(instruction.startsWith("if")){
            return TypeInstruction.IF;
        } else if(instruction.contains("else")){
            return TypeInstruction.ELSE;
        } else if(instruction.startsWith("let _")){
            if(instruction.endsWith("=")){
                return TypeInstruction.NVL_FCT;
            } else {
                return TypeInstruction.FLT;
            }
        }
        else {
            return TypeInstruction.AUTRES;
        }
    }
    
    @Override
    public String toString(){
       String res = "CFG\n";
       for(String l : labels.keySet()){
           res += "LABEL " + l + "\n";
           res += labels.get(l).toString();
       }
       return res;
    }
    
}
