/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototypeasml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Pepefab
 */
public class ASMLArbre {
    
    private ArrayList<ASMLFunDefs> labels;

    public ASMLArbre(String code){
        labels = new ArrayList<>();
        
        ArrayList<String> instructions = new ArrayList<>(Arrays.asList(code.split("\n")));
        ASMLBranche brancheActuelle = null;
         
        for(String instr : instructions){
            brancheActuelle = decoderInstruction(instr, brancheActuelle);
        } 
    }
    
    private ASMLBranche decoderInstruction(String instruction, ASMLBranche brancheActuelle){
        instruction = instruction.trim();
        switch(TypeInstruction.getTypeInstruction(instruction)){
            case LET_FUN:
                ASMLFonction a_fun = new ASMLFonction(instruction);
                labels.add(a_fun);
                brancheActuelle = a_fun;
                break;
            case LET_FLOAT:
                ASMLFloat a_float = new ASMLFloat(instruction);
                labels.add(a_float);
                break;
            case IF:
                ASMLIf a_if = new ASMLIf(instruction);
                brancheActuelle = a_if;
                break;
            case ELSE:
                ((ASMLIf)brancheActuelle).setConstructionThen(false);
                break;
            case FI:
                brancheActuelle = brancheActuelle.getPere();
                break;
            case LET_IN:
                brancheActuelle.ajouterInstruction(new ASMLLet(instruction));
                break;
            case ADD:
                brancheActuelle.ajouterInstruction(new ASMLArith(instruction));
                break;
            case SUB:
                brancheActuelle.ajouterInstruction(new ASMLArith(instruction));
                break;
            case FADD:
                brancheActuelle.ajouterInstruction(new ASMLArith(instruction));
                break;
            case FSUB:
                brancheActuelle.ajouterInstruction(new ASMLArith(instruction));
                break;
            case CALL:
                brancheActuelle.ajouterInstruction(new ASMLCall(instruction));
                break;
            case NOP:
                break;
            case MEM:
                brancheActuelle.ajouterInstruction(new ASMLMem(instruction));
                break;
            // TODO : gérer les autres cas
        }  
        
        return brancheActuelle;
    }
    
    public void registerAllocation_Spill(){  
        for(ASMLFunDefs label : labels){
            if(label instanceof ASMLFonction){
                // paramètres
                ((ASMLFonction)label).allocationRegistre_Spill();
            }
        }
        
    }
    
    @Override
    public String toString(){
        String res = "-------- ARBRE ----------\n";
        for(ASMLFunDefs a : labels){
            res += a.toString();
        }
        return res;
    }
    
    
}
