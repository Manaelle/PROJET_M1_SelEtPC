import java_cup.runtime.*;
import java.io.*;
import java.util.*;

public class Main {
    
    static public void main(String argv[]) { 
        
        String fichier = new String();
        String nomFichier = null;
        String[] pathTab;
        String[] nameTab;
        String nomFichierSansMl = null;
        
        //Nom du fichier .ml à compiler
        if(argv.length == 2){
            fichier = argv[1];
            pathTab = argv[1].split("/");
            nomFichier = pathTab[pathTab.length-1];
            nameTab = nomFichier.split(".ml");
            nomFichierSansMl = nameTab[0];
        }
        //cas sans options
        else if(argv.length == 1){
            fichier = argv[0] ;
            pathTab = argv[0].split("/");
            nomFichier = pathTab[pathTab.length-1];
            nameTab = nomFichier.split(".ml");
            nomFichierSansMl = nameTab[0];
        }
  
        try{
            //PARSING
            Parser p = new Parser(new Lexer(new FileReader(fichier)));
            Exp expression = (Exp) p.parse().value;      
            assert (expression != null);
            
            //CAS AVEC OPTION
            if(argv.length == 2){
                
                //TYPAGE (option -t)
                if( argv[0].equals("-t") ){
                    System.out.println("------------ Liste des Types ----------");
                    GenerateurDEquation ge = new GenerateurDEquation();
                    ge.GenererEquations(new EnvironnementType(), expression, new TUnit());
                    System.out.println(ge.toString());
                    System.out.println("-------- Resolution des types  --------");
                    ge.resoudreEquation(ge.getListeEquation());
                    if(ge.isBienTypee()){
                        System.out.println("Equation bien typé !");
                    }
                    else{
                        System.err.println("Erreur de typage !");
                    }
                    
                
                }
            
                //PARSING (option -p)
                if( argv[0].equals("-p")  ){
                    System.out.println("--------------- AST ---------------");
                    expression.accept(new PrintVisitor());
                    System.out.println();
                    System.out.println("-------- Height of the AST --------");
                    int height = Height.computeHeight(expression);
                    System.out.println("using Height.computeHeight: " + height);
                    ObjVisitor<Integer> v = new HeightVisitor();
                    height = expression.accept(v);
                    System.out.println("using HeightVisitor: " + height); 
                }
                
                 //K-NORMALISATION (option -kn)
                if( argv[0].equals("-kn")  ){
                    System.out.println("------------- K-NORM --------------");
                    Exp knorm = expression.accept(new KNormVisitor());
                    knorm.accept(new PrintVisitor());
                    System.out.println();
                }
                
                //A-CONVERSION (option -ac)
                if( argv[0].equals("-ac")  ){
                    System.out.println("------------- A-CONV --------------");
                    Exp alphaC = expression.accept(new AlphaConversionVisitor());
                    alphaC.accept(new PrintVisitor());
                    System.out.println();
                }
                
                //B-REDUCTION (option -br)
                if( argv[0].equals("-br")  ){
                    //A COMPLETER
                }
                
                //LetReduction (option -lr)
                if( argv[0].equals("-lr")  ){
                    System.out.println("------------- LET-REDUCTION --------------");
                    Exp letred = expression.accept(new LetReduction());
                    letred.accept(new PrintVisitor());
                    System.out.println();   
                }
                
                //CREATION DU .ASML (AVEC LET-REDUCTION ET CLOSURE) (option -asml)
                if( argv[0].equals("-asml")){
                    System.out.println("------------- GEN. ASML --------------");
                    String asml =  expression.accept(new GenerateurASML());
                    asml = GenerateurASML.declarationFloat + GenerateurASML.declaration + GenerateurASML.entryPoint + asml ;
                    String nomFichierAvecAsml = nomFichierSansMl + ".asml";
                    System.out.println(asml);
                    PrintWriter w = new PrintWriter( new BufferedWriter( new FileWriter(nomFichierAvecAsml)));
                    w.print(asml);
                    w.close();
					//CREATION DE l'ASSEMBLEUR (option -o)
					if( argv[0].equals("-o")  ){
						
						ASMLArbre arbreASML = new ASMLArbre(asml);
						arbreASML.registerAllocation_Spill();
						String arm = arbreASML.genererAssembleur();
						String nomFichierAvecArm = nomFichierSansMl + ".s";
						w = new PrintWriter(new BufferedWriter(new FileWriter(nomFichierAvecAsml)));
						w.print(arm);
						w.close();
					}
                }
                

                
            }
            
            //SI AUCUNE OPTION, TOUT FAIRE
            else if(argv.length == 1){
                    System.out.println("--------------- AST ---------------");
                    expression.accept(new PrintVisitor());
                    System.out.println();
                    System.out.println("-------- Height of the AST --------");
                    int height = Height.computeHeight(expression);
                    System.out.println("using Height.computeHeight: " + height);
                    ObjVisitor<Integer> v = new HeightVisitor();
                    height = expression.accept(v);
                    System.out.println("using HeightVisitor: " + height);
                    
                    System.out.println("------------ Liste des Types ----------");
                    GenerateurDEquation ge = new GenerateurDEquation();
                    ge.GenererEquations(new EnvironnementType(), expression, new TUnit());
                    System.out.println(ge.toString());
                    System.out.println("-------- Resolution des types  --------");
                    ge.resoudreEquation(ge.getListeEquation());
                    if(ge.isBienTypee()){
                        System.out.println("Equation bien typé !");
                    }
                    else{
                        System.err.println("Erreur de typage !");
                    }
                    
                    System.out.println("------------- K-NORM --------------");
                    Exp knorm = expression.accept(new KNormVisitor());
                    knorm.accept(new PrintVisitor());
                    System.out.println();
                    
                    //A-CONVERSION : 
                    System.out.println("------------- A-CONV --------------");
                    Exp alphaC = expression.accept(new AlphaConversionVisitor());
                    alphaC.accept(new PrintVisitor());
                    System.out.println();
                    
                    //B-REDUCTION : A COMPLETER
                    
                    //LET-REDUCTION
                    System.out.println("------------- LET-REDUCTION --------------");
                    Exp lrec = expression.accept(new KNormVisitor());
                    Exp letred = lrec.accept(new LetReduction());
                    letred.accept(new PrintVisitor());
                    System.out.println();   

                    //ASML :
                    System.out.println("------------- GEN. ASML --------------");
                    String asml =  expression.accept(new GenerateurASML());
                    asml = GenerateurASML.declarationFloat + GenerateurASML.declaration + GenerateurASML.entryPoint + asml ;
                    String nomFichierAvecAsml = nomFichierSansMl + ".asml";
                    System.out.println(asml);
                    PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(nomFichierAvecAsml)));
                    w.print(asml);
                    w.close();
                    
                    //BACKEND : A COMPLETER 
					ASMLArbre arbreASML = new ASMLArbre(asml);
					arbreASML.registerAllocation_Spill();
					String arm = arbreASML.genererAssembleur();
					String nomFichierAvecArm = nomFichierSansMl + ".s";
					w = new PrintWriter(new BufferedWriter(new FileWriter(nomFichierAvecAsml)));
					w.print(arm);
					w.close();
			}

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
      }
}

