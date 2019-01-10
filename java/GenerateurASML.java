
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Sub e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(FSub e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(FMul e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(FDiv e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Array e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Get e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Put e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
