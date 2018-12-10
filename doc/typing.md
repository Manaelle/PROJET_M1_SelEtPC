Typing
======

OCaml types
-----------

OCaml (and MinCaml) is statically typed. Every identifier has a type that is 
inferred (i.e. guessed) by the compiler. See for instance the following 
function:

    let f x = x + 1

The compiler can infer that `x` has type `int` and `f` has type
`int -> int`.

Altneratively, types can be provided by the programmer.

    let f (x : int) : int = x 

The compiler checks the type before trying to generate code.  This allows to 
rule out incorrect programs such as

    let x = 1 + "hello" in ...

*Exercice*: simple types

What are the types of the following expressions?

    1
    1.0 
    1 + 2
    let x = 1 in 1
    let x = 1 in 1 + x
    if true then 1 else 2 
    let f x = x + 1 in (f 0)
    let f x = x + 1 in f
    let f x y z = x + y + z in f 
    let f (x,y,z) = x + y + z in f

### Polymorphism

Some functions can accept and return values of different types

    let f x = x in f 1

    let f x = 0 in (f 1.0) + (f "hello");

    let f x = x in f

*Exercice*: Is the expression `let f g = g 1 + g "1"` well-typed, and if yes,
what is its type?

### Unit type

The type `unit` has only one inhabitant: `()` (called nil). Nil can be used as 
a return type for procedures.  For instance, `print_int` has type 
`int -> unit`: It always returns nil when applied to a string. Hence, 
`print_int 42` has type `unit` and is equal to nil.

When a value is equal to nil, there's no need to store it in a variable.

    let x = print_string "hello" in ...

There's no reasonable use for `x` since it is necessarily equal to nil. In that
case, one can write 

    let _ = print_string "hello" in ... 

or even better, use the `;` operator as in:

    print_string "hello"; ....

Conversely, the `;` shouldn't be preceded by a non-nil value.  The compiler
will issue a warning on the following program.

    let x = 0; 1 in ...

*Exercice*: there's a predefined function `ignore` that we can use to tame
the compiler.

    let x = (ignore 0); 1 in ...

What is the type of `ignore`?

MinCaml type system
-------------------

What we presented is a very small subset of the OCaml type system, which is called
the [Hindly-Milner type system](https://en.wikipedia.org/wiki/Hindley–Milner_type_system).
MinCaml implements the same features with a few simplifications.

An important difference is that unlike OCaml, MinCaml is *monomorphic*.

These two expressions are well-typed

    let f x = 0 in (f 1.0)
    let f x = 0 in (f true) 

But this one is not.

    let f x = 0 in (f 1.0) + (f true) 

Another minor difference is that MinCaml programs must have unit type.

For instance:

    let rec fact x = if x = 0 then 1 else x * (fact (x-1)) 
    in print_int (fact 3)

is well-typed with type unit. Whereas 

    let rec fact x = if x = 0 then 1 else x * (fact (x-1)) 
    in (print_int 0); fact 3

has type `int`, and is not a correct MinCaml program.

On a side note, this program is valid in OCaml but not syntactically correct in
MinCaml

    let rec fact x = if x = 0 then 1 else x * (fact (x-1)) 

    let _ = print_int (fact 3)

Finally, MinCaml programs never contain type annotation, unlike in OCaml
where they are optional.

### MinCaml types

MinCaml types are simple structured expressions defined by the following
(incomplete)  syntax.

    t ::=  bool
       |   int
       |   unit
       |   t1 -> t2       (* function type *)
       |   variable       (* type variables *)
       | ... 

This means you'll have to define a new datatype for types (complete 
`Type.{c,java,ml}`). (For those working in Java, it's a tree datatype, so it 
should be defined along the same lines as Exp.)

### Type checking

The *type checker* is the part of the compiler that takes an AST, an 
environment (that contains predefined symbols and their types, for instance 
external functions like `print_int`) and determines if the program is 
well-typed or not. If it is not, it returns with an error message and the 
compiler doesn't attempt to generate code. Sometimes, the type checker 
annotates the program with information that are useful for code generation, but 
it is not the case in MinCaml. Actually, the type checker is *optional* and you 
can try to generate code independently.

Type checking is implemented in *three stages*. The first step is to generate 
type variables. Initially, a MinCaml program doesn't contain type information.

    let x = 3 in
    let y = 4 in
    in x + y

