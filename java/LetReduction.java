
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
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
            Var e1 = (Var)e.e.accept(this);
            return new Not(e1);
            
    }

    @Override
    public Exp visit(Neg e) {
        Var e1 = (Var)e.e.accept(this);
        return new Neg(e1);
    }

    @Override
    public Exp visit(Add e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new Add(e1,e2);
    }

    @Override
    public Exp visit(Sub e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new Sub(e1,e2);
    }

    @Override
    public Exp visit(FNeg e) {
        Var e1 = (Var)e.e.accept(this);
        return new FNeg(e1);
    }

    @Override
    public Exp visit(FAdd e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new FAdd(e1,e2);
    }

    @Override
    public Exp visit(FSub e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new FSub(e1,e2);
    }

    @Override
    public Exp visit(FMul e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new FMul(e1,e2);
    }

    @Override
    public Exp visit(FDiv e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new FDiv(e1,e2);    }

    @Override
    public Exp visit(Eq e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new Eq(e1,e2);
    }

    @Override
    public Exp visit(LE e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new LE(e1,e2);
    }

    @Override
    public Exp visit(If e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        Var e3 = (Var)e.e3.accept(this);
        return  new If(e1,e2,e3);
    }

    @Override
    public Exp visit(Let e) {
        Exp e1 = e.e1.accept(this);
        Type t = e.t;
        Id id = e.id;
        
        
        if(e1 instanceof Let ){
            // let x = (let y = e1 in e2) in e3 ---> let y = e1 in let x = e2 in e3
            System.out.println("je suis dans le let ");
            Let let = (Let) e1;
            Type type = let.t;
            Id i = let.id;
            Exp e2 = let.e1;
            Exp e3 = let.e2;
            Exp e4 = e.e2.accept(this);
            return new Let(i,type,e2,new Let(id,t, e3,e4).accept(this));
   
        }else if(e1 instanceof LetRec){
            System.out.println("je suis dans le letRec ");
            //a verifier 
            LetRec lrec = (LetRec)e1;
            Exp e2 = lrec.e;
            Exp e3  = e.e2.accept(this);
            FunDef f = lrec.fd;
            return new LetRec(f,new Let(id,t,e2,e3).accept(this));
            
        }else if(e1 instanceof LetTuple){
            System.out.println("je suis dans le let tuple ");
            LetTuple ltuple = (LetTuple)e1; 
            Exp e2 = ltuple.e1;
            Exp e3 = ltuple.e2;
            Exp e4 = ltuple.e2.accept(this);
            return new LetTuple(ltuple.ids,ltuple.ts,e2,new Let(id,t,e3,e4));
               
        }else{
            System.out.println("else");
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
        Var e1 = (Var)e.e.accept(this);
        List<Exp> list = e.es;
        
        
        //todo
        return null;
    }

    @Override
    public Exp visit(Tuple e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Exp visit(LetTuple e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Exp visit(Array e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new Array(e1,e2);
    }

    @Override
    public Exp visit(Get e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        return  new Get(e1,e2);   
    }

    @Override
    public Exp visit(Put e) {
        Var e1 = (Var)e.e1.accept(this);
        Var e2 = (Var)e.e2.accept(this);
        Var e3 = (Var)e.e3.accept(this);

        return  new Put(e1,e2,e3);
    }
    
    
}
