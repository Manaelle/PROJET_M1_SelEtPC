import java_cup.runtime.*;
import java.io.*;
import java.util.*;

public class Main {
    static public void main(String argv[]) { 
        boolean outputFile = false; 
        boolean help = false;
        boolean version = false;
        boolean type = false;
        boolean parse = false;
        boolean asml = false;
        boolean personnel = false;
        boolean knor = false; 
        boolean alphaC= false;
        String fichier = argv[argv.length-1] ;
        for(int i = 0 ; i<argv.length ; i++){
            switch(argv[i]){
             
                case "-o": 
                    outputFile= true;
                    break;
                case "-h":
                    help = true;
                    break; 
                case "-v":
                    version = true; 
                    break; 
                case "-t":
                    type = true;
                    break;
                case "-p":
                    parse = true;
                    break; 
                case "-asml":
                    asml = true;
                    break; 
                case "-kn": 
                    knor = true; 
                    break; 
                case "-ar":
                    alphaC = true; 
                    break; 
                case "-my-opt":
                    personnel = true ; 
                    break; 
                default :
                    break; 
                    
            }
            
        }
        
        try{
            Parser p = new Parser(new Lexer(new FileReader(fichier)));
            Exp expression = (Exp) p.parse().value;      
            assert (expression != null);
            if(outputFile==true){
                
            }
            if(help == true ){
                System.out.println("Utilisation : ./mincaml [Options] <file.ml>");
                System.out.println("-o <file>                     :output file");
                System.out.println("-h                            :afficher le help");
                System.out.println("-t                            :type Checking ");
                System.out.println("-v                            :la version ");
                System.out.println("-p                            :le parseur");
                System.out.println("-kn                           :la K-Normalisation");
                System.out.println("-ar                           :l'alpha conversion");
                
                 
            }
            if (version == true){
                System.out.println("version 0.2");
                
            }
            if(type == true ){
                System.out.println("------ Liste des Types ------");
                GenerateurDEquation ge = new GenerateurDEquation();
                ge.GenererEquations(new EnvironnementType(), expression, new TUnit());
                System.out.println(ge.toString());
                System.out.println("------ Resolution des types  ------");
                ge.resoudreEquation(ge.getListeEquation());
                System.out.println("bien typé est : " +ge.isBienTypee());
                
            }
            if(parse == true  ){
                System.out.println("------ AST ------");
                expression.accept(new PrintVisitor());
                System.out.println();
                System.out.println("------ Height of the AST ----");
                int height = Height.computeHeight(expression);
                System.out.println("using Height.computeHeight: " + height);
                ObjVisitor<Integer> v = new HeightVisitor();
                height = expression.accept(v);
                System.out.println("using HeightVisitor: " + height);
                
            }
            if(asml == true){
                
            }
            if(knor == true ){
                System.out.println("------ KNORM ------");
                Exp knorm = expression.accept(new KNormVisitor());
                knorm.accept(new PrintVisitor());
                System.out.println();
                Exp letred = expression.accept(new LetReduction());
                letred.accept(new PrintVisitor());
            }
            if (alphaC == true){
                
            }
            if(personnel == true ){
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
}