The parser generates type variables. In other words, after parsing, every 
variable in the AST is associated a unique object of class `Type` that represents
a unique variable (there's a placeholder in the AST to store this information).

    let (x : 'a) = 3 in
    let (y : 'b) = 4 in
    in x + y 

The second step is to generate type equations. We'll see later how to do it, 
but suppose we obtain these three equations:

    'a = int   (* because x is assigned an integer, and has type 'a *)
    'b = int   (* idem *)
    int = unit (* because x + y must have type unit, and is the result of + *)

The last step is to solve the type equations. In this case, the type equations
lead to a contradiction `int = unit`, meaning that the program is not well-typed.
It is rejected by the compiler before attempting to generate code.

Let us go through the three stages.

#### Generating type variables

All variables should have a type. If not supplied by the user, we use a type 
variable. The AST contains place holders for types (here in Java, transposable 
to other languages).

    class Let extends Exp {
       Id id;
       Type t;
       Exp e1;
       Exp e2;
       ...

When the AST is initially created, *fresh* type variables are generated by the 
static `Type.gen()` method. 

#### Type equations 

A *type equation* is simply a pair of types. We generate a list of 
typing equations that should hold for our term to be typable. 
These equations are generated during one recursive traversal of the AST. 

Typing is done relative to a typing environment. An *environment* `E`
is a mapping from identifiers to types. Initially, the environment 
contains the types of the predefined function. We call `PREDEF` the 
initial environment.

  `PREDEF = { print_string : string -> unit, print_int : int -> unit }`

Type equations are generated by the following algorithm.

    Algorithm GenEquations: 
       Input: (Expr, Environment, Type) 
       Output: A list of equations (Type, Type)

`GenEquation` takes three parameters:

- `Expr` the program to be type checked 
- `Environment` the environment in which the program is typechecked
- `Type` the type that the program must have

It returns a list of equations that hold if and only if 
term *Expr* has type *Type* in environment *Environment*. 
For a MinCaml program E, we need to compute `GenEquations(PREDEF, E, unit)`.
`GenEquations` is defined recursively on the structure of Expr. 

Here's the pseudocode (it uses OCaml list notation).

    GenEquations(env, expr, type) =
      case on expr:
        // only one equation "unit = type"
        UNIT -> return [ (unit, type) ]  
        // similarly "int = type", "float = type"
        INT -> return [ (int, type) ]
        FLOAT -> return  [ (float, type) ]
        // "not expr2" is a boolean, so we must have "bool = type"
        // but expr2 must be a boolean, so we generate the additional
        // equations "GenEquation(env, expr2, bool)"
        Not expr2 -> 
           eq = GenEquations(env, expr2, bool)
           return (bool, type) :: eq
        Add (expr1, expr2) ->
           eq1 = GenEquations(env, expr1, int)
           eq2 = GenEquations(env, expr2, int)
           return (int, type) :: eq1 :: eq2
        //  This case corresponds to a program of the form
        // Let (x : id_type) = exrp1 in expr2
        // expr1 must be of the type id_type, so we generate 
        // GenEquations(env, expr1, id_type) 
        // expr2 must be of type "type", but in a new environment where
        // x is of type id_type, so we generate
        // GenEquations(env + (id -> id_type), expr2, type)
        Let (id, id_type, expr1, expr2) ->
           eq1 = GenEquations(env, expr1, id_type)  
           eq2 = GenEquations(env + (id -> id_type), expr2, type)
           return e1 :: eq2
        // a variable x has a type t1 that must be known from the environment 
        // (possibly a type variable)
        // then, we have only one equation "ty = type" 
        Var (x) ->
           t1 = env(x)  (* error if not defined *)
           return [(t1, type)]
        ... to be completed!
        Letrec ...  ->  
        App ... ->

#### Equations resolution

The equations generated by GenEquations are equalities of tree expressions
with variables. Consider the following system: 

    (int -> 'a) = 'b -> 'c 
    `a = (int -> 'd)
    `d = bool

We can substitute the third equation in second one, then the second in the first. 
We obtain

    (int -> (int -> bool)) = `b -> `c. 

Finally, we deduce:

    `a = int -> bool
    `b = int 
    `c = int -> bool
    `d = bool

The *unification algorithm* is a general algorithm used to solve such tree 
equations. Some of you may remember this algorithm from UE INF242...

    Algorithm: Unification
      Input : List of equations
      Output : List of equations

    Output is a list of "solved" equations of the form:
      (var_1, type_1), ... (var_n, type_n)

    Resolution(list): 
      if list is empty
         return [] (empty list) 
      if list = (t1,t2) :: list2
       by case on (t1,t2): 
        if t1 and t2 are cleary not unifiable (int, float) or (t1 -> t2, int)
          exit("not unifiable")
        if (t1, t2) = (t, t) with t = int, bool, float, unit 
          return Resolution(list2)
        if (t1, t2) = (t11 * t12, t21 * t22)
          return Resolution ( (t11, t21) :: (t12, t22) :: list2 )
        if (t1, t2) = (t11 -> t12, t21 -> t22)
          return Resolution ( (t11, t21) :: (t12, t22) :: list2 )
        if (t1, t2) = (var, t2)
          list3 = list with *occurences of var replaced by t2*
          return (var, t2) :: Resolution(list3)
       ... to complete

