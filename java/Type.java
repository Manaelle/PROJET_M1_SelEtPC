
import java.util.ArrayList;
import java.util.List;

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
    
}

class TUnit extends Type { 
    public boolean isUnit(Object o){
        return(o instanceof TUnit );
    }

    @Override
    public String ToString() {
	return "unit";
    }

  
}

class TBool extends Type {
    
    public boolean isBool(Object o){
        return(o instanceof TBool );
    }

    @Override
    public String ToString() {
        return "bool";  
    }
}

class TInt extends Type { 
    public boolean isInt(Object o){
        return(o instanceof TInt );
    }

    @Override
    public String ToString() {
        return "int";
    }
        
}

class TFloat extends Type { 
 
    public boolean isFloat(Object o){
        return(o instanceof TFloat );
    }

    @Override
    public String ToString() {
	return "float";
    }
}

class TFun extends Type {
    public ArrayList<Type> arguments;
    public Type typeRetour;
    
    public TFun(ArrayList<Type> arguments ,Type typeRetour){
        this.arguments= arguments;
        this.typeRetour = typeRetour;
        
    }
  /*  
    public TFun(List<Type> arguments){
        this.arguments = arguments ; 
    }
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
            s= s + arguments.get(i) + "->" ;
            i++;
          }
        s = s + ")";
        return s;     
    }
    public ArrayList<Type> getArgument(){
        return this.arguments;
    }

    boolean contientTvar() {
        int taille =  getArgument().size();
        for(int i = 0; i<taille ; i++){
            if(getArgument().get(i) instanceof TVar){
                return false;
                
            }
        }
        return !(this.typeRetour instanceof TVar);
    }
    
    
}

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
            s= s + "* " + list.get(i); 
            i++;
        }
        s = s + ")";
        return s;
       
    }
    
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
