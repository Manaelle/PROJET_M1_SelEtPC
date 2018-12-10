Front-end
=========

The *front-end* of the compiler is fully specified in the 
[reference paper](./min-caml_article.pdf) (section 4). This document provides some examples and
clarifications, but ultimately, you should refer to the paper.

The front-end transforms a MinCaml program into an ASML program (See
[ASML](./asml.html)). Typechecking is technically part of the front-end but is
presented in [typing](./typing.html).

The MinCaml front-end is defined as a sequence of small and simple 
transformations. 

- K-Normalization
- alpha-conversion
- reduction of nested-let
- closure conversion
- ASML generation
- other transformations (beta-reduction, inline expansion, constant folding, 
elimination of unnecessary definitions, 13-bit optimization) are for 
optimisation purpose and can be skipped.

In your front-end, you will write these transformations using tree traversal
algorithms. Typically, the transformation algorithms are defined recursively on
the structure of the tree.

There are several choices to be made to implement these algorithms. 

* How to represent the tree and which pattern to use for the tree traversal (see [trees in java](./java-tree.html)).
* Do you construct a new tree, or update the current tree. For some transformation, it will be 
  easier to construct a new tree (k-normalisation) and for other (alpha-conversation), and update will be easier. 
* What new datatypes do you need? when do you need a new datatype for the transformed tree?
 At the very least, you will need to define a new datatype for ASML. 

Now we go through the main transformations on two examples (be careful that it 
is not be *exactly* what you will get by applying strictly the algorithms of the documents)
but you should get the idea.

Example 1
---------

    let x = 
       let y = 1 + 2 in y
    in let rec succ x = x + 1 
    in let rec double x = 2 * x 
    in print_int (succ (double x))

### K-normalization

 All nested expressions are replaced by new *fresh* variables.
 By doing so, the code gets one step closer to assembly languages where
 instructions operate on registers instead of expressions.  Notice that
 this introduces many temporary variables. Eventually, all these variables will 
 be efficiently translated to registers (or optimized away in some of the
 transformations).

    let x = 
      let y = 
       let v1 = 1 in
       let v2 = 2 in
       v1 + v2
       in y

    in let rec succ x = 
       let v3 = x in
       let v4 = 1 in
       v3 + v4

    in let rec double x = 
       let v5 = 2 in
       let v6 = x in
       v5 * v6

    in let v7 = 
       let v8 = 
         let v9 = x in double v9 in
       succ v8
    in print_int v7

### Alpha-conversion

Here we make sure that all the bound variables (variables defined by a let or functions parameters)
are different. This will be needed for some of the next steps.
     
    let x = 
      let y = 
       let v1 = 1 in
       let v2 = 2 in
       v1 + v2
       in y 

    in let rec succ x1 = 
       let v3 = x1 in
       let v4 = 1 in
       v3 + v4

    in let rec double x2 = 
       let v5 = 2 in
       let v6 = x2 in
       v5 * v6

    in let v7 = 
       let v8 = 
         let v9 = x in double v9 in
       succ v8
    in print_int v7


### Reduction of nested Let-expressions:

We "linearize" or "flatten" all the let definitions (but not the let-rec). 
For instance:

    let x = 
       (let y = e1 in e2)
    in e3

becomes:

    let y = e1 in
    let x = e2 in
    e3

On the example, it gives us:

    let v1 = 1 in
    let v2 = 2 in
    let y = v1 + v2 in
    let x = y in

    let rec succ x1 = 
      let v3 = x1 in
      let v4 = 1 in
      v3 + v4 in 

    let rec double x2 = 
      let v5 = 2 in
      let v6 = x2 in
      v5 * v6 in

    let v9 = x in
    let v8 = double v9 in
    let v7 = succ v8 in
     print_int v7

### Closure Conversion 

In the next transformation, we separate function definitions from the rest
of the code. First, we suppose that there are no higher-order functions. It means
that functions don't take other functions as parameters and don't return functions.  

In that case:

* we flatten the nest let rec. 
* for each function, we assign a label that will be used later on in the generated assembly
* we identify the parameters of the function
* we replace the calls "f x1 ... xn" with a new instruction "apply_direct(l, (x1 ... xn))" where l
 is the label for f.

In the current examples, we generate the following function definitions:

    label: _succ
    parameters: x2 
    code:
      let v4 = x2 in
      let v5 = 1 in
      let v6 = x2 + 1 in
      v6  

    label: _double
    parameters: x3  
    code:
       let v7 = 2 in
       let v8 = x3 in
       let v9 = v7 * v8 in
       v9

and the main program:

    let v1 = 1 in
    let v2 = 2 in
    let v3 = v1 + v2 in
    let x1 = v3 in
    let x = x1 in 
    let v10 = apply_direct(_double, x) in
    let v11 = apply_direct(_succ, v10) in
    print_int v11

### ASML code generation. 

The last step is ASML generation. ASML generation makes explicit the memory
accesses used for closures, tuples and arrays. See [Closure](./closure.html) and
[ASML](./asml.html) for more details.

Example 2
---------

We consider now a more challenging example with a higher-order function.

    let rec f x =
       let rec g y = x + y in g
    in print_int ((f 0) 0)

In this program, the function f returns a value g that is itself a function.
To make things harder, g is a nested function that uses a variable `x` declared
outside its scope (`x` is not a parameter).

The transformations work as before except closure conversion that requires more
care. Just before closure conversion, we get this program:

    let rec f x = 
       let rec g y = x + y in g
    in 

    let v1 = f 0 in
    let v2 = v1 0 in
    print_int v2

There are two issues. If we flatten the let definitions, we get something non-sensical.

    let g y = x + y 
    let rec f x = g in
    ...

Moreover, the value returned by g is a function. Is it enough to replace this variable g
 by the label assigned to g in the transformation? 

These issues are adressed by using the notion of closure. As explained in 
[closure](./closure.html), at a lower-level, the value returned by `f` 
is encoded as a structure (callsed a closure) that contains a pointer to g (given by its label),
and the value of the free variables of `g` at the time it was created. 

In the closure conversion step, we define a `make_closure` operation
that constructs a closure, and apply_closure that calls the function 
in the closure with the provided arguments, and replace the free variables 
by the values stored in the closure. 

After closure conversion, we get something like this:

#### Functions definitions:

    label: _f 
    free variables: None
    parameters: x
    code:
       let v3 = make_closure(_g,x) 
       in v3

    label _g:
    free variables: x
    parameters: y
    code:
         x + y  

#### Main program:

    let v1 = apply_direct(_f, 0) in
    let v2 = apply_closure(v1, 0) in
    print_int v2

