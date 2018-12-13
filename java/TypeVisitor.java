/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author benaissm
 */
public class TypeVisitor implements ObjVisitor<Type>{
    
    EnvironnementType g;
    
    public TypeVisitor(EnvironnementType g){
        this.g = g;
    }
    
    //----------------------------------------------------
    @Override
    public Type visit(Unit e) {
        System.out.print("nil");
        return new TUnit();
    }

    @Override
    public Type visit(Bool e) {
        System.out.print("bool");
        return new TBool();
    }

    @Override
    public Type visit(Int e) {
        System.out.print("int");
        return new TInt();
    }

    @Override
    public Type visit(Float e) {
        System.out.print("float");
        return new TFloat();
    }

    //----------------------------------------------------
    //La vérif' de type commence ici
    @Override
    public Type visit(Not e) {
        System.out.print("not(");
        Type t = e.e.accept(this); //vérification que le type de l'expression contenue dans "Not" est un booléen
        System.out.print(")");
        
        if(t.toString().equals("bool")){    
            return t;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Le paramètre de 'Not' n'est pas de type booléen. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
        
    }
    

    @Override
    public Type visit(Neg e) {
        System.out.print("(- ");
        Type t = e.e.accept(this); //vérification que le type de l'expression contenue dans "Not" est un booléen
        System.out.print(")");
        
        if(t.toString().equals("int")){
            return t;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Le paramètre de 'Neg' n'est pas de type int. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
        
    }
    
    //----------------------------------------------------
    //Opération sur des INT SEULEMENT
    @Override
    public Type visit(Add e) {
     
        System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" + ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("int")&&t2.toString().equals("int")){
            return t1; //ou t2
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'Add' ne sont pas de type int. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    @Override
    public Type visit(Sub e) {
        System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" - ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("int")&&t2.toString().equals("int")){
            return t1;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'Sub' ne sont pas de type int. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    //----------------------------------------------------
    @Override
    public Type visit(FNeg e) {
        
        System.out.print("(-. ");
        Type t = e.e.accept(this); //vérification que le type de l'expression contenue dans "Not" est un booléen
        System.out.print(")");
        
        if(t.toString().equals("float")){
            return t;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Le paramètre de 'FNeg' n'est pas de type float. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    @Override
    public Type visit(FAdd e) {
        
        System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" + ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("float")&&t2.toString().equals("float")){
            return t1;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'FAdd' ne sont pas de type float. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    @Override
    public Type visit(FSub e) {
        System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" - ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("float")&&t2.toString().equals("float")){
            return t1;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'FSub' ne sont pas de type float. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    @Override
    public Type visit(FMul e) {
        
        System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" * ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("float")&&t2.toString().equals("float")){
            return t1;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'FMul' ne sont pas de type float. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    @Override
    public Type visit(FDiv e) {
        System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" / ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("float")&&t2.toString().equals("float")){
            return t1;
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'FDiv' ne sont pas de type float. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    @Override
    public Type visit(Eq e) {
        System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" = ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("int")&&t2.toString().equals("int")){
            return new TBool();
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'Eq' ne sont pas de type int. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }

    @Override
    public Type visit(LE e) {
System.out.print("(");
        Type t1 = e.e1.accept(this);
        System.out.print(" < ");
        Type t2 = e.e2.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("int")&&t2.toString().equals("int")){
            return new TBool();
        }
        else{
            System.out.println("\nERREUR TYPAGE: Les paramètres de 'Le' ne sont pas de type int. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }
    
    //----------------------------------------------------
    @Override
    //ici E2 et E3 doivent être du même type
    public Type visit(If e) {
       
        System.out.print("(if ");
        Type t1 = e.e1.accept(this);
        System.out.print(" then ");
        Type t2 = e.e2.accept(this);
        System.out.print(" else ");
        Type t3 = e.e3.accept(this);
        System.out.print(")");
        
        if(t1.toString().equals("bool")&&(t2.toString().equals(t3.toString()))){
            return t2;
        }
        else{
            System.out.println("\nERREUR TYPAGE: La condition n'est pas un booléen, ou les expressions ne sont pas du même type. ");
            return null; //A REVOIR : que retourner en cas de bug ?
        }
    }
    
    //----------------------------------------------------
    @Override
    public Type visit(Let e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //----------------------------------------------------
    @Override
    public Type visit(Var e) {
        System.out.print(e.id);
        return TVar.gen();
    }
    //----------------------------------------------------
    @Override
    public Type visit(LetRec e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type visit(App e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type visit(Tuple e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type visit(LetTuple e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //----------------------------------------------------
    @Override
    public Type visit(Array e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //----------------------------------------------------
    @Override
    public Type visit(Get e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type visit(Put e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
