import java.util.*;

class VarVisitor implements ObjVisitor<Set<String>> {

    public Set<String> visit(Unit e) {
        return new HashSet<String>();
    }

    public Set<String> visit(Bool e) {
        return new HashSet<String>();
    }

    public Set<String> visit(Int e) {
        return new HashSet<String>();
    }

    public Set<String> visit(Float e) { 
        return new HashSet<String>();
    }

    public Set<String> visit(Not e) {
        Set<String> fv = e.e.accept(this);
        return e.e.accept(this);
    }

    public Set<String> visit(Neg e) {
        Set<String> fv = e.e.accept(this);
        return fv;
    }

    public Set<String> visit(Add e) {
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Sub e) {
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FNeg e){
        Set<String> fv = e.e.accept(this);
        return fv;
    }

    public Set<String> visit(FAdd e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FSub e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FMul e) {
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FDiv e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Eq e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(LE e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(If e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        Set<String> fv3 = e.e3.accept(this);
        fv1.addAll(fv2);
        fv1.addAll(fv3);
        return fv1;
    }

    public Set<String> visit(Let e) {
        Set<String> res = new HashSet<String>();
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv2.remove(e.id.toString());
        res.addAll(fv1);
        res.addAll(fv2);
        return res;
    }

    public Set<String> visit(Var e){
        Set<String> res = new HashSet<String>();
        res.add(e.id.toString());
        return res;
    }

    public Set<String> visit(LetRec e){
        Set<String> res = new HashSet<String>();
        Set<String> fv = e.e.accept(this);
        Set<String> fv_fun = e.fd.e.accept(this);
        for (Id id : e.fd.args) {
            fv_fun.remove(id.toString());
        }
        fv.remove(e.fd.id.toString());
        fv_fun.remove(e.fd.id.toString());
        res.addAll(fv);
        res.addAll(fv_fun);
        return res;
    }

    public Set<String> visit(App e){
        Set<String> res = new HashSet<String>();
        res.addAll(e.e.accept(this));
        for (Exp exp : e.es) {
            res.addAll(exp.accept(this));
        }
        return res;
    }

    public Set<String> visit(Tuple e){
        Set<String> res = new HashSet<String>();
        for (Exp exp : e.es) {
            res.addAll(exp.accept(this));
        }
        return res;
    }

    public Set<String> visit(LetTuple e){
        Set<String> res = new HashSet<String>();
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        for (Id id : e.ids) {
            fv2.remove(id.toString());
        }
        res.addAll(fv1);
        res.addAll(fv2);
        return res;
    }

    public Set<String> visit(Array e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Get e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Put e){
        Set<String> fv1 = e.e1.accept(this);
        Set<String> fv2 = e.e2.accept(this);
        Set<String> fv3 = e.e3.accept(this);
        fv1.addAll(fv2);
        fv1.addAll(fv3);
        return fv1;
    }
}


