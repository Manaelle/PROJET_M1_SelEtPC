import java.util.*;

class KNormVisitor implements ObjVisitor<Exp> {

    public Unit visit(Unit e) {
        return e;
    }

    public Bool visit(Bool e) {
        return e;
    }

    public Int visit(Int e) {
        return e;
    }

    public Float visit(Float e) { 
        return e;
    }

    public Let visit(Not e) {
        Exp e1 = e.e.accept(this);
        Id new_var = Id.gen();
        Type new_type = Type.gen();
        return new Let(new_var, new_type, e1, new Not(new Var(new_var))) ;
    }

    public Let visit(Neg e) {
        Exp e1 = e.e.accept(this);
        Id new_var = Id.gen();
        Type new_type = Type.gen();
        return new Let(new_var, new_type, e1, new Neg(new Var(new_var))) ;
    }

    public Let visit(Add e) {
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                  new Let(new_var2, new_type2, e2,
                    new Add(new Var(new_var1), new Var(new_var2))));
        return res;
    }

	
    public Let visit(Sub e) {
		//TO DO
        return null;
    }

    public Exp visit(FNeg e){
      
       //TO DO
        return null;
    }

    public Let visit(FAdd e){
       //TO DO
        return null;
    }

    public Let visit(FSub e){
        //TO DO
        return null;
    }

    public Let visit(FMul e) {
       //TO DO
        return null;
    }

    public Let visit(FDiv e){
        //TO DO
        return null;
    }

    public Let visit(Eq e){
        //TO DO
        return null;
    }

    public Let visit(LE e){
        //TO DO
        return null;
    }

    public If visit(If e){
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Exp e3 = e.e3.accept(this);
        return new If(e1, e2, e3);
    }

    public Let visit(Let e) {
      //TO DO
       return null;
    }

    public Var visit(Var e){
        return e;
    }

    public Exp visit(LetRec e){
       //TO DO
        return null;
    }

    public Exp visit(App e){
        //TO DO
        return null;
    }

    public Tuple visit(Tuple e){
       //TO DO
        return null;
    }

    public LetTuple visit(LetTuple e){
        //TO DO
        return null;
    }

    public Let visit(Array e){
        //TO DO
        return null;
   }

    public Let visit(Get e){
        //TO DO
        return null; 
    }

    public Let visit(Put e){
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Exp e3 = e.e3.accept(this);

        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Id new_var3 = Id.gen();
        Type new_type3 = Type.gen();

        Let res = 
        new Let(new_var1, new_type1, e1,
          new Let(new_var2, new_type2, e2,
              new Let(new_var3, new_type3, e3,
                new Put(new Var(new_var1), new Var(new_var2), new Var(new_var3)))));
        return res;
    }
}


