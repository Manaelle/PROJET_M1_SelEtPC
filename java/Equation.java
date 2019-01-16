/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe pour définir une equation 
 * @author boulakhf
 */
public class Equation {
    
    public Type typeDepart;
    public Type typeArrive;
    
    public Equation(Type typeDepart, Type typeArrive){
        this.typeDepart=typeDepart;
        this.typeArrive=typeArrive;
    }

    public Type getDepart() {
        return typeDepart;
    }

    public Type getArrive() {
        return typeArrive;
    }
    
    

    @Override
    public String toString() {
        return '(' + typeDepart.ToString() + "," + typeArrive.ToString() + ')';
    }
    
}
