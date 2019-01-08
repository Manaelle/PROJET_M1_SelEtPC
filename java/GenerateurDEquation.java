
import com.sun.javafx.font.t2k.T2KFactory;
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
public class GenerateurDEquation {
    
    private ArrayList<Equation> listeEquation;
    private boolean bienTypee = true;
    public GenerateurDEquation(){
        listeEquation = new ArrayList<>();
    }
    
    public ArrayList<Equation> getListeEquation(){
        return this.listeEquation;
    }
    
    public String toString(){
        String resultat= "[";
        for(Equation e : listeEquation){
            resultat+= e.toString();
        }
        resultat+="]";
        return resultat;
    }

    public boolean isBienTypee() {
        return bienTypee;
    }
    
    
    //Voir Doc Typing.ml : Fonction qui genere toutes les equations (de type) de l'expression e de type presumé t
    public void GenererEquations(EnvironnementType env, Exp e, Type t){
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
            Type T = Type.gen();
            GenererEquations(env,((LE) e).e1,T);
            GenererEquations(env,((LE) e).e2,T);     
        }       
        else if (e instanceof Eq){
            listeEquation.add(new Equation(new TBool(), t));
            Type T = Type.gen();
            GenererEquations(env,((Eq) e).e1,T);
            GenererEquations(env,((Eq) e).e2,T);
        }
  
        // le plus dur
        else if (e instanceof Let){   // ex:    let x = 1 + 1 in let y = 2 + x in y 
                                      // e1 -> x = 1 + 1 in || e2 -> let y = 2 +x in y 
            EnvironnementType newEnv = (EnvironnementType) env.clone();
            VarEnv newVar = new VarEnv(((Let) e).id.toString(), ((Let) e).t);
            newEnv.add(newVar);// Mise a jour de l'environnement
            GenererEquations(env, ((Let) e).e1, ((Let) e).t);
            GenererEquations(newEnv, ((Let) e).e2, t);          
        }
        else if (e instanceof Var){
            System.out.println("------>"+((Var) e).id.toString());
            if (env.check(((Var) e).id.toString())){ // On check si la variable est dans l'environnement, si oui on cherche son type et on l'ajoute dans la liste
                Type typeCorrespondant = env.correspondanceVarType(((Var) e).id.toString());
                listeEquation.add(new Equation(typeCorrespondant, t));
            }
            else{
                System.out.println("Problème typage"); // Si non, erreur typage on stop la compil   
                bienTypee = false;
                System.out.println(((Var) e).id.id);
                System.out.println(env.toString());
            }
        } 
        
        else if (e instanceof LetRec){// LetRec x = M in N
            FunDef fun = ((LetRec) e).fd;
            Type retour = Type.gen();
            Exp M = fun.e;
            Exp N = ((LetRec) e).e;
            EnvironnementType env1 = (EnvironnementType) env.clone();
            EnvironnementType env2 = (EnvironnementType) env.clone();
           
            ArrayList<Type> listeArgument = new ArrayList<>();
            if(fun.args.isEmpty()){
                VarEnv newVar = new VarEnv(fun.id.toString(), new TFun(new ArrayList<>(),retour)); // On ajoute un nouvel element à l'environnement contenant 
                env1.add(newVar);                                                                      // le nom de la fonction et le type Tfun sans argument               
                env2.add(newVar);     
            }
            else{
                
                for(int i=0;i<fun.args.size();i++){ // On ajoute tout les arguments dans l'environnement
                    Type ti = Type.gen();
                    listeArgument.add(ti);
                    VarEnv newVar = new VarEnv(fun.args.get(i).toString(), ti);
                    env2.add(newVar);
                }
                VarEnv v = new VarEnv(fun.id.toString(), new TFun(listeArgument, retour)); // Ajout du type de la fonction 
                env1.add(v);
                //System.out.println("//////"+env1);
                env2.add(v);
            }
            
            
            GenererEquations(env2, M, retour);
            GenererEquations(env1, N, t);
        } 
        else if (e instanceof App){
          if (((App)e).es.isEmpty()) {
            GenererEquations(env, ((App)e).e, new TFun(new ArrayList<>(), t));// si pas de parametre alors checker e de type fonction sans parametre
          } else {
            ArrayList<Type> listeTypes = new ArrayList();
            for (int i = 0; i < ((App)e).es.size(); i++){ // On check tout les parametres comme pour letrec
                Type t1 = Type.gen();
                
            //    System.out.println(((App) e).es.get(i));
                String v = ((App) e).es.get(i).toString();
                String[] newV = v.split("\\@");
                v = newV[0];
                Type typeTraduit; 
        
                switch(v){
                    case "Int":
                        GenererEquations(env, ((App)e).es.get(i), new TInt());
                        listeTypes.add(new TInt());
                        break;
                    case "Bool":
                        GenererEquations(env, ((App)e).es.get(i), new TBool());
                        listeTypes.add(new TBool());
                        break;
                    case "Float":
                        GenererEquations(env, ((App)e).es.get(i), new TFloat());
                        listeTypes.add(new TFloat());
                        break;
                    case "Unit" : 
                        GenererEquations(env, ((App)e).es.get(i), new TUnit());
                        listeTypes.add(new TUnit());
                        break;
                    default:
                        listeTypes.add(t1);
                        GenererEquations(env, ((App)e).es.get(i), t1);  
                        
                    }                             
                }
            GenererEquations(env, ((App)e).e, new TFun(listeTypes, t)); 
            }   
        }
        
