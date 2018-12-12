abstract class Type {
    private static int x = 0;
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

class TFun extends Type {}

class TTuple extends Type { }

class TArray extends Type {
    public Type type;
    
    public TArray(Type type){
        this.type = type; 
        
    }
    public String  ToString(){
        return "array of("+this.type+")";
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

