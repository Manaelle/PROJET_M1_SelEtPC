import java.util.*;

class KNormVisitor implements ObjVisitor<Exp> {

    @Override
    public Unit visit(Unit e) {
        return e;
    }

    @Override
    public Bool visit(Bool e) {
        return e;
    }

    @Override
    public Int visit(Int e) {
        return e;
    }

    @Override
    public Float visit(Float e) { 
        return e;
    }

    @Override
    public Let visit(Not e) {
        Exp e1 = e.e.accept(this);
        Id new_var = Id.gen();
        Type new_type = Type.gen();
        return new Let(new_var, new_type, e1, new Not(new Var(new_var))) ;
    }

    @Override
    public Let visit(Neg e) {
        Exp e1 = e.e.accept(this);
        Id new_var = Id.gen();
        Type new_type = Type.gen();
        return new Let(new_var, new_type, e1, new Neg(new Var(new_var))) ;
    }

    @Override
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

	
    @Override
    public Let visit(Sub e) {
        //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new Sub(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
    public Exp visit(FNeg e){
        //TO DO
        Exp e1 = e.e.accept(this);
        Id new_var = Id.gen();
        Type new_type = Type.gen();
        return new Let(new_var, new_type, e1, new FNeg(new Var(new_var))) ;
    }

    @Override
    public Let visit(FAdd e){
       //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new FAdd(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
    public Let visit(FSub e){
        //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new FSub(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
    public Let visit(FMul e) {
       //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new FMul(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
    public Let visit(FDiv e){
        //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new FDiv(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
    public Let visit(Eq e){
        //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new Eq(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
    public Let visit(LE e){
        //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new LE(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
    public If visit(If e){
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Exp e3 = e.e3.accept(this);
        return new If(e1, e2, e3);
    }

    @Override
    public Let visit(Let e) {
        //TO DO
        /*Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        return new Let(new_var1, new_type1, e1, e2);*/
        Let res = new Let(e.id, e.t, e.e1.accept(this), e.e2.accept(this));
        return res;
    }

    @Override
    public Var visit(Var e){
        return e;
    }

    @Override
    public Exp visit(LetRec e){
        //TO DO
        FunDef fd = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e);
        LetRec res = new LetRec(fd,e);
        return res;
    }

    Exp rec_app(List<Exp> llet, List<Exp> la, Exp e) {
        if (llet.isEmpty()) {
            return new App(e, la);
        } else {
            Id i = Id.gen();
            Type t = Type.gen();
            la.add(new Var(i));
            Exp elt = llet.remove(0);
            Let l = new Let(i, t, elt, rec_app(llet, la, e));
            return l;
        }
    }
    
    List<Exp> rec_list(List<Exp> l) {
        List<Exp> new_list = new LinkedList<>();
        new_list.clear();
        if (l.isEmpty()) {
            new_list.add(new Unit());
        }
        Iterator<Exp> it = l.iterator();
        while (it.hasNext()) {
            new_list.add(it.next().accept(this));
        }
        return new_list;
    }
        
    @Override
    public Exp visit(App e){
        //TO DO
        List<Exp> list = new LinkedList<>();
        Exp res = rec_app(rec_list(e.es), list, e.e);
        return res;
    }

    @Override
    public Tuple visit(Tuple e){
        //TO DO
        List<Exp> list_tuple_es = new ArrayList<Exp>();
        for (int i = 0; i < e.es.size(); i++){
            Exp tuple_cpt = e.es.get(i);
            list_tuple_es.add(tuple_cpt.accept(this));
        }
        return new Tuple(list_tuple_es);
    }

    @Override
    public LetTuple visit(LetTuple e){
        //TO DO
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Let visit(Array e){
        //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        Let res = new Let(new_var1, new_type1, e1,
                    new Let(new_var2, new_type2, e2,
                    new Array(new Var(new_var1), new Var(new_var2))));
        return res;
   }

    @Override
    public Let visit(Get e){
        //TO DO
        Exp e1 = e.e1.accept(this);
        Exp e2 = e.e2.accept(this);
        
        Id new_var1 = Id.gen();
        Type new_type1 = Type.gen();
        Id new_var2 = Id.gen();
        Type new_type2 = Type.gen();
        
        Let res = 
        new Let(new_var1, new_type1, e1,
          new Let(new_var2, new_type2, e2,
              new Get(new Var(new_var1), new Var(new_var2))));
        return res;
    }

    @Override
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


