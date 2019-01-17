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
public class ASMLNew implements ASMLExp{
    
    private ASMLOperande op;
    
    public ASMLNew(String exp) {
        String donnee = exp.split(" ")[1];
        op = new ASMLOperande(donnee, donnee.matches("[0-9]+") ? TypeOperande.IMM : TypeOperande.VAR);
    }


    @Override
    public ArrayList<ASMLOperande> getOperandes() {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        a.add(op);
        return a;
    }

    @Override
    public ArrayList<ASMLOperande> getOperandes(TypeOperande type) {
        ArrayList<ASMLOperande> a = new ArrayList<>();
        if(op.getType() == type) {
            a.add(op);
        }
        return a;
    }
    
    public String toString(){
        return "new " + this.op;
    }

    @Override
    public String genererAssembleur() {
        String code = "";
        code += "\tldr r12, r4\n"; // permet de retourner l'adresse de début de l'allocation
        code += "\tsub r4, r4, #" + op + "\n"; // déplacement du pointeur de tas 
        return code;
    }
    
}
