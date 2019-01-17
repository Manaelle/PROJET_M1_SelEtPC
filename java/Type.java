
import java.util.ArrayList;
import java.util.List;
/**
 * Classe Type
 * @author benmousn
 */
abstract class Type {
    private static int x = 0;
    /**
     * generer le type 
     * @return le Type généré 
     */
    static Type gen() {
        return new TVar("?" + x++);
    }
    public abstract String ToString();
    
    /*public boolean equals(Type t){
       return this.ToString().equals(t);
    }*/
    
}
/**
 * Classe TUnit  pour le type unit 
 * @author benmousn
 */
class TUnit extends Type { 
    public boolean isUnit(Object o){
        return(o instanceof TUnit );
    }

    @Override
    public String ToString() {
	return "unit";
    }

  
}
/**
 * Classe TBool pour le type  booleen
 * @author benmousn
 */
class TBool extends Type {
    
    public boolean isBool(Object o){
        return(o instanceof TBool );
    }

    @Override
    public String ToString() {
        return "bool";  
    }
}
/**
 * Classe TInt pour le type  entier 
 * @author benmousn
 */
class TInt extends Type { 
    public boolean isInt(Object o){
        return(o instanceof TInt );
    }

    @Override
    public String ToString() {
        return "int";
    }
        
}
/**
 * Classe TFloat pour le type float  
 * @author benmousn
 */
class TFloat extends Type { 
 
    public boolean isFloat(Object o){
        return(o instanceof TFloat );
    }

    @Override
    public String ToString() {
	return "float";
    }
}
/**
 * Classe TFun pour le type fonction 
 * @author benmousn
 */
class TFun extends Type {
    public ArrayList<Type> arguments;
    public Type typeRetour;
    
    public TFun(ArrayList<Type> arguments ,Type typeRetour){
        this.arguments = new ArrayList<Type>(arguments);
        if(arguments.isEmpty()){
            this.arguments.add(Type.gen());
        }
        //this.arguments= arguments;
        this.typeRetour = typeRetour;
        
    }
  /*  
    public TFun(List<Type> arguments){
        this.arguments = arguments ; 
    }
    */
    /**
     * 
     * @param o
     * @return renvoie vrai si l'objet testé est une fonction
     */
    public boolean isFun(Object o){
        if(!(o instanceof TFun)){
            return false ; 
            
        }else{
            return (this.typeRetour.equals(((TFun)o).typeRetour)) && (this.arguments.equals(((TFun)o).arguments)) ;
        }
        
        
    }
    @Override
    public String ToString() {
        String s = "(fun : ";
        int i = 0 ;
        while(i<arguments.size()){
            s= s + arguments.get(i).ToString() + "->" ;
            i++;
          }
        s = s + ")";
        return s;     
    }
    public ArrayList<Type> getArgument(){
        return this.arguments;
    }
    /**
     * 
     * @return renvoie faux si la fonction contient un type Tvar
     */
    boolean contientTvar() {
        int taille =  getArgument().size();
        for(int i = 0; i<taille ; i++){
            if(getArgument().get(i) instanceof TVar){
                return false;
                
            }
        }
        System.out.println();
        System.out.println();
        System.out.println(" contien le Tvar ok ou non "+(this.typeRetour instanceof TVar));
        System.out.println();
        System.out.println();

        return !(this.typeRetour instanceof TVar);
    }
    
    
}
/**
 * Classe TTuple pour le type tuple (, , , ...)
 * @author benmousn
 */
class TTuple extends Type { 
    public ArrayList<Type> list; 
    /**
     * constructeur de List de type 
     * @param list 
     */
    public TTuple(ArrayList<Type> list){
        this.list = list;
    }
    /**
     *exemple : let person = ("larry", 47, 165, ’M’);;
     *val person : string * int * int * char = ("larry", 47, 165, ’M’)
     * @return 
     */
    public String ToString(){
        int i =1;
        String s = "(";
        s = s + list.get(0);
        while(i<list.size()){
            s= s + "," + list.get(i); 
            i++;
        }
        s = s + ")";
        //System.out.println("\n\n"+s);
        return s;
       
    }
     /**
     * 
     * @param o
     * @return renvoie vrai si l'objet testé est une fonction
     */
   public boolean isTuple(Object o){
       if(!(o instanceof TTuple)){
           return false; 
       }else{
           return this.list.equals(((TTuple)o).list);   
       }
    }
    public ArrayList<Type> getList(){
	return list;
    }
    /**
     * 
     * @return  renvoie vrai si le tuple contient le type Tvar ?
     */
    boolean contientTvar() {
       int taille =  this.list.size();
       for(int i = 0 ; i< taille ; i++){
           if(this.list.get(i) instanceof TVar){
               return false ;
           }
       }
       return true; 
       
    }
	
    
}
/**
 * Classe TArray pour le type tableau  
 * @author benmousn
 */
class TArray extends Type {
    public Type type;
    
    public TArray(Type type){
        this.type = type; 
        
    }

    TArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public String  ToString(){
        return "array of("+this.type.ToString()+")";
    }
    
    public boolean isArray(Object o){
        if(!(o instanceof TArray )){
            return false ;
        }else {
            return this.type.equals(((TArray)o).type);
        }
    }
    public Type getType(){
        return this.type;
    }
}
/**
 * Classe TVar pour le type var ?
 * @author benmousn
 */
class TVar extends Type {
    Type t; 
    String v;
    TVar(String v) {
        this.v = v;
    }

    public TVar(Type t) {
        this.t = t;
    }
    
    @Override
    public String ToString() {
        return v; 
    }
}
