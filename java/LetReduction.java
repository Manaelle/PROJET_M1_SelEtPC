
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * documentation  4.6 et http://esumii.github.io/min-caml/index-e.html : Reduction of Nested let (assoc.ml)
 * Classe pour réduire les expressions de Let 
 * @author benmousn
 */
public class LetReduction implements ObjVisitor<Exp> {

    @Override
    public Exp visit(Unit e) {
        return e;
    }

    @Override
    public Exp visit(Bool e) {
        return e;
    }

    @Override
    public Exp visit(Int e) {
        return e ; 
    }

    @Override
    public Exp visit(Float e) {
        return e;
    }

    @Override
    public Exp visit(Not e) {
            Exp e1 = e.e.accept(this);
            return new Not(e1);
            
    }

    @Override
    public Exp visit(Neg e) {
        Exp e1 = e.e.accept(this);
        return new Neg(e1);
    }

    @Override
    public Exp visit(Add e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new Add(e1,e2);
    }

    @Override
    public Exp visit(Sub e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new Sub(e1,e2);
    }

    @Override
    public Exp visit(FNeg e) {
        Exp e1 = e.e.accept(this);
        return new FNeg(e1);
    }

    @Override
    public Exp visit(FAdd e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new FAdd(e1,e2);
    }

    @Override
    public Exp visit(FSub e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new FSub(e1,e2);
    }

    @Override
    public Exp visit(FMul e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new FMul(e1,e2);
    }

    @Override
    public Exp visit(FDiv e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new FDiv(e1,e2);    }

    @Override
    public Exp visit(Eq e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new Eq(e1,e2);
    }

    @Override
    public Exp visit(LE e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new LE(e1,e2);
    }

    @Override
    public Exp visit(If e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Exp e3 = e.e3.accept(this);
        return  new If(e1,e2,e3);
    }

    @Override
    public Exp visit(Let e) {
        Exp e1 = e.e1.accept(this);
        Type t = e.t;
        Id id = e.id;
        
        
        if(e1 instanceof Let ){
            // let x = (let y = e1 in e2) in e3 ---> let y = e1 in let x = e2 in e3
            //System.out.println("je suis dans le let ");
            Let let = (Let) e1;
            Type type = let.t;
            Id i = let.id;
            Exp e2 = let.e1;
            Exp e3 = let.e2;
            Exp e4 = e.e2.accept(this);
            return new Let(i,type,e2,new Let(id,t, e3,e4).accept(this));
   
        }else if(e1 instanceof LetRec){
            //System.out.println("je suis dans le letRec ");
            //a verifier 
            LetRec lrec = (LetRec)e1;
            Exp e2 = lrec.e;
            Exp e3  = e.e2.accept(this);
            FunDef f = lrec.fd;
            return new LetRec(f,new Let(id,t,e2,e3).accept(this));
            
        }else if(e1 instanceof LetTuple){
            //System.out.println("je suis dans le let tuple ");
            LetTuple ltuple = (LetTuple)e1; 
            Exp e2 = ltuple.e1;
            Exp e3 = ltuple.e2;
            Exp e4 = ltuple.e2.accept(this);
            return new LetTuple(ltuple.ids,ltuple.ts,e2,new Let(id,t,e3,e4));
               
        }else{
            //System.out.println("else");
            return new Let(id,t,e1,e.e2.accept(this)); 
            //return null ; 
        }
        
         
    }

    @Override
    public Exp visit(Var e) {
        return e;
    }

    @Override
    public Exp visit(LetRec e) {
        Exp e1 = e.fd.e.accept(this);
        Exp e2 = e.e.accept(this);
        FunDef f = new FunDef(e.fd.id,e.fd.type,e.fd.args,e1);
        return new LetRec(f,e2);
        
        
    }

    @Override
    public Exp visit(App e) {
        
        Exp e1 = e.e.accept(this);
        List<Exp> list= new ArrayList<Exp>(); 
        if(e.es.isEmpty()){
            list = new ArrayList<Exp>();
        }else{
            for(Exp var : e.es){
                Exp ex = var.accept(this);
                list.add(ex);
            }
        }
        return new App(e1,list);
    }   

    @Override
    public Exp visit(Tuple e) {
        
        List<Exp> list= new ArrayList<Exp>(); 
        if(e.es.isEmpty()){
            list = new ArrayList<Exp>();
        }else{
            for(Exp var : e.es){
                Exp ex = var.accept(this);
                list.add(ex);
            }
        }
        return new Tuple(list);
    }

    @Override
    public Exp visit(LetTuple e) {
        Exp e1 = e.e1.accept(this);
        List<Type> types = e.ts;
        List<Id> ids = e.ids;
        
        if(e1 instanceof Let  ){
            Let let = (Let) e1;
            Type type = let.t;
            Id i = let.id;
            Exp e2 = let.e1;
            Exp e3 = let.e2;
            Exp e4 = e.e2.accept(this);
            return new Let(i,type,e2,new LetTuple(ids,types, e3,e4).accept(this));
        }else if(e1 instanceof LetTuple){
            LetTuple ltuple = (LetTuple)e1; 
            Exp e2 = ltuple.e1;
            Exp e3 = ltuple.e2;
            Exp e4 = ltuple.e2.accept(this);
            return new LetTuple(ltuple.ids,ltuple.ts,e2,new LetTuple(ids,types,e3,e4));
                  
        } else if (e1 instanceof LetRec){
            LetRec lrec = (LetRec)e1;
            Exp e2 = lrec.e;
            Exp e3  = e.e2.accept(this);
            FunDef f = lrec.fd;
            return new LetRec(f,new LetTuple(ids,types,e2,e3).accept(this));    
        }else{
            return new LetTuple(ids,types,e1,e.e2.accept(this)); 
   
        }
        
        
    
    }

    @Override
    public Exp visit(Array e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new Array(e1,e2);
    }

    @Override
    public Exp visit(Get e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return  new Get(e1,e2);   
    }

    @Override
    public Exp visit(Put e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Exp e3 = e.e3.accept(this);

        return  new Put(e1,e2,e3);
    }
    
    
}
