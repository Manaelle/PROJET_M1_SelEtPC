/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Pepefab
 */
public class ASMLCall implements ASMLExp {
    
    private String labelFonction;
    private ArrayList<ASMLOperande> parametres;

    public ASMLCall(String instruction){
        String[] donnees = instruction.split(" ");
        labelFonction = donnees[1]; // call XXXXX ... ...
        parametres = new ArrayList<>();
        for(int i = 2; i < donnees.length; i++){
            parametres.add(new ASMLOperande(donnees[i], 
                                            donnees[i].matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR));
        }
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        return parametres;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        for(ASMLOperande op : parametres){
            if(op.getType() == type){
                a.add(op);
            }
        }
        return a;
    }
    
    public String toString(){
        String res = "call " + this.labelFonction;
        for(ASMLOperande p : parametres){
            res += " " + p;
        }
        return res;
    }

    @Override
    public String genererAssembleur() {
        String code = "";
        // 1-sauvegarder registres r0-r3
        code += "\tpush {r0-r3}\n";
        // 2- paramètres
        ASMLOperande p;
        for(int i = 0; i < parametres.size(); i++){
            p = parametres.get(i);
            if(i < 4){ // on les charge dans les registres r0-r3
                if(p.estVariable()){
                    if(p.getNom().startsWith("r")){ // registre
                        code += "\tldr r" + i + ", " + p.getNom() + "\n";
                    } else { // pile
                        code += "\tldr r" + i + ", " + p.getNom() + "\n";
                    }
                } else { // valeur immédiate
                    code += "\tmov r" + i + ", #" + p.getNom() + "\n";
                }
            } else { // il faut le mettre sur la pile
                if(p.estVariable()){
                    if(p.getNom().startsWith("r")){ // registre 
                        code += "\tpush {" + p.getNom() + "}\n";
                    } else { // pile
                        code += "\tldr r12, sp, " + p.getNom() + "\n";
                        code += "\tpush {r12}\n";
                    }
                } else { // valeur immédiate
                    code += "\tmov r12, #" + p.getNom() + "\n";
                    code += "\tpush {r12}\n";
                }                
            }
        }
        // 3-appeler la fonction
        code += "\tbl " + this.labelFonction + "\n";
        // 4-restaurer les registres
        code += "\tpop {r0-r3}\n";
        return code;
    }
    
    
}
