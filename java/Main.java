import java_cup.runtime.*;
import java.io.*;
import java.util.*;

public class Main {
    
  static public void main(String argv[]) {    
    try {
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;      
      assert (expression != null);

      System.out.println("------ AST ------");
      expression.accept(new PrintVisitor());
      System.out.println();

      System.out.println("------ Liste des Types ------");
      GenerateurDEquation ge = new GenerateurDEquation();
      ge.GenererEquations(new EnvironnementType(), expression, new TUnit());
      System.out.println(ge.toString());
      System.out.println();
      System.out.println();
      System.out.println("------ Resolution des types  ------");
      System.out.println(ge.toString());
      ge.resoudreEquation(ge.getListeEquation());
      System.out.println("bien typé est : " +ge.isBienTypee());
      System.out.println();
      System.out.println();
       
      System.out.println("------ AST ------");
      expression.accept(new PrintVisitor());
      System.out.println();
      
      System.out.println("------ KNORM ------");
      Exp knorm = expression.accept(new KNormVisitor());
      knorm.accept(new PrintVisitor());
      System.out.println();

      System.out.println("------ Height of the AST ----");
      int height = Height.computeHeight(expression);
      System.out.println("using Height.computeHeight: " + height);

      ObjVisitor<Integer> v = new HeightVisitor();
      height = expression.accept(v);
      System.out.println("using HeightVisitor: " + height);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

