import java.util.*;

public class GenerateurASML implements ObjVisitor<String> {
	
        private static boolean inApp = false ;
	private static boolean opBinaire = false ;
	private static boolean not = false ; 
	private static boolean iff = false ;
	
        static String asml;
	static String declaration ="";
	static String declarationFloat ="";
	static String entryPoint ="\nlet _ = \n\t";
        
        static int cp ;
	private static int vn = 0 ;
        
	public GenerateurASML()
	{
		cp = 0 ;
	}
	
	public String variable(int c){
            return String.format("v%s", c);
	}
        
        public String visit(Unit e) {
                    return e.toString();
        }

        public String visit(Bool e) {
            if(e.b == false) {
                    return String.format("%s",0);
            }
            return String.format("%s",1);
        }

        public String visit(Int e) {
            return String.format("%s",e.i);
        }

        public String visit(Float e) {
            String s = String.format("%.2f", e.f);
            s=s.replace(',','.');
            return s;
        }

        public String visit(Not e) {
            return String.format("%s",e.e.accept(this));
        }

        public String visit(Neg e) {
            String retour ="";
            if(e.e instanceof Var){
                retour += String.format("neg %s",e.e.accept(this));
            } else {
                GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",variable(vn),e.e.accept(this));
                retour += String.format("neg %s",variable(vn));
                vn++;
            }
            return retour ;
        }
        