        else if (e instanceof If){
            GenererEquations(env, ((If) e).e1, new TBool()); // on sait que e1 sera de type bool
            GenererEquations(env, ((If) e).e2, t); 
            GenererEquations(env, ((If) e).e3, t); 
        }


        // Partie pour les tableaux
        else if (e instanceof Array){
            Type T = Type.gen();
            listeEquation.add(new Equation(new TArray(T), t));
            GenererEquations(env,((Array) e).e1,new TInt()); // taille du tableau
            GenererEquations(env,((Array) e).e2,T); // premier element du tableau , on ne sait pas son type    
        }
        else if (e instanceof Get){ // Ne modifie pas la liste des equations de type
            GenererEquations(env,((Get) e).e1,new TArray(t)); // Tableau
            GenererEquations(env,((Get) e).e2,new TInt()); // index  
        }
        else if (e instanceof Put){
            Type tx = Type.gen();
            GenererEquations(env, ((Put)e).e1, new TArray(tx)); //Tableau
            GenererEquations(env, ((Put)e).e2, new TInt()); // index
            GenererEquations(env, ((Put)e).e3, tx); //element a ajouter dont on ne connait pas le type  
        }
        
        // Partie pour les tuples
        else if (e instanceof Tuple){ 
            ArrayList<Type> l = null; 
            
            for(int i =0;i<((Tuple) e).es.size();i++){
                Type tTuple = Type.gen();
                GenererEquations(env, ((Tuple) e).es.get(i),tTuple);
                l.add(tTuple);
            }
            listeEquation.add(new Equation(new TTuple(l), t));   
        }
        else if (e instanceof LetTuple){
            EnvironnementType env1 = new EnvironnementType();
            for(int i=0;i<((LetTuple) e).ids.size();i++){
                VarEnv newVar = new VarEnv(((LetTuple) e).ids.get(i).toString(), Type.gen());
                env1.add(newVar);
                GenererEquations(env, ((LetTuple) e).e1, Type.gen());
        }
            GenererEquations(env1, ((LetTuple) e).e2, t);
        }
    }
    
    public void resoudreEquation(ArrayList<Equation> listeEquation){
        if((listeEquation.isEmpty())||(bienTypee == false)){
            return;
        }
        // la premiere equation 
        Equation e = listeEquation.get(0);
        Type type1 = e.getDepart();
	Type type2 = e.getArrive(); 
        // supprimer la premiere equation 
        System.out.println(" "+listeEquation.toString());
        listeEquation.remove(0);
        String ctype1 = type1.toString();
        String ctype2 = type2.toString();
        
        
        // le meme type  a modifié car c'est moche et avec le ToString ca ne marche pas 
        // quand on fait if(ctype1.equals(c.type2)) ca retourne toujours false 
        Class c1 = type1.getClass();
        Class c2 = type2.getClass();
  
        if((c1.equals(c2)) || (c2.getName().equals("TUnit"))){
            if((type1 instanceof TInt)||(type1 instanceof TFloat)||(type1 instanceof TBool)||(type1 instanceof TUnit)){
                resoudreEquation(listeEquation);  
            }else if(type1 instanceof TVar){
                if(((TVar)type1).equals(((TVar)type2))){ //?t = ?t 
                    resoudreEquation(listeEquation);
                 }else{ ////?t = ?s 
                    ArrayList<Equation> l = new ArrayList<Equation>();// list pour remplacer tout 
                    // remplacer toutes les equations 
                    // remplacement !  
                    int taille = listeEquation.size();
                    for(int i = 0 ; i< taille ; i++){
                        Type t1  = remplacer(listeEquation.get(i).getDepart(),type2,(TVar)type1);
                        Type t2  = remplacer(listeEquation.get(i).getArrive(),type2,(TVar)type1);
                        l.add(new Equation(t1,t2));
                    }
                    listeEquation = l;
                    resoudreEquation(listeEquation);
                     
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
                //   System.out.println("type de retour de " +fun1.ToString()+" est "+fun1.typeRetour.ToString());
                //    System.out.println("type de retour de " +fun2.ToString()+" est "+fun2.typeRetour.ToString());
                    Equation e1 = new Equation(fun1.typeRetour,fun2.typeRetour);
                    listeEquation.add(e1);
                    resoudreEquation(listeEquation);
               }else{
                   this.bienTypee = false ;
               }        
           }
        }else{// des types differents + TVAR 
            
            if(type2 instanceof TVar){
            //    System.out.println("on permute ");
                TVar  type3;
                type3 = new TVar((TVar)type2);
                type2 = type1;
                type1 = type3;    
            }
            if(type1 instanceof TVar){
                if(type2 instanceof TArray ){
                    if((((TArray)type2).getType() instanceof TVar)){
                        listeEquation.add(e);
                    }else{
                        
                        ArrayList<Equation> l = new ArrayList<Equation>();// list pour remplacer tout 
                        // remplacer toutes les equations 
                        // remplacement !  
                        int taille = listeEquation.size();
                        for(int i = 0 ; i< taille ; i++){
                            Type t1  = remplacer(listeEquation.get(i).getDepart(),type2,(TVar)type1);
                            Type t2  = remplacer(listeEquation.get(i).getArrive(),type2,(TVar)type1);
                            l.add(new Equation(t1,t2));
                        }
                        listeEquation = l;     
                    }

                }else if(type2 instanceof TFun ){
                    if(((TFun)type2).contientTvar()){
                        // je pense je dois creer une fonction pour ce bout de code je l'ai copie coller plusieurs fois 
                        ArrayList<Equation> l = new ArrayList<Equation>();// list pour remplacer tout 
                        // remplacer toutes les equations 
                        // remplacement !  
                        int taille = listeEquation.size();
                        for(int i = 0 ; i< taille ; i++){
                            Type t1  = remplacer(listeEquation.get(i).getDepart(),type2,(TVar)type1);
                            Type t2  = remplacer(listeEquation.get(i).getArrive(),type2,(TVar)type1);
                            l.add(new Equation(t1,t2));
                        }
                        listeEquation = l;      
                    }else{
                        listeEquation.add(e);
                    }  
                }else if(type2 instanceof TTuple){
                     if(((TTuple)type2).contientTvar()){
                         ArrayList<Equation> l = new ArrayList<Equation>();// list pour remplacer tout 
                        // remplacer toutes les equations 
                        // remplacement !  
                        int taille = listeEquation.size();
                        for(int i = 0 ; i< taille ; i++){
                            Type t1  = remplacer(listeEquation.get(i).getDepart(),type2,(TVar)type1);
                            Type t2  = remplacer(listeEquation.get(i).getArrive(),type2,(TVar)type1);
                            l.add(new Equation(t1,t2));
                        }
                        listeEquation = l;    
                         
                     }else{
                         listeEquation.add(e);
                     } 
   
                }else{
                    //je sais pas si je dois remplacer ou non
                    ArrayList<Equation> l = new ArrayList<Equation>();
                     int taille = listeEquation.size();
                        for(int i = 0 ; i< taille ; i++){
                            Type t1  = remplacer(listeEquation.get(i).getDepart(),type2,(TVar)type1);
                            Type t2  = remplacer(listeEquation.get(i).getArrive(),type2,(TVar)type1);
                            l.add(new Equation(t1,t2));
                        }
                        listeEquation = l;    
                    
                }
            resoudreEquation(listeEquation);  
            }else{
                System.out.println("une erreur d'unification");
                this.bienTypee = false ;
            }  
            
        }   
    }
    
    
    // remplacer toutes les occurences de tvar  dans type1 par type2   
    public Type remplacer(Type type1 , Type type2 , TVar tvar ){
        if(type1.equals(tvar)){
            return type2;
        }else if(type1 instanceof TTuple){
            ArrayList<Type> list1 = ((TTuple)type1).getList(); 
            ArrayList<Type> list2 = new ArrayList<Type>();
            int taille = list1.size();
            for(int i = 0; i<taille ; i++){
                if(list1.get(i).equals(tvar)){
                    list2.add(type2);
                }else{
                    list2.add(list1.get(i));
                }
            }
            return new TTuple(list2);
            
        }else if (type1 instanceof TArray){
            if(((TArray)type1).getType().equals(tvar)){
                TArray t = new TArray(type2);
                return t;
            }else{
                return type1;
            }
        }else if(type1 instanceof TFun){
            Type typeRetour; 
            ArrayList<Type> list = new ArrayList<Type>();
            ArrayList<Type> args = ((TFun)type1).getArgument();
            Type typeRetourAncien  = ((TFun)type1).typeRetour;
            int taille  = args.size();
            for(int i=0 ; i<taille; i++ ){
                if(args.get(i).equals(tvar)){
                    list.add(type2);
                }else{
                    list.add(args.get(i));
                }
            }
            
            if(typeRetourAncien.equals(tvar)){
                typeRetour = type2;
            }else{
                typeRetour = typeRetourAncien;
            }
            return new TFun(list,typeRetour);      
        }else{
          return type1;   
        }
       
        
    } 
    
}
