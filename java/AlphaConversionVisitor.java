
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * creer un hashmap (on fait correspondre ancien nom avec nouveau nom)
 * map(string, id) conv
 * let/letrec > rajouter les nom de variables dans la hashmap
 * alpha epsilon : var non connue / alpha epsilon x -> x' : x est l'ancienne variable qui correspond à la nouvelle : x' 
 * @author Guillaume
 */
public class AlphaConversionVisitor implements ObjVisitor<Exp>  {
    // Environnement des noms de variables
    private HashMap<String, Stack> env = new HashMap<String, Stack> ();
    
    // Variables utilisées dans Let/LetRec
    private HashMap<String, Stack> usedVariables = new HashMap<String, Stack> ();
    
    // Ensembles des fonctions
    private HashSet<String> functions = new HashSet();
    
    private boolean isAfterLet = false;
    
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
        return e;
    }

    @Override
    public Exp visit(Float e) {
        return e;
    }

    @Override
    public Exp visit(Not e) {
        return new Not(e.e.accept(this));
    }

    @Override
    public Exp visit(Neg e) {
        return new Neg(e.e.accept(this));
    }

    @Override
    public Exp visit(Add e) {
        return new Add(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Sub e) {
        return new Sub(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FNeg e) {
        return new FNeg(e.e.accept(this));
    }

    @Override
    public Exp visit(FAdd e) {
        return new FAdd(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FSub e) {
        return new FSub(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FMul e) {
        return new FMul(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FDiv e) {
        return new FDiv(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Eq e) {
        return new Eq(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(LE e) {
        return new LE(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(If e) {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    @Override
    public Exp visit(Let e) {
        Var generatedVariable = new Var(e.id.gen());
        Stack stack = env.get(e.id.toString());
        
        // cas où la variable n'existe pas dans l'environnement
        if(stack == null){
            stack = new Stack();
            env.put(e.id.toString(), stack);
        }
        
        stack.push(generatedVariable.id.toString()); // on rajoute la variable dans la pile 
        
        Let returnedLet = new Let(generatedVariable.id, e.t, e.e1.accept(this), e.e2.accept(this));
        isAfterLet = true;
        
        Stack usesOfThisVar = usedVariables.get(generatedVariable.id.toString());
        
        if(usesOfThisVar == null){ // variable jamais utilisée
            usesOfThisVar = new Stack();
            usedVariables.put(e.id.toString(), usesOfThisVar);
        }
        
        HashSet set = new HashSet();
        while(!usesOfThisVar.isEmpty()){ // Parcours des variables utilisées
            String key = (String) usesOfThisVar.pop();
            
            if ((stack.size() > 1 && functions.contains(key)) || !functions.contains(key)){
                Stack s = env.get(key);
                if (!s.empty() && !set.contains(key)){
                    s.pop();
                    set.add(key);
                }
            }
        }
        
        return returnedLet;
    }

    @Override
    public Exp visit(Var e) {
        Stack stack = env.get(e.id.toString());
        
        // cas où la variable n'existe pas dans l'environnement
        if(stack == null){
            stack = new Stack();
            env.put(e.id.toString(), stack);
        }
        
        if (!stack.empty()){
            if(!isAfterLet){ // variable = fonction
                if (functions.contains(e.id.toString())){
                    return new Var(new Id((String) stack.peek()));
                }else{
                    Var var = new Var(e.id.gen());
                    stack.push(var.id.toString());
                    return var;
                }
            }else{ // Variable locale
                Stack usesOfThisVar = env.get(e.id.toString());
                if (usesOfThisVar == null){
                  usesOfThisVar = new Stack();
                  env.put(e.id.toString(), usesOfThisVar);
                }
                usesOfThisVar.push(e.id.toString());
                Var var = new Var(new Id((String) stack.peek()));
                return var;
            }
        }else{ 
            return e;
        }
    }

    @Override
    public Exp visit(LetRec e) {
        functions.add(e.fd.id.toString());
        Id newId = e.fd.id.gen();
        Stack stack = env.get(e.fd.id.toString());
        
        if (stack == null) {
          stack = new Stack();
          env.put(e.fd.id.toString(), stack);
        }
        
        stack.push(newId.toString());
        List<Id> idList = new LinkedList();
        
        for (Id arg: e.fd.args){
          Id new_arg = arg.gen();
          Stack argStack = env.get(arg.toString());
          
          if (argStack == null){
            argStack = new Stack();
            env.put(arg.toString(), argStack);
          }
          
          argStack.push(new_arg.toString());
          idList.add(new_arg);
        }
        
        isAfterLet = true;
        Exp exp = e.fd.e.accept(this);
        FunDef funDef = new FunDef(newId, e.fd.type, idList, exp);
        
        for (Id arg: e.fd.args){
          Stack s = env.get(arg.toString());
          if (!s.empty()) {
            s.pop();
          }
        }
        
        isAfterLet = false;
        return new LetRec(funDef, e.e.accept(this));
    }

    @Override
    public Exp visit(App e) {
        Exp app = e.e.accept(this);
        List<Exp> expList = new LinkedList<Exp>();
        
        for (Exp exp: e.es){
          expList.add(exp.accept(this));
        }
        
        return new App(app, expList);
    }

    @Override
    public Exp visit(Tuple e) {
        List<Exp> expList = new LinkedList<Exp>();
        isAfterLet = true;
        
        for (Exp exp: e.es){
          expList.add(exp.accept(this));
        }
     
        return new Tuple(expList);
    }

    @Override
    public Exp visit(LetTuple e) {
        List<Id> idLists = new LinkedList<Id>();
        
        for (Id id: e.ids){
          Id newId = id.gen();
          Stack stack = env.get(id.toString());
          
          if (stack == null){
            stack = new Stack();
            env.put(id.toString(), stack);
          }
          
          stack.push(newId.toString());
          idLists.add(newId);
        }

        return new LetTuple(idLists, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Array e) {
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Get e) {
        return new Get(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Put e) {
        return new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }
    
}
