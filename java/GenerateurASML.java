
import java.util.Iterator;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boulakhf
 */
public class GenerateurASML implements ObjVisitor<String> { //Doc ASML/md pour comprendre
    
    private static String entryPoint ="\nlet _ = \n\t";
    private int vn = 0; 
    private static String floatDeclaration ="";
    private static String autreDeclaration ="";
    private static String asmlEnSortie;
    
    
    public String newVariable(int i){
	return String.format("v%s", i);
    }
    
    @Override
    public String visit(Unit e) {
        return e.toString();
    }

    @Override
    public String visit(Bool e) {
        if(e.b == false) {
            return String.format("%s",0);
    	}
        return String.format("%s",1);
    }

    @Override
    public String visit(Int e) {
        return String.format("%s",e.i);
    }

    @Override
    public String visit(Float e) {
        String s = String.format("%.2f", e.f);
    	s=s.replace(',','.'); //ancien "," , nouveau "."
        return s;
    }

    @Override
    public String visit(Not e) {
        return String.format("%s",e.e.accept(this));
    }

    @Override
    public String visit(Neg e) {
        String res ="";
    	if(e.e instanceof Var){
            res += String.format("neg %s",e.e.accept(this));
    	} else {
            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",newVariable(vn),e.e.accept(this));
            res += String.format("neg %s",newVariable(vn));
            vn++;          
    	}
    	return res ;
    }

