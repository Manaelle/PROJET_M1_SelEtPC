import java.util.*;

// Example of Visitor that computes a value (returns
// the height of the AST) but uses the simple visitor for which
// every method returns void. Not recommended.
// 
// To use it, add the following lines in your main(),
// where "result" is the Exp produced by the parser.
//
//      HeightVisitor v = new HeightVisitor();
//      result.accept(v);
//      System.out.println("Height = " + v.getRes());
//
// Similarly, you can compute more complex values based
// on this scheme. For instance, the set of free variables,
// or generate a new tree.
 
class HeightVisitorSimple implements Visitor {

    // this variable is used to store the result of 
    // each visit method. In particular, we can consider
    // that after each call to "accept", res stores the
    // result produced by the visitor
    private int res;

    // Used to get the final result from the Main method 
    public int getRes() {
        return res;
    }

    public void visit(Unit e) {
        // This tree is of height 0
        res = 0;
    }

    public void visit(Bool e) {
        res = 0;
    }

    public void visit(Int e) {
        res = 0;
    }

    public void visit(Float e) { 
        res = 0;
    }

    public void visit(Not e) {
        // we compute the height of e's only subtree
        // we know that after this call, the height of the
        // tree will be stored in res
        e.e.accept(this);
        // we increment res, as e is one unit higher then e.e
        res++;
    }

    public void visit(Neg e) {
        e.e.accept(this);
        res++;
    }

    public void visit(Add e) {
        // this not has two subtrees
        // we retrieve e1 height with accept call on left subtree
        e.e1.accept(this);
        // we need to copy res as it will be updated by the next
        // accept call
        int res1 = res;
        // same thing on right subtree
        e.e2.accept(this);
        int res2 = res;
        // finally, we store e's height in res
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(Sub e) {
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
   }

    public void visit(FNeg e){
        e.e.accept(this);
        res++;
    }

    public void visit(FAdd e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(FSub e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(FMul e) {
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
     }

    public void visit(FDiv e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(Eq e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(LE e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(If e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        e.e3.accept(this);
        int res3 = res;
        res = Math.max(res1, Math.max(res2, res3)) + 1 ;
    }

    public void visit(Let e) {
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(Var e){
        res = 0;
    }

    public void visit(LetRec e){
        e.e.accept(this);
        int res1 = res;
        e.fd.e.accept(this);
        int res2 = res;
        res = Math.max(res1, res2) + 1 ;
    }

    public void visit(App e){
        e.e.accept(this);
        int res1 = res;
        for (Exp exp : e.es) {
            exp.accept(this);
            res1 = Math.max(res1, res);
        }
        res = res1 + 1;
    }

    public void visit(Tuple e){
        int res1 = 0;
        for (Exp exp : e.es) {
            exp.accept(this);
            res1 = Math.max(res, res1);
        }
        res = res1 + 1;
    }

    public void visit(LetTuple e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2);
    }

    public void visit(Array e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2);
    }

    public void visit(Get e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        res = Math.max(res1, res2);
    }

    public void visit(Put e){
        e.e1.accept(this);
        int res1 = res;
        e.e2.accept(this);
        int res2 = res;
        e.e3.accept(this);
        int res3 = res;
        res = Math.max(res1, Math.max(res2, res3)) + 1 ;
    }
}


