/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pepefab
 */
public abstract class ASMLBranche {
    
    private ASMLBranche pere;
    
    public abstract void ajouterInstruction(ASMLExp expression);
    
    public ASMLBranche getPere(){
        return pere;
    }
    
    public void setPere(ASMLBranche pere){
        this.pere = pere;
    }
    
}
