
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
    
}

class TUnit extends Type { 
    
    public String toString(){
	return "unit";
	}
    
    public boolean equals(Object o){
        return(o instanceof TUnit );
    }
}

class TBool extends Type {
    
    public String toString(){
	return "bool";
    }
    public boolean equals(Object o){
        return(o instanceof TBool );
    }
}

class TInt extends Type { 
    
    public String toString(){
	return "int";
    }
    public boolean equals(Object o){
        return(o instanceof TInt );
    }
        
}

class TFloat extends Type { 
    
    public String toString(){
	return "float";
    }
    public boolean equals(Object o){
        return(o instanceof TFloat );
    }
}

class TFun extends Type {
    public List<Type> arguments;
    public Type typeRetour;
    
    public TFun(List<Type> arguments ,Type typeRetour){
        this.arguments= arguments;
        this.typeRetour = typeRetour;
        
    }
  /*  
    public TFun(List<Type> arguments){
        this.arguments = arguments ; 
    }
    */
    
    public String ToString(Object o){
      String s = "(fun : ";
      int i = 0 ;
      while(i<arguments.size()){
          s= s + arguments.get(i) + "->" ;
          i++;
      }
      s = s + ")";
      return s;  
    }
    
    public boolean equals(Object o){
        if(!(o instanceof TFun)){
            return false ; 
            
        }else{
            return (this.typeRetour.equals(((TFun)o).typeRetour)) && (this.arguments.equals(((TFun)o).arguments)) ;
        }
        
        
    }
    
    
}

class TTuple extends Type { 
    public List<Type> list; 
    /**
     * constructeur de List de type 
     * @param list 
     */
    public TTuple(List<Type> list){
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
    
   public boolean equals(Object o){
       if(!(o instanceof TTuple)){
           return false; 
       }else{
           return this.list.equals(((TTuple)o).list);   
       }
    }
    
}

class TArray extends Type {
    public Type type;
    
    public TArray(Type type){
        this.type = type; 
        
    }
    public String  ToString(){
        return "array of("+this.type+")";
    }
    
    public boolean equals(Object o){
        if(!(o instanceof TArray )){
            return false ;
        }else {
            return this.type.equals(((TArray)o).type);
        }
    }
}

class TVar extends Type {
    String v;
    TVar(String v) {
        this.v = v;
    }
    @Override
    public String toString() {
        return v; 
    }
}

