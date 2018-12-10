Trees in Java
=============

The expression problem
----------------------

Compilers use tree structures in order to represent programs and 
manipulate them. There are different ways of defining such structures, 
usually dictated by the programming language. Actually the question of what 
is the best programming paradigm to address these questions
has been a debated topic among programming languages enthusiasts 
(see the [expression problem](https://en.wikipedia.org/wiki/Expression_problem)).

We won't dwelve too much into the benefits of each approach. Instead, we'll just
the different options if you choose to do the compiler in Java.

Let us consider simple arithmetic expressions defined by the following abstract
syntax:

    Exp ::= Exp + Exp
         |  Exp * Exp
         |  Num

The abstract syntax naturally defines a tree structure. OCaml is very well-suited
for this kind of data structures.

    type exp = Plus of exp * exp
             | Mult of exp * exp 
             | Num of int

We can easily write a recursive evaluation function that works by case on the
tree structure.

    let rec eval e =
      match e with
      | Plus (e1, e2) -> (eval e1) + (eval e2)
      | Mult (e1, e2) -> (eval e1) * (eval e2)
      | Num n -> n

Usually, in a compiler, many such functions will be needed. Typechecker, 
printers, transformers...

In object-oriented languages such as Java, it is not as straighfoward and 
several solutions are possible. We'll go through different solutions,
from simple to complex.

Trees
-----

Common to all these solutions is to define one concrete class for each case, and
one abstract class for each non-terminal of the grammar.

    abstract class Exp { }

    class Plus extends Exp {
        Exp e1;
        Exp e2;
    }

    class Mult extends Exp {
        Exp e1;
        Exp e2;
    }

    class Num extends Exp {
        Integer n;
    }

Adding evaluation methods
-------------------------

### Case analysis

We can mimic the OCaml function using the `instanceof` operator.

    static Integer eval(Exp exp) {
      if (exp instanceof Plus) {
          Plus e = (Plus) e;
          return eval(e.e1) + eval(e.e2);
      } else if (exp instanceof Mult) {
          Mult e = (Mult) e;
          return eval(e.e1) + eval(e.e2);
      } else if (exp instanceof Num) {
          Num e = (Num) e;
          return e.n;
      }

One weakness of this representation is the use of dynamic casts that can
cause runtime errors. Programming errors (such as missing a case or trying to cast
to the wrong instance won't be caught at compile time). In comparison, 
the OCaml version provides much better compile-time garantees. 
This representation is used in the first edition of [Modern Compiler Implementation in Java](https://www.cs.princeton.edu/~appel/modern/java/).

Interpreter pattern
-------------------

An other solution closer to the object-oriented philosophy is given by 
the *interpreter* pattern.

    abstract class Exp { 
      Integer evaluate();
    }

    class Plus extends Exp {
        Exp e1;
        Exp e2;

        Integer evaluate() {
          return e1.evaluate() + e2.evaluate();
        }
    }

    class Mult extends Exp {
        Exp e1;
        Exp e2;

        Integer evaluate() {
          return e1.evaluate() * e2.evaluate();
        }
    }

    class Num extends Exp {
        Integer n;

        Integer evaluate() {
          return n;
        }
    }

This doesn't suffer from the dynamic cast problem. Instead, it uses dynamic-
binding to choose the right version of the `evaluate()` method.

Still, this is not fully satisfactory. This solution lacks modularity 
as the evaluation function behavior is spread across multiple classes. 
Moreover `Exp` has to be recompiled whenever a new behavior is added. 

Visitor pattern
---------------

Our final solution is the *visitor* pattern.

    abstract class Exp {
        abstract Integer accept(Visitor v);
    }

    class Plus extends Exp {
        Exp e1;
        Exp e2;

        Integer accept(Visitor v) {
            return v.visit(this);
        }
    }

    class Mult extends Exp {
        Exp e1;
        Exp e2;

        Integer accept(Visitor v) {
            return v.visit(this);
        }
    }

    class Num extends Exp {
        Integer n;
        Integer accept(Visitor v) {
            return v.visit(this); 
        }
    }

Now an `Exp` has a general method `accept()` that lets a visitor walks the tree
structure of the expression. There is a visitor class for every functions
that operate on tree. Each visitor class implements the `Visitor` interface.

    interface Visitor {
      Integer visit(Num e);
      Integer visit(Plus e);
      Integer visit(Mult e);
    }

We can define an `EvaluationVisitor`.

    class EvaluationVisitor implements Visitor {
      Integer visit(Num e) { 
          return e.n;
      }

      Integer visit(Plus e) {
          return e.accept(this) + e.accept(this);
      }

      Integer visit(Mult e) {
          return e.accept(this) * e.accept(this);
      }
    }

This almost solves the problems of the interpreter patterns. Now, 
new behaviors can be added without modifying the `Exp` class, plus 
all the behavior appears in one place, in the visitor class. 

There is one last issue. Notice the constraint on the return type of the 
visitor. We are still restricted to integer-returning visitors. 
But suppose we want visitors that return something else, like expressions.
There are several ways around this, but an elegant one is to use genericity.

    interface Visitor<E> {
      E visit(Num e);
      E visit(Plus e);
      E visit(Mult e);
    }

    class EvaluationVisitor implements Visitor<Integer> {
      Integer visit(Num e) { 
          return e.n;
      }

      Integer visit(Plus e) {
          return e.accept(this) + e.accept(this);
      }

      Integer visit(Mult e) {
          return e.accept(this) * e.accept(this);
      }
    }

    abstract class Exp {
        abstract <E> E accept(Visitor<E> v);
    }

    class Plus extends Exp {
        Exp e1;
        Exp e2;

        <E> E accept(Visitor<E> v) {
            return v.visit(this);
        }
    }

    class Mult extends Exp {
        Exp e1;
        Exp e2;

        <E> E accept(Visitor<E> v) {
            return v.visit(this);
        }
    }

    class Num extends Exp {
        Integer n;
        <E> E accept(Visitor<E> v) {
            return v.visit(this); 
        }
    }

Java code
---------

The java code given to you illustrates some of these patterns. This is the code
of the `main` function in `Main.java`:

      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;      
      assert (expression != null);

      System.out.println("------ AST ------");
      expression.accept(new PrintVisitor());
      System.out.println();

      System.out.println("------ Height of the AST ----");
      int height = Height.computeHeight(expression);
      System.out.println("using Height.computeHeight: " + height);

      ObjVisitor<Integer> v = new HeightVisitor();
      height = expression.accept(v);
      System.out.println("using HeightVisitor: " + height);

It uses three of the above-mentioned patterns. After parsing the input file, the
AST `expression` is printed using a `PrintVisitor` (which implements the simple
non-generic `Visitor` interface, whose all `visit` methods return `void`). Then,
the height of this AST is computed using a static method `computeHeight`.
Finally, the height is computed again, but this time using a visitor
`HeightVisitor` (which implements generic interface `ObjVisitor<Integer>`).

You're free to use whatever method you like best but the generic visitor is
arguably the best choice. However, the other patterns are valid too if you find
them easier to understand.
