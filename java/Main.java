import java_cup.runtime.*;
import java.io.*;
import java.util.*;

public class Main {
    
    static public void main(String argv[]) { 
        
        String fichier = new String();
        
        //Nom du fichier .ml à compiler
        if(argv.length == 2){
            fichier = argv[1] ;
        }
        //cas sans options
        else if(argv.length == 1){
            fichier = argv[0] ;
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
                
                //A-CONVERSION (option -ar)
                if( argv[0].equals("-ar")  ){
                    System.out.println("------------- A-CONV --------------");
                    Exp alphaC = expression.accept(new AlphaConversionVisitor());
                    alphaC.accept(new PrintVisitor());
                    System.out.println();
                }
                 //LetReduction (option -lr)
                if( argv[0].equals("-lr")  ){
                    Exp knorm = expression.accept(new KNormVisitor());
                    Exp letred = knorm.accept(new LetReduction());
                    letred.accept(new PrintVisitor());
                    System.out.println();
                   
                }
                
                //B-REDUCTION (option -br)
                if( argv[0].equals("-br")  ){
                    //A COMPLETER
                }
                
                //CREATION DU .ASML (AVEC LET-REDUCTION ET CLOSURE) (option -asml)
                if( argv[0].equals("-asml")  ){
                    //A COMPLETER
                }
                
                //CREATION DE l'ASSEMBLEUR (option -o)
                if( argv[0].equals("-o")  ){
                    //A COMPLETER
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
                    
                    //BACKEND : A COMPLETER 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
      }
}

