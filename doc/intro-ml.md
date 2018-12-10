Quick intro to OCaml and MinCaml
================================

Expressions vs Statements
-------------------------

An *imperative* program is a sequence of *statements*.

    x = 1 + 1;
    y = 2 + x;
    print("result = %d\n", y);

Each line describes an action that has an effect (update the memory,
display something).

The statements contain *expressions* like `2 + x` or `1 + 1`. A *pure* expression 
evaluates to a value, but doesn't have (side) effect.

*Functional* programs are usually expression. For instance:

    let x = 1 + 1 in
    let y = 2 + x in
    y

This program is an expression, that evaluates to `4`. It has no side effect.

In most programming languages, the line is not so clear-cut between expressions
and statements.

For instance, in C, expressions are statements too. Consider this program:

    x = 1;
    2;
    3;
    print("%d\n", x);

Actually, even `x = 1` is actually an expression! `x = 1` evaluates to 1, with
the side-effect of assigning 1 to x. One can write:

    x = 1;
    y = x = 1;

In functional languages, some expressions have side-effects too. For instance,

    let x = 1 in
    let y = 1 in
    print_int (x + y)

This programs evaluates to `()` and prints `2` as a side effect. Understand
the difference with

    let x = 1 in
    let y = 1 in
    x + y

This programs evaluates to `2` with no side-effect.

Generally, side effects are useful for input/output, or when using *mutable*
data structures, but functional programming try to reduce them to a minimum.

Nowadays, modern languages tend to be multi-paradigm (imperative, OO, functional)
and the programmer can choose the best style depending on the problem to be 
solved. 

Simple expressions
------------------

Try them to with the OCaml *top-level* (interpreter `ocaml`). 

    1;;

    1 + 2;;

    3.0 +. 7.0;;

    1.0 + 3;;   (* not well-typed *)

    let x = 2 in
    let y = 3 in
    x + y;;

    let x = 2 in       
    let y = 3 in
    if (x >= y) then  (* similar to (x >= y)?1:0  in C *)
      1
    else
      0;;

    if true then false else true;

Now expression with side effects:

    let x = 1 in print_int 1;;

    let x = print_int 1 in 
    let y = 2 in 
    print_int y;;

    let _ = print_int 1 in 
    let y = 2 in
    print_int y;;

    let y = (print_int 1); 2 in
    print_int y;;


Remark:

* the `;;` is useful in the top-level interpreter `ocaml` but not needed 
  in programs compiled with `ocamlc`. In MinCaml, it's incorrect to use it. 

* notice that each expression has a type, and is evaluated to a value. We have
  seen the types: `int`, `float`, `unit`, `bool`. `bool` has two values,
  `true` and `false`, while `unit` has one `()` (called nil).

* `A;B` is a shortcut for `let _ = A in B`. Normally `A` has unit type. 

In OCaml (but not MinCaml), we can define several `let` in sequence. 

    let x = 1;;
    let y = 2;;
    let _ = print_int (x + y);;

Functions
---------

    let f x = x

    let f x = x + 1

    let f = fun x -> x + 1

    let rec f x = x + 1 (* rec is mandatory for all functions in mincaml *)

    let rec fact x = 
      if x == 0 then
         1
      else 
         x * (fact (x - 1)) (* be careful to use proper parenthesis)

Remark:

OCaml is a statically typed languages, but the programmer doesn't need
to explicitly give the type. One could write:

      let f (x : int) : int = x + 1

But even if we don't, the compiler will *infer* that `f` has type `int -> int`.

In MinCaml, explict types are forbidden, and the `fun` keyword isn't defined.