        public String visit(FNeg e) {
            String res ="";
            if(e.e instanceof Var){
                res += String.format("fneg %s",e.e.accept(this));
            } else {
                String v1 = variable(vn++);
                GenerateurASML.declarationFloat += String.format("\nlet _%s = %s ",v1,e.e.accept(this));
                GenerateurASML.entryPoint += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s +0) in",v1,v1,variable(vn),v1);
                res += String.format("fneg %s",variable(vn));
                vn++;
            }
            return res ;
        }

        public String visit(Add e) {
            String retour ="";
            if(e.e1 instanceof Var){
                    if(e.e2 instanceof Var){
                            retour += String.format("add %s %s",e.e1.accept(this), e.e2.accept(this));
                    } else {
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",variable(vn),e.e2.accept(this));
                            retour += String.format("add %s %s ", variable(vn),e.e1.accept(this));
                            vn++;
                    }
            } else {
                    if(e.e2 instanceof Var){
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",variable(vn),e.e1.accept(this));
                            retour += String.format("add %s %s ", variable(vn),e.e2.accept(this));
                            vn++;
                    } else {
                            String v1=variable(vn);
                            vn++;
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), variable(vn),e.e1.accept(this));
                            retour += String.format("add %s %s ",v1,variable(vn));
                            vn++;
                    }
            }
            return retour ;
        }

        public String visit(Sub e) {
            String retour ="";
            if(e.e1 instanceof Var){
                    if(e.e2 instanceof Var){
                            retour += String.format("sub %s %s",e.e1.accept(this), e.e2.accept(this));
                    } else {
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",variable(vn),e.e2.accept(this));
                            retour += String.format("sub %s %s", variable(vn),e.e1.accept(this));
                            vn++;
                    }
            } else {
                    if(e.e2 instanceof Var){
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",variable(vn),e.e1.accept(this));
                            retour += String.format("sub %s %s", variable(vn),e.e2.accept(this));
                            vn++;
                    } else {
                            String v1=variable(vn);
                            vn++;
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), variable(vn),e.e1.accept(this));
                            retour += String.format("sub %s %s",v1,variable(vn));
                            vn++;
                    }
            }
            return retour ;
        }


        public String visit(FAdd e){
            String retour ="";
            if(e.e1 instanceof Var){
                    if(e.e2 instanceof Var){
                            retour += String.format("fadd %s %s",e.e1.accept(this), e.e2.accept(this));
                    } else {
                            String v = variable(vn++);
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                            retour += String.format("fadd _%s %s", variable(vn),e.e1.accept(this));
                            vn++;
                    }
            } else {
                    if(e.e2 instanceof Var){
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",variable(vn),e.e1.accept(this));
                            retour += String.format("fadd _%s %s", variable(vn),e.e2.accept(this));
                            vn++;
                    } else {
                            String v1=variable(vn);
                            vn++;
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), variable(vn),e.e1.accept(this));
                            retour += String.format("fadd _%s _%s",v1,variable(vn));
                            vn++;
                    }
            }
            return retour ;          
        }

        public String visit(FSub e){
            String res ="";
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fsub %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",variable(vn),e.e2.accept(this));
                    res += String.format("fsub _%s %s", variable(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",variable(vn),e.e1.accept(this));
                    res += String.format("fsub _%s %s", variable(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1=variable(vn);
                    vn++;
                    GenerateurASML.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), variable(vn),e.e1.accept(this));
                    res += String.format("fsub _%s _%s",v1,variable(vn));
                    vn++;
            }
            }
            return res ;      
        }

        public String visit(FMul e) {
            String retour ="";
            if(e.e1 instanceof Var){
                    if(e.e2 instanceof Var){
                            retour += String.format("fmul %s %s",e.e1.accept(this), e.e2.accept(this));
                    } else {
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",variable(vn),e.e2.accept(this));
                            retour += String.format("fmul _%s %s", variable(vn),e.e1.accept(this));
                            vn++;
                    }
            } else {
                    if(e.e2 instanceof Var){
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",variable(vn),e.e1.accept(this));
                            retour += String.format("fmul _%s %s", variable(vn),e.e2.accept(this));
                            vn++;
                    } else {
                            String v1=variable(vn);
                            vn++;
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), variable(vn),e.e1.accept(this));
                            retour += String.format("fmul _%s _%s",v1,variable(vn));
                            vn++;
                    }
            }
            return retour ;
        }


        public String visit(FDiv e){
            String retour ="";
            if(e.e1 instanceof Var){
                    if(e.e2 instanceof Var){
                            retour += String.format("fdiv %s %s",e.e1.accept(this), e.e2.accept(this));
                    } else {
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",variable(vn),e.e2.accept(this));
                            retour += String.format("fdiv _%s %s", variable(vn),e.e1.accept(this));
                            vn++;
                    }
            } else {
                    if(e.e2 instanceof Var){
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",variable(vn),e.e1.accept(this));
                            retour += String.format("fdiv _%s %s", variable(vn),e.e2.accept(this));
                            vn++;
                    } else {
                            String v1=variable(vn);
                            vn++;
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), variable(vn),e.e1.accept(this));
                            retour += String.format("fdiv _%s _%s",v1,variable(vn));
                            vn++;
                    }
            }
            return retour ;
        }

        public String visit(Eq e){ 
            String res ="";
            String v1 ="";
            String v2="";
                 
            if(e.e1 instanceof Float){
            GenerateurASML.declaration = String.format("\nlet _z%s = %s \nlet _z%s = %s",cp++,e.e1.accept(this),cp++,e.e2.accept(this));
            res += String.format("_z%s = _z%s", cp--,cp++);
            } else if(e.e1 instanceof Bool || e.e1 instanceof Int) {
                if(e.e2 instanceof Int || e.e2 instanceof Bool) {
                    String v = variable(vn) ;
                    vn++;
                    v1 = variable(vn++) ;
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in \n\t let %s = %s in ",v,e.e1.accept(this),v1,e.e2.accept(this));
                    if(not){
                        res += String.format("%s != %s", v,v1);
                        not = false ;
                    } else {
                        res += String.format("%s = %s", v,v1);
                    }
                    vn++;
                    } else {
                        String v = variable(vn) ;
                        vn++;
                        GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",v,e.e1.accept(this));
                        if(not){
                            res += String.format("%s != %s", v,e.e2.accept(this));
                            not = false ;
                        } else {
                            res += String.format("%s = %s", v,e.e2.accept(this));
                        }
                    }	
            } else if(e.e1 instanceof App || e.e2 instanceof App) {
                    if(e.e1 instanceof App) {
                        String txt = e.e1.accept(this);
                        v1 = variable(vn++);
                        GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",v1,txt);
                    }else {
                        v1 = e.e1.accept(this);
            } if(e.e2 instanceof App){
                String txt = e.e2.accept(this);
                v2 = variable(vn++);
                GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",v2,txt);
            }else {
                v2 = e.e2.accept(this);
                    }
            if(not){
                res += String.format("%s >= %s", v1,v2);
                not = false ;
                } else {
                res += String.format("%s <= %s", v1,v2);
                }
            } else {
                if(not){
                    res += String.format("%s != %s", e.e1.accept(this),e.e2.accept(this));
                not = false ;
                } else {
                    res += String.format("%s = %s", e.e1.accept(this),e.e2.accept(this));
                }
            }
            return res ;
            }

        public String visit(LE e){
            String retour ="";
            String v1="";
            String v2="";
                    
            if(e.e1 instanceof Float){
                GenerateurASML.declaration = String.format("\nlet _z%s = %s \nlet _z%s = %s",cp++,e.e1.accept(this),cp++,e.e2.accept(this));
                retour += String.format("_z%s <= _z%s", cp--,cp++);
            } else if(e.e1 instanceof Bool || e.e1 instanceof Int) {
                if(e.e2 instanceof Int || e.e2 instanceof Bool) {
                    String v = variable(vn) ;
                    vn++;
                     v1 = variable(vn++) ;
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v,e.e1.accept(this),v1,e.e2.accept(this));
                    if(not){
                        retour += String.format("%s >= %s", v,v1);
                        not = false ;
                    } else {
                        retour += String.format("%s <= %s", v,v1);
                    }
                    vn++;
                } else {
                    String v = variable(vn) ;
                    vn++;
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in",v,e.e1.accept(this));
                    if(not){
                        retour += String.format("%s >= %s", v,e.e2.accept(this));
                        not = false ;
                    } else {
                        retour += String.format("%s <= %s", v,e.e2.accept(this));
                    }
            }

            } else if(e.e1 instanceof App || e.e2 instanceof App) {
                if(e.e1 instanceof App) {
                    String txt = e.e1.accept(this);
                    v1 = variable(vn++);
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",v1,txt);
                }else {
                    v1 = e.e1.accept(this);
                } 
                
                if(e.e2 instanceof App){
                    String txt = e.e2.accept(this);
                    v2 = variable(vn++);
                    GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",v2,txt);
                }else {
                    v2 = e.e2.accept(this);
                }
                if(not){
                    retour += String.format("%s >= %s", v1,v2);
                    not = false ;
                } else {
                    retour += String.format("%s <= %s", v1,v2);
                    }
            } else if(not){
                retour += String.format("%s >= %s", e.e1.accept(this),e.e2.accept(this));
                not = false ;
            } 
            
            else {
                retour += String.format("%s <= %s", e.e1.accept(this),e.e2.accept(this));
            }
            return retour;
      }

        public String visit(If e){
            String retour ="";
            String haut ="";
            if(e.e1 instanceof Not){
                            not = true;
            }
            if (e.e2 instanceof Float){
                GenerateurASML.declaration += String.format("let _z%s = %s \nlet _z%s = %s",cp++,e.e2.accept(this),cp,e.e3.accept(this));
            if(e.e1 instanceof Bool){
                GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in",variable(vn),e.e1.accept(this));
                retour += String.format("\n\tif %s = %s ",variable(vn),variable(vn++));
            } else {
                retour += String.format("\n\tif %s ",e.e1.accept(this));
            }
            cp--;
            if(iff){
                retour += String.format("then ( \n\t\tlet %s = _z%s in %s\n\t)else(\n\t\tlet %s = _z%s in %s\n\t)",asml,cp++,asml,asml,cp++,asml);

            } else {
                retour += String.format("then ( \n\t\t_z%s \n\t)else(\n\t\t _z%s \n\t)",cp++,cp++);
            }
                return retour;
            } else {
                if(e.e1 instanceof Bool){
                    if(((Bool)e.e1).b == true) {
                        GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in ",variable(vn),e.e1.accept(this));
                        retour += String.format("\n\tif %s = %s ",variable(vn),variable(vn++));
                        } else {
                            String v = variable(vn++);
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = 1",v,e.e1.accept(this),variable(vn));
                            retour += String.format("\n\tif %s = %s ",v,variable(vn++));
                        }
                } else {
                    retour += String.format("\n\tif %s ",e.e1.accept(this));
                }
                if(iff){
                    retour += String.format("then ( \n\t\tlet %s = %s in %s\n\t)else(\n\t\tlet %s = %s in %s\n\t)",asml,e.e2.accept(this),asml,asml,e.e3.accept(this),asml);
                } else {
                    retour += String.format("then ( \n\t\t%s \n\t)else(\n\t\t %s \n\t)",e.e2.accept(this),e.e3.accept(this));
                }
            }
            return retour ;
            
            }

        public String visit(Let e) {
            String haut ="";
            String retour ="";
                if((e.e1) instanceof Float)
                {
                    GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",e.id,e.e1.accept(this));
                    retour += String.format("let %s = _%s in \n\tlet %s = mem(%s +0) in ", e.id,e.id,variable(vn),e.id);

                if(!(e.e2 instanceof App)){
                        retour += String.format("%s \n\t ",e.e2.accept(this));
                } else {
                        retour += String.format("\n\t %s",e.e2.accept(this));
                    }
                } else if (e.e1 instanceof If) {
                    iff = true ; 
                    asml = e.id.id ; 
                    retour += String.format("%s in ",e.e1.accept(this));
                    retour += String.format("\n\t %s",e.e2.accept(this));
                    iff = false ;
                }
                    else {
                    retour += String.format("\n\tlet %s = ",e.id);
                    retour += String.format("%s in %s ",e.e1.accept(this),e.e2.accept(this));
                } 
                    return retour;
        }


        public String visit(Var e){
            e.id.id=e.id.id.replace('?','_');
            return e.id.id;
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

        // print sequence of Exp
        public String printExps(List<Exp> l, String op,String id) {
            String t ="";
            String txt="";
            for(Exp e : l){
                if(e instanceof Var){
                t += e + " ";  
                } else {
                    if (e instanceof App) {
                        if(inApp) {
                            inApp = false ;
                            txt = e.accept(this);
                            inApp = true ;
                        } else {
                            txt = e.accept(this);
                        }
                        if(inApp){
                            txt += String.format("in call _%s %s",id,t) ;
                            t = String.format("\n\tlet %s = %s",variable(vn),txt);
                        } else {
                            t += String.format("\n\tlet %s = %s",variable(vn),txt);
                        }
                    } else {
                        GenerateurASML.declaration += String.format("\n\tlet %s = %s in ",variable(vn),e.accept(this));
                        }
                    t += variable(vn) + " ";
                    vn++;
                }                    
            }
            return t ;
        }

        public String visit(LetRec e){
            GenerateurASML.declaration += String.format("\nlet _%s %s = \n\t",e.fd.id,printIds(e.fd.args, " "),printIds(e.fd.args, " "));
            String a = e.fd.e.accept(this);
            GenerateurASML.declaration += String.format ("%s",a);
            String retour = e.e.accept(this);
            return retour;
        }

        public String visit(App e){
            String retour ="";
                    if(e.e instanceof Var && (((Var)e.e).id.id.equals("print_int") || ((Var)e.e).id.id.equals("print_float") || ((Var)e.e).id.id.equals("truncate") || ((Var)e.e).id.id.equals("print_newline") || ((Var)e.e).id.id.equals("int_of_float") || ((Var)e.e).id.id.equals("float_of_int"))){
                            for(Exp r : e.es){
                            inApp = true ;
                            retour += printExps(e.es, " ",e.e.accept(this));

                    } 
                            if(((Var)e.e).id.id.equals("print_float") ){
                                    String v = variable(vn++) ;
                            retour += String.format("\n\tlet %s = call _min_caml_int_of_float %s in ",v,printExps(e.es, " ",e.e.accept(this)),variable(vn),v);
                            retour += String.format("call _min_caml_print_int %s ",v);
                            } else if(!inApp){
                            String txt = printExps(e.es, " ",e.e.accept(this));
                            if(opBinaire){
                                    retour += txt ;
                            } else
                            retour += String.format("call _min_caml_%s %s ",e.e.accept(this),txt);
                            }
                    } else {
                            for(Exp r : e.es){
                            if ((!(r instanceof Var) && !(r instanceof Int) )){
                                    inApp = true ;
                                    retour += printExps(e.es, " ",e.e.accept(this));
                                    inApp = true ;
                            }
                            }
                            if(!inApp) {
                                    String txt = e.e.accept(this);
                                    String str2 = "call";
                                    if(txt.toLowerCase().contains(str2.toLowerCase())){
                                            GenerateurASML.entryPoint += String.format("let %s = %s in",variable(vn),txt);
                                    retour += String.format("call _%s %s ",variable(vn++),printExps(e.es, " ",e.e.accept(this)));
                                    } else
                                    retour += String.format("call _%s %s ",txt,printExps(e.es, " ",e.e.accept(this)));

                            }
                            inApp = false;
                    }
                    return retour ;
        }

        public String visit(Tuple e){
            return String.format("%s",printIds(e.es, " "));
        }

        public String visit(LetTuple e){
            return String.format("let %s = 1 in \n\tlet %s = call _min_caml_create_array %s %s in %s",variable(vn),printIds(e.ids,""),variable(vn++), e.e1.accept(this),e.e2.accept(this));
        }

        public String visit(Array e){
            String retour ="";
            String v1 = "";
            String v2 = "";
            String v =variable(vn++);
            if(e.e1 instanceof Int){
                            v2 = variable(vn++);
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in",v2,e.e1.accept(this));
            } else if (e.e1 instanceof Var){
                            v2 = e.e1.accept(this);
            } 
            if(e.e2 instanceof Int){
                            v1 = variable(vn++);
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = %s in",v1,e.e2.accept(this));
                    retour += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
            } else if (e.e2 instanceof Var){
                            v1 = e.e2.accept(this);
                    retour += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
            } else if (e.e2 instanceof Float){
                            v1 = variable(vn++);
                            String v3 = variable(vn++);
                            GenerateurASML.entryPoint += String.format("\n\tlet %s = _%s in\n\tlet %s = mem(%s + 0) in",v1,v1,v3,v1);
                            GenerateurASML.declarationFloat += String.format("\nlet _%s = %s",v1,e.e2.accept(this));
                    retour += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
            } 
            return retour;
        }

        public String visit(Get e){
            String v = variable(vn);
            GenerateurASML.entryPoint += String.format("\n\tlet %s = mem(%s + %s) in",v, e.e1.accept(this), e.e2.accept(this));
            return v;
            }

        public String visit(Put e){
            return String.format("mem(%s + %s) <- %s", e.e1.accept(this), e.e2.accept(this),e.e3.accept(this));
        }

    
}
