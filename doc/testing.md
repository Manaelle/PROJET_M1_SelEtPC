Testing
=======

File organization 
-----------------

Organize your MinCaml test infrastructure using the following conventions. 

    scripts/
  	tests/
      syntax/
         valid/
           tests that should parse correctly
         invalid/
           tests that shouldn't parse correctly
  	  typechecking/
  	     valid/
  	       tests that should typecheck
  	     invalid/
  	       tests that shouldn't typecheck
  	  gen-code/
  	     contains examples that should compile and execute properly

Put executable programs in `scripts` and add this directory to your `$PATH`.
You should have at least two types of files in `scripts/`. 

1. Symbolic links to mincamlc and asmlc and other executables you may have developed (e.g. the backend)
2. Scripts that run the tests case (launcher) and check the results.

The test cases (MinCaml program) are placed in `tests/` accordingly to the above
conventions.   Try to use explicit names for your tests, and use comments to
describe what feature is tested. Note that you don't have to test the parser
since we gave it to you.

As an example, we provide a script `mincaml-test-parser.sh` that runs the test
in syntax/valid and syntax/invalid. 

You will have to extend this infrastructure. For instance, you should create
subdirs of `valid/` `invalid/` and `gencode/` for different increments of your
language. You should add new scripts to test typechecking, code generation,
command-line options and so on.

Testing strategy
----------------

There are several strategy to check whether a test has passed or failed,
depending on the program output on the test case. For instance, you can:

1.  check the error code  
2.  compare the actual result with the expected result using diff. Expected
result can be store in a separate file, or in a comment in the test case. 

To test typechecking, you will typically check the error code. But to test
code generation, you can't really compare the generated code with an expected result because
there can be many valid results. In that case, you can run the generated code (for instance
using the simulator for ASML code, or qemu for ARM code) and compare the result with
the expected behavior of the program.

Suppose you want to test your ARM compiler on the test case `test_print_int.ml`:

    let _ = print_int 42

You will store the expected result (`42`) in a file `test_print_int.expected`
Then you (probably using a script) will compile `test_print_int.ml`, compile the `test_print_int.s`
ARM file, run it with qemu, store the result in a file `test_print_int.actual`. Finally, you can
compare `test_print_int.actual` with `test_print_int.expected` using `diff`.

Note that you can automatically compute the expect result by running the test case with the 
ocaml interpreter.

Remark: 
 you can test the "completude" of your test base using code coverage evaluation 
 tools such as Cobertura.

Unit tests
----------

In addition to the *system tests* described above, you can add unit tests (e.g. using JUnit in Java).