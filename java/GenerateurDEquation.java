
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author boulakhf
 */
public class GenerateurDEquation {
    
    private ArrayList<Equation> listeEquation;
    
    public GenerateurDEquation(){
        listeEquation = new ArrayList<>();
    }
    
    //Voir Doc Typing.ml : Fonction qui genere toutes les equations (de type) de l'expression e de type presumé t
    public void GenererEquations(Environnement env, Exp e, Type t){
        //TODO 
        
        // Cas simples 
        if (e instanceof Int) // A ne pas montrer à M.Tchounikine
            listeEquation.add(new Equation(new TInt(), t));
        else if (e instanceof Bool)
            listeEquation.add(new Equation(new TBool(), t));
        else if (e instanceof Float) 
            listeEquation.add(new Equation(new TFloat(), t));
        else if (e instanceof Unit) 
             listeEquation.add(new Equation(new TUnit(), t));
        
        // A faire avec tout les noeuds possible de l'AST
        else if (e instanceof Not){
            listeEquation.add(new Equation(new TBool(), t));
            GenererEquations(env,((Not) e).e,new TBool());
        }
        
        //Operation sur entier
        else if (e instanceof Add){ 
            listeEquation.add(new Equation(new TInt(), t));
            GenererEquations(env,((Add) e).e1,new TInt());
            GenererEquations(env,((Add) e).e2,new TInt());
        }
        else if (e instanceof Sub){
            listeEquation.add(new Equation(new TInt(), t));
            GenererEquations(env,((Sub) e).e1,new TInt());
            GenererEquations(env,((Sub) e).e2,new TInt());
        }
        
//        else if (e instanceof Mul){ // pas d'expression de multiplication ni de div pour les entiers (wtf)
//            listeEquation.add(new Equation(new TInt(), t));
//            GenererEquations(env,((Mul) e).e1,t);
//            GenererEquations(env,((Mul) e).e2,t);
//        }
        
        //Operation sur floats
        else if (e instanceof FAdd){ 
            listeEquation.add(new Equation(new TFloat(), t));
            GenererEquations(env,((FAdd) e).e1,new TFloat());
            GenererEquations(env,((FAdd) e).e2,new TFloat());
        }
        else if (e instanceof FSub){ 
            listeEquation.add(new Equation(new TFloat(), t));
            GenererEquations(env,((FSub) e).e1,new TFloat());
            GenererEquations(env,((FSub) e).e2,new TFloat());
        }
        else if (e instanceof FMul){ 
            listeEquation.add(new Equation(new TFloat(), t));
            GenererEquations(env,((FMul) e).e1,new TFloat());
            GenererEquations(env,((FMul) e).e2,new TFloat());
        }
        else if (e instanceof FDiv){ //On ne sais pas si il faut prendre en comptre les float pour l'instant on prend que des int
            listeEquation.add(new Equation(new TFloat(), t));
            GenererEquations(env,((FDiv) e).e1,new TFloat());
            GenererEquations(env,((FDiv) e).e2,new TFloat());
        }
        

        //Little equals et equals
        else if (e instanceof LE){
            listeEquation.add(new Equation(new TBool(), t));
            GenererEquations(env,((LE) e).e1,t);
            GenererEquations(env,((LE) e).e2,t);     
        }
        
        else if (e instanceof Eq){
            listeEquation.add(new Equation(new TBool(), t));
            GenererEquations(env,((Eq) e).e1,t);
            GenererEquations(env,((Eq) e).e2,t);     
        }
        
        
        // le plus dur
        else if (e instanceof Let){ //A completer         
            GenererEquations(env, ((Let) e).e1, t);
            GenererEquations(env, ((Let) e).e2, t);          
        }
        else if (e instanceof Var){} // TODO
        else if (e instanceof LetRec){} // TODO
        else if (e instanceof App){} // TODO
        
        else if (e instanceof If){
            GenererEquations(env, ((If) e).e1, new TBool()); // on sait que e1 sera de type bool
            GenererEquations(env, ((If) e).e2, t); 
            GenererEquations(env, ((If) e).e3, t); 
        }
        
    }
    
}