    @Override
    public String visit(Add e) {
        String res = new String();
    	if(e.e1 instanceof Var){
            if(e.e2 instanceof Var){
                    res += String.format("add %s %s",e.e1.accept(this), e.e2.accept(this));
            } else {
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",newVariable(vn),e.e2.accept(this));
                    res += String.format("add %s %s ", newVariable(vn),e.e1.accept(this));
                    vn++;
            }
        } else {
            if(e.e2 instanceof Var){
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",newVariable(vn),e.e1.accept(this));
                    res += String.format("add %s %s ", newVariable(vn),e.e2.accept(this));
                    vn++;
            } else {
                    String v1=newVariable(vn);
                    vn++;
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), newVariable(vn),e.e1.accept(this));
                    res += String.format("add %s %s ",v1,newVariable(vn));
                    vn++;
            }
        }
    	return res ;
    }

    @Override
    public String visit(Sub e) {
        String res ="";
        if(e.e1 instanceof Var){
            if(e.e2 instanceof Var){
                        res += String.format("sub %s %s",e.e1.accept(this), e.e2.accept(this));
            } else {
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",newVariable(vn),e.e2.accept(this));
                    res += String.format("sub %s %s", newVariable(vn),e.e1.accept(this));
                    vn++;
            }
        } else {
            if(e.e2 instanceof Var){
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",newVariable(vn),e.e1.accept(this));
                    res += String.format("sub %s %s", newVariable(vn),e.e2.accept(this));
                    vn++;
            } else {
                    String v1=newVariable(vn);
                    vn++;
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), newVariable(vn),e.e1.accept(this));
                    res += String.format("sub %s %s",v1,newVariable(vn));
                    vn++;
            }
        }
        return res ;
    }

    @Override
    public String visit(FNeg e) {
        String retour ="";
    	if(e.e instanceof Var){
        	retour += String.format("fneg %s",e.e.accept(this));
    	} else {
    		String v1 = newVariable(vn++);
    		GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s ",v1,e.e.accept(this));
        	GenerateurASML.entryPoint += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s +0) in",v1,v1,newVariable(vn),v1);
    		retour += String.format("fneg %s",newVariable(vn));
                vn++;
    	}
    	return retour ;
    }

    @Override
    public String visit(FAdd e) {
        String res ="";
        if(e.e1 instanceof Var){
            if(e.e2 instanceof Var){
                res += String.format("fadd %s %s",e.e1.accept(this), e.e2.accept(this));
            } else {
                String v = newVariable(vn++);
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                res += String.format("fadd _%s %s", newVariable(vn),e.e1.accept(this));
                vn++;
            }
        } else {
            if(e.e2 instanceof Var){
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",newVariable(vn),e.e1.accept(this));
                res += String.format("fadd _%s %s", newVariable(vn),e.e2.accept(this));
                vn++;
            } else {
                String v1=newVariable(vn);
                vn++;
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVariable(vn),e.e1.accept(this));
                res += String.format("fadd _%s _%s",v1,newVariable(vn));
                vn++;
            }
        }
        return res ;
    }

    @Override
    public String visit(FSub e) {
        String res ="";
        if(e.e1 instanceof Var){
            if(e.e2 instanceof Var){
                res += String.format("fsub %s %s",e.e1.accept(this), e.e2.accept(this));
            } else {
                String v = newVariable(vn++);
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                res += String.format("fsub _%s %s", newVariable(vn),e.e1.accept(this));
                vn++;
            }
        } else {
            if(e.e2 instanceof Var){
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",newVariable(vn),e.e1.accept(this));
                res += String.format("fsub _%s %s", newVariable(vn),e.e2.accept(this));
                vn++;
            } else {
                String v1=newVariable(vn);
                vn++;
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVariable(vn),e.e1.accept(this));
                res += String.format("fsub _%s _%s",v1,newVariable(vn));
                vn++;
            }
        }
        return res ;
    }

    @Override
    public String visit(FMul e) {
        String res ="";
        if(e.e1 instanceof Var){
            if(e.e2 instanceof Var){
                res += String.format("fmul %s %s",e.e1.accept(this), e.e2.accept(this));
            } else {
                String v = newVariable(vn++);
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                res += String.format("fmul _%s %s", newVariable(vn),e.e1.accept(this));
                vn++;
            }
        } else {
            if(e.e2 instanceof Var){
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",newVariable(vn),e.e1.accept(this));
                res += String.format("fmul _%s %s", newVariable(vn),e.e2.accept(this));
                vn++;
            } else {
                String v1=newVariable(vn);
                vn++;
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVariable(vn),e.e1.accept(this));
                res += String.format("fmul _%s _%s",v1,newVariable(vn));
                vn++;
            }
        }
        return res ;
    }

    @Override
    public String visit(FDiv e) {
        String res ="";
        if(e.e1 instanceof Var){
            if(e.e2 instanceof Var){
                res += String.format("fdiv %s %s",e.e1.accept(this), e.e2.accept(this));
            } else {
                String v = newVariable(vn++);
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                res += String.format("fdiv _%s %s", newVariable(vn),e.e1.accept(this));
                vn++;
            }
        } else {
            if(e.e2 instanceof Var){
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",newVariable(vn),e.e1.accept(this));
                res += String.format("fdiv _%s %s", newVariable(vn),e.e2.accept(this));
                vn++;
            } else {
                String v1=newVariable(vn);
                vn++;
                GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVariable(vn),e.e1.accept(this));
                res += String.format("fdiv _%s _%s",v1,newVariable(vn));
                vn++;
            }
        }
        return res ;
    }

    @Override
    public String visit(Eq e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(LE e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(If e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Let e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Var e) {
        e.id.id=e.id.id.replace('?','_');
    	return e.id.id;
    }

    @Override
    public String visit(LetRec e) {
        GenerateurASML.autreDeclaration += String.format("\nlet _%s %s = \n\t",e.fd.id,printIds(e.fd.args, " "),printIds(e.fd.args, " "));
        String suite = e.fd.e.accept(this);
        GenerateurASML.autreDeclaration += String.format ("%s",suite);
	String res = e.e.accept(this);
        return res;
    }

    @Override
    public String visit(App e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Tuple e) {
        return String.format("%s",printIds(e.es, " "));
    }

    @Override
    public String visit(LetTuple e) {
        return String.format("let %s = 1 in \n\tlet %s = call _min_caml_create_array %s %s in %s",newVariable(vn),printIds(e.ids,""),newVariable(vn++), e.e1.accept(this),e.e2.accept(this));
    }

    @Override
    public String visit(Array e) {
        String res ="";
    	String v1 = "";
    	String v2 = "";
    	String v =newVariable(vn++);
    	if(e.e1 instanceof Int){
            v2 = newVariable(vn++);
            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in",v2,e.e1.accept(this));
    	} else if (e.e1 instanceof Var){
            v2 = e.e1.accept(this);
    	} 
    	if(e.e2 instanceof Int){
            v1 = newVariable(vn++);
            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in",v1,e.e2.accept(this));
            res += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
    	} else if (e.e2 instanceof Var){
            v1 = e.e2.accept(this);
            res += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
    	} else if (e.e2 instanceof Float){
            v1 = newVariable(vn++);
            String v3 = newVariable(vn++);
            GenerateurASML.entryPoint += String.format("\n\tlet %s = _%s in\n\tlet %s = mem(%s + 0) in",v1,v1,v3,v1);
            GenerateurASML.floatDeclaration += String.format("\nlet _%s = %s",v1,e.e2.accept(this));
            res += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
    	} 
    	return res;
    }

    @Override
    public String visit(Get e) {
        String v = newVariable(vn);
    	GenerateurASML.entryPoint += String.format("\n\tlet %s = mem(%s + %s) in",v, e.e1.accept(this), e.e2.accept(this));
    	return v;
    }

    @Override
    public String visit(Put e) {
        return String.format("mem(%s + %s) <- %s", e.e1.accept(this), e.e2.accept(this),e.e3.accept(this));
    }
    
    
    static <E> String printIds(List<E> l, String op) {
        if (l.isEmpty()) {
            return "";
        }
        String t = "" ;
        Iterator<E> it = l.iterator();
        t += it.next();
        while (it.hasNext()) {
            t += op + it.next();
        }
        return t ;
    }
}
