package prototypeasml;

import java.util.ArrayList;
import prototypeasml.ASMLOperande.TypeOperande;

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
        op1 = new ASMLOperande(donnees[3], 
                               donnees[3].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
        expThen = new ArrayList<>(); // remplissage lors de la 
        expElse = new ArrayList<>(); // lecture des autres instructions
    }
    
    @Override
    public void renommerVariable(String ancien, String nouveau) {
        op1.renommerVariable(ancien, nouveau);
        op2.renommerVariable(ancien, nouveau);
        for(ASMLExp exp : expThen){
            exp.renommerVariable(ancien, nouveau);
        }
        for(ASMLExp exp : expElse){
            exp.renommerVariable(ancien, nouveau);
        }
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
    
    
}
