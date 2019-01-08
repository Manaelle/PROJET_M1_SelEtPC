
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author boulakhf
 */
public class GenEquation {
    
    private ArrayList<Equation> listeEquation;
    private boolean bienTypee = true;
    
    
    public GenEquation(){
        listeEquation = new ArrayList<>();
    }
    
    
    //-----------------------------------------------------------
    public ArrayList<Equation> getListeEquation(){
        return this.listeEquation;
    }
    
    //-----------------------------------------------------------
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
        
        
        // Partie pour les tableaux
        else if (e instanceof Array){
            listeEquation.add(new Equation(new TArray(t), t));
            GenererEquations(env,((Array) e).e1,new TInt()); // taille du tableau
            GenererEquations(env,((Array) e).e2,t); // premier element du tableau     
        }
        else if (e instanceof Get){ // Ne modifie pas la liste des equations de type
            GenererEquations(env,((Get) e).e1,new TArray(t)); // taille du tableau
            GenererEquations(env,((Get) e).e2,new TInt()); // index  
        }
        else if (e instanceof Put){ 
            //TODO
        }
        
        // Partie pour les tuples
        else if (e instanceof Tuple){ // Jui pas du tout sur de ce que ça donne :/
            List<Type> l = null; 
            
            for(int i =0;i<((Tuple) e).es.size();i++){
                Type tTuple = Type.gen();
                GenererEquations(env, ((Tuple) e).es.get(i),tTuple);
                l.add(tTuple);
            }
            //listeEquation.add(new Equation(new TTuple(l), t));   
        }
    }
    
    //-----------------------------------------------------------
    public void resoudreEquation(ArrayList<Equation> listeEquation){
        if(listeEquation.isEmpty()){
            System.out.println(" equation vide");
            return;
        }
        // la premiere equation 
        Equation e = listeEquation.get(0);
        Type type1 = e.getDepart();
	Type type2 = e.getArrive(); 
        // supprimer la premiere equation 
        listeEquation.remove(0);
        String ctype1 = type1.toString();
        String ctype2 = type2.toString();
        
        
        // le meme type 
        if(ctype1 == ctype2){
            if((type1.toString().equals("int"))||(type1.toString().equals("float"))||(type1.toString().equals("bool"))||(type1.toString().equals("unit"))){
                resoudreEquation(listeEquation);  
            }
            // type Tvar 
            if(type1 instanceof TVar){
                if(((TVar)type1).equals(((TVar)type2))){
                // 
                    resoudreEquation(listeEquation);
                 }else{
                     // remplacement !  
                }
           }else if (type1 instanceof TArray){
               TArray array1  = (TArray)type1;
               TArray array2 =(TArray)type2;
               Equation e1 = new Equation(array1.getType() ,array2.getType());
               listeEquation.add(e1);
               resoudreEquation(listeEquation); 
               // tuple ou struct 
           }else if(type1 instanceof TTuple){
               TTuple tuple1 = (TTuple) type1;
               TTuple tuple2 = (TTuple) type2;
               ArrayList<Type> list1 = tuple1.getList();
               ArrayList<Type> list2 = tuple2.getList();
               int taille1 = list1.size();
               int taille2 = list1.size();
               // on verifie deja la taille des deux struct
               if(taille1 == taille2){
                int i = 0; 
                // resoudre toute les equation de struct 
                while(i<taille1){
                    Equation e1 = new Equation(list1.get(i),list2.get(i));
                    listeEquation.add(e1);
                    i++;
                }
               Equation e1 = new Equation(list1.get(i),list2.get(i));
               resoudreEquation(listeEquation);
               }else{
                   this.bienTypee = false ;
               }    //fonction    
           }else if(type1 instanceof TFun){
               TFun fun1 = (TFun) type1;
               TFun fun2 = (TFun) type2;
               ArrayList<Type> list1 = fun1.getArgument();
               ArrayList<Type> list2 = fun2.getArgument();
               int taille1 = list1.size();
               int taille2 = list1.size();
               // on verifie deja la taille des deux arguments 
               if(taille1 == taille2){
                int i = 0; 
         
                while(i<taille1){
                    Equation e1 = new Equation(list1.get(i),list2.get(i));
                    listeEquation.add(e1);
                    i++;
                } 
                // ajouter le type de retour 
                Equation e1 = new Equation(fun1.typeRetour,fun2.typeRetour);
                listeEquation.add(e1);
                resoudreEquation(listeEquation);
               }else{
                   this.bienTypee = false ;
               }        
           }
      
        
        }else{// des types differents 
            
        }   
    }
    
}
