/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
/**
 *
 * @author Pepefab
 */
public enum TypeInstruction {   
    NOP,
    INT,
    IDENT,
    LABEL,
    NEG,
    FNEG,
    FADD,
    FSUB,
    FMUL,
    FDIV,
    NEW,
    ADD,
    SUB,
    MEM,
    IF,
    FI,
    ELSE,
    CALL,
    CALLCLO,
    LET_IN,
    LET_FLOAT,
    LET_FUN;
    
    public static TypeInstruction getTypeInstruction(String instruction){
        instruction = instruction + "   "; // pour pas que le substring plante si c'est plus court
        switch(instruction.substring(0,3)){
            case "nop":
                return TypeInstruction.NOP;
            case "neg":
                return TypeInstruction.NEG;
            case "fne":
                return TypeInstruction.FNEG;
            case "fad":
                return TypeInstruction.FADD;
            case "fsu":
                return TypeInstruction.FSUB;
            case "fdi":
                return TypeInstruction.FDIV;
            case "new":
                return TypeInstruction.NEW;
            case "add":
                return TypeInstruction.ADD;
            case "sub":
                return TypeInstruction.SUB;
            case "mem":
                return TypeInstruction.MEM;
            case "if ":
                return TypeInstruction.IF;
            case ") e":
                if(instruction.trim().equals(") else (")){
                    return TypeInstruction.ELSE;    
                } else {
                    return null;
                }
            case "cal":
                return TypeInstruction.CALL;
            case "app":
                return TypeInstruction.CALLCLO;
            case "let":
                if(instruction.startsWith("let _")){
                    if(instruction.trim().endsWith("=")){
                        return TypeInstruction.LET_FUN;
                    } else {
                        return TypeInstruction.LET_FLOAT;
                    }
                } else {
                    return TypeInstruction.LET_IN;
                }
            default:
                if(instruction.matches("[0-9]+\\s*")){
                    return TypeInstruction.INT;
                } else if(instruction.matches("[a-z0-9]+\\s*")){
                    return TypeInstruction.IDENT;
                } else if(instruction.startsWith("_")){
                    return TypeInstruction.LABEL;
                } else if(instruction.startsWith("(")){
                    return getTypeInstruction(instruction.substring(1,instruction.length()-2).trim());
                } else if(instruction.startsWith(")")) { 
                    return TypeInstruction.FI;
                } else { // en théorie on ne doit entrer ici que si l'instruction est vide, il faut l'ignorer
                    return TypeInstruction.NOP;
                }
        }
    }
}
