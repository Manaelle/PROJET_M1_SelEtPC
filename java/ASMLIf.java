
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pepefab
 */
public class ASMLIf extends ASMLBranche implements ASMLExp{
    
    private ASMLOperande op1;
    private String comparateur;
    private ASMLOperande op2;
    
    private ArrayList<ASMLExp> expThen;
    private ArrayList<ASMLExp> expElse;

    private boolean constructionThen; // sert lors de la construction de l'arbre pour indiquer si on est dans le then ou else
    
    public ASMLIf(String instruction){
        this.constructionThen = true;
        String[] donnees = instruction.split(" ");
        op1 = new ASMLOperande(donnees[1], 
                               donnees[1].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
        comparateur = donnees[2];
        op2 = new ASMLOperande(donnees[3], 
                               donnees[3].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
        expThen = new ArrayList<>(); // remplissage lors de la 
        expElse = new ArrayList<>(); // lecture des autres instructions
    }

    @Override
    public void ajouterInstruction(ASMLExp expression) {
        if(constructionThen){
            expThen.add(expression);
        } else {
            expElse.add(expression);
        }
    }

    public void setConstructionThen(boolean constructionThen) {
        this.constructionThen = constructionThen;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op1);
        a.add(op2);
        for(ASMLExp exp : expThen){
            a.addAll(exp.getOperandes());
        }
        for(ASMLExp exp : expElse){
            a.addAll(exp.getOperandes());
        }
        return a;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        if(op1.getType() == type){
            a.add(op1);
        }
        if(op2.getType() == type){
            a.add(op2);
        }
        for(ASMLExp exp : expThen){
            a.addAll(exp.getOperandes());
        }
        for(ASMLExp exp : expElse){
            a.addAll(exp.getOperandes());
        }
        return a;
    }

    @Override
    public String genererAssembleur() {
        // if    
        String code = "";
        if(op1.getNom().startsWith("r") && op2.getNom().startsWith("r")){ // tout est dans les registres
            code += "\tcmp " + op1 + " " + op2 + "\n";
        } else {
            if(!op1.getNom().startsWith("r") && !op2.getNom().startsWith("r")){ // op1 et op2 doivent être chargé en registre
                if(op1.estVariable()){
                    code += "\tldr r9, " + op1 + "\n";
                } else { // valeur immédiate
                    code += "\tmov r9, " + op1 + "\n";
                }
                if(op2.estVariable()){
                    code += "\tldr r10, " + op2 + "\n";
                } else { // valeur immédiate
                    code += "\tmov r10, " + op2 + "\n";
                }
                code += "\tcmp r9, r10\n";
            } else if(!op1.getNom().startsWith("r")){ // op1 doit être chargé en mémoire
                if(op1.estVariable()){
                    code += "\tldr r9, " + op1 + "\n";
                } else { // valeur immédiate
                    code += "\tmov r9, " + op1 + "\n";
                }
                code += "\tcmp r10, " + op2 + "\n";
            } else { // op2 doit être chargé en mémoire
                if(op2.estVariable()){
                    code += "\tldr r10, " + op2 + "\n";
                } else { // valeur immédiate
                    code += "\tmov r10, " + op2 + "\n";
                }
                code += "\tcmp " + op1 + ", r10\n";
            }
        }
        
        switch(comparateur){ // /!\ les floats ne sont pas gérés
            case "=":
                code += "\tbne TAG_ELSE_" + this.hashCode() + "\n";
                break;
            case "<=":
                code += "\tbgt TAG_ELSE_" + this.hashCode() + "\n";
                break;
            case "<":
                code += "\tbge TAG_ELSE_" + this.hashCode() + "\n";
                break;
            case ">=":
                code += "\tblt TAG_ELSE_" + this.hashCode() + "\n";
                break;
            case ">":
                code += "\tble TAG_ELSE_" + this.hashCode() + "\n";
                break;
        }
        
        // then
        for(ASMLExp exp : expThen){
            code += exp.genererAssembleur();
        }
        
        code += "TAG_ELSE_" + this.hashCode() + ":\n";
        for(ASMLExp exp : expElse){
            code += exp.genererAssembleur();
        }
        
        code += "TAG_SUITE_" + this.hashCode() + ":\n";
        
        return code;
    }
    
    
}
