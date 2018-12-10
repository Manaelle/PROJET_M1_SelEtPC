Compilation project
===================

Welcome to the compilation project. This document contains
the basic information you need to get you started. Read everything before starging to work on the project!

Practical organization
----------------------

### Group

The very first step you need to do is to make group choices. We recommend
groups of size five people, and at least four people. 

###  Important dates

Mon. 11 -  13h30 - 16h45 Presentation of the project

Fri. 15/12, 22/12, 12/01 - 19h weekly report (mid-project submission on 22/12)

Wed. 17/01 - 12h00 Final project submission

Fri. 19 - 09h00 - 17h00 Project defense (Thu. 18?)

### Tutors presence

Most days F204 (morning or afternoon) 

Prerequisites 
-------------

It is important that you are at least familiar with both Git and OCaml at the beginning of the project. If you're not, spend 1/2 day before doing
anything else. You should also know how to manipulate trees in the programming language you'll use in the project. 

### Learning Git

This project involves working as a team on a single project. To achieve 
maximum efficiency, we force you to use the Git Source Code Management software.
If you don't know Git (or need to freshen up your memories), look at the 
[git tutorial](./tutorial_git.html).

### Learning OCaml

Although you will be compiling only a minimal subset of OCaml, MinCaml, it 
is essential for you to learn the minimum required to write small programs 
in OCaml so that you know:

* The semantics of the programs you will be compiling
* How to write tests for your compiler!

Start with this micro introduction to [MinCaml](./intro-ml.html). 
There are many resources available on the Internet. We recommend you to use:

* [try.ocamlpro.com](http://try.ocamlpro.com) where your can try by yourself the language;
* this [tutorial](https://ocaml.org/learn/tutorials/basics.html) (+ see the table of contents)
for more detailed explanations. Note that you can open 
a console with an OCaml "top-level" (interpreter) by launching the 
command `ocaml` in a terminal.

Try to focus your attention on how functions are manipulated, you may skip
features that are not supported in MinCaml, such as:

* lists
* pattern matching
* tuples
* mutable variables
* loops (for, while...)


### Trees

Tree is the main datastructure used in this project. We use tree to represent programs (Abstract syntax tree) and types. The AST type is provided in Java, OCaml and C. Trees in C and OCaml shouldn't pose any difficulties. In object oriented languages such as Java or C++, there are several ways to represent and process trees. This is discussed in [trees in Java](./java-tree.html)) and should be well-understood before doing anything else. 

Setting up the project
----------------------

### Git repository

One person on the team must setup the Git repository on the team's 
project, while the other will just clone from the team's project.

There are several places where you can set up a git repository. One of the most popular website is [github](http://www.github.com). It's free to create an account there but by default you can only create *public* repository. However, we don't want that you use public repository. To be able to use the private repo feature, you need to upgrade to a premium account (which you can do for free as a student, see [education github](http://education.github.com)). It can take up to a few days before github validates you premium account. In the meantime, you can keep your repo public.

Another option is [bitbucket](http:://www.bitbucket.com) where you can have private repository even with a standard account.

<!-- 
1. Every one will need an account on the imag forge. If you don't have one, 
  register at this [address](https://forge.imag.fr/account/register.php).

2. Setting up the team's Git. This is done by only one person.

Once you are logged in on the forge, register a new projet 
`My Page->Register Project`, or use this [link](https://forge.imag.fr/register).
Choose a project name as close to your team's name as possible, make 
sure to choose git as the SCM, *LIG* as the laboratory, and make the 
project *private*.

When the project is accepted, do not forget to add every team member to 
the list of developers so they can contribute.

Make an initial (empty) clone of your project. this will create 
a directory `compilation-project` in the current working directory.

    git clone git+ssh://<login>@scm.forge.imag.fr/var/lib/gforge/chroot/scmrepos/git/<project>.git compilation-project

If at that point you must enter your password, you should consider
uploading your ssh key to the forge... -->

One student has to create the repository. Make it private if you can.
Once this is done, you need to add the material for the project. Download the archive from the project main page, then

    tar xvf archive.tar.gz

Copy the files from the archive to your git directory and make an initial add of those files so that they are 
available to everyone on the team. You can follow github/bitbucket instruction or do something like that:

    git add *
    git commit -m 'Initial import'
    git push

The person that created the repo can add the other students as collaborator so that they can clone the repo on their personal computer.

Once the project is setup, you should send an email with the project URL.

### Personal computer

If you want to work on your personal computer, you should install (at 
minimum) the following packages:

* git
* for a project in C:
  * bison >= 2.3
  * flex >= 2.5
* for a project in OCaml:
  * ocaml >= 4.01
  * ocamlbuild
  * ocamlyacc
* to test the assembly:
  * qemu-user
  * gcc-arm-none-eabi
  * gdb-arm-none-eabi

Roadmap and organization advice
-------------------------------

### Step 1

Read the [reference paper](./min-caml_article.pdf). It describes the compilation project as it was used at university of Tokyo. You don't have to understand everything and not everything is relevant but some parts will serve as *reference for the implementation*.

The most important parts are section 2 and 4. In particular:

* Section 2 and Figure 1 describe the MinCaml language. The semantics of the MinCaml is the same as OCaml for the syntactic subset given in the paper,
* Section 4.3 to 4.11 describe the transformations you will have to implement in the front-end. These transformations are explained furthermore in the provided doc.

### Step 2

Before trying to code anything, everybody should get accustomed to the three languages used in the project. MinCaml, ASML and ARM. There are code examples for each of these languages in the archive, as well as instructions to execute them (see READMEs).

* MinCaml can be parsed with the provided parser, and run using ocaml.
* ARM programs are run using the qemu VM
* ASML programs can be run using a provided interpreter.
* You can compile C programs to ARM using gcc-arm-none-eabi (useful to see how to generate ARM code) 

### Step 3

Get an understanding of the different tasks in the project and decide what you want to work on. 

There are several tasks that can be performed more or less separately. For each of these tasks, there is a corresponding documentation.

* [Typechecking](./typing.html)
* [Compilation of MinCaml to ASML](./frontend.html)
* [Compilation of ASML to ARM](./backend.html)
* [Testing](./testing.html)

Moreover, typechecking and the front-end rely heavily on tree traversal algorithms (in Java, see [trees in Java](./java-tree.html)). 

The front-end and the back-end interface eachother via the intermediary language [ASML](./asml.html). It means you need to develop a data structure that represent ASML programs in memory (some sort of AST for ASML). More likely, the sub-team working on the  backend will write this datatype because they need it first.

Testing is an orthogonal part of the project. It requires a good understanding of the specifications. You will write a testing infrastructure (scripts to run the programs to test) and test cases (input programs).

### Step 4: Preliminary exercices

These exercises can be done independently.

#### Tree traversals

Before starting to work on the front-end.

1. Compute the set of variables occuring in a MinCaml AST.
2. Duplicate an AST (create a copy of an AST, don't just copy a reference, this means you have to walk the AST and duplicate each node).

Use the tree traversals algorithms given in [trees in java](./java-tree.html) document 
and in the provided code samples (HeightVisitor, PrintVisitor...).

#### ASML datatype

The first step in the backend part is to design a data structure for the ASML
language.  ASML is the intermediary language. It is convenient to have a data structure to store in memory such a program (some type of AST for ASML). You can start with something simple such as a list of ARM instructions.

It may be handy (but not necessary) to write a simple parser for ASML. This way you can test the backend before the front-end is complete. 

#### Command line options

Implement the command-line options (see below for a description). 

### Step 5

Make a development plan. 

Subdivide the project in small increments. For a compiler, it is natural to define increments as subsets of the source languages. We suggest the following steps.

1. simple arithmetic expression
2. call to external functions 
3. functions (let rec)
4. if-then-else
5. closures, tuple, arrays
6. only for the best teams: floats or other extensions

You don't have to follow precisely this order, but you should aim to reach
point 3 for the mid-project submission. You may also identify other intermediary
steps (for instance, dealing with bigger integer in ARM, or register allocation
strategy).  

These steps can be followed for each task of the project (e.g. each transformation
in the front-end, typechecking, testing, backend...). A good way to proceed 
is to write the tests before the code. Increments can be specified by set of
test cases. 

For reference, usually only few groups manage to have step 5 working perfectly by the end of the project. Most serious groups complete step 4. 

### Step 6

Set up your environment (see [testing](./testing.html) for project structure). You will save *a lot of time* if you have a properly set up environment from the beginning of the project. 

For developping in Java, a popular IDE is Eclipse. If you develop in OCaml, we recommend a general purpose editor such as VSCode. It works well with most programming languages and integrates very nicely with Git.

Other documents: 
----------------

* presentation slides
* arm-reference-manual.pdf

Remarks on the provided java code
---------------------------------
It is minimalist on purpose, we expect you to refactor it to follow
Java coding guidelines. At the very least, you should organizing classes 
into packages and separate files. Other ideas:

* compiling/deploying/testing with "maven" or "ant"
  (Makefile not portable, not recommended for java programs)
* use javadoc
* unit testing (e.g. JUnit)

Mincamlc
--------

We will use our test suite on your compilers. So it is *very important*
that your mincamlc program be self-contained and respect the following
interface. 

### Returned value

* 0 on success
* 1 on error, plus error message on stderr

### Command line options

* -o <file> : output file
* -h : display help
* -v : display version
* -t : type check only
* -p : parse only
* -asml : output ASML
* -my-opt : you can add personal options (optimizations, etc.)

Your program should not write anything on standard output (except for the -h, -v
and -my-opt option). If you want to display extra information, for instance the
types of the function in the MinCaml program, add an extra option for it.
Unimplemented features should lead to a specific exception (e.g.
NotYetImplemented). There should not be any development trace in the released
version.

Submissions
-----------

For both submissions, we will retrieve and evaluate the code from your 
git archive on the master branch at the specified date and time.
You will give us the documentation of the project at the beginning of the 
defense. 

We will evaluate the code (including the scripts for testing) on mandelbrot.
It is recommended that you do a fresh clone your repository before the submission
to make sure everything is working properly.

Include a README that explains how to compile and lauch the compiler, and how
to launch the test scripts. Say also what subset of mincaml is working
properly. We are not interested in half-finished features. We would prefer
to see robust and well-tested compilers on a smaller subset, rather than
a lot features that more or less work.

### Mid-project submission

For the mid-project, your compiler should work at least on a subset of the language
that contains arithmetic expressions (`+` and `-`) and function calls (including
to the library function `print_int`). The command line options should be fully
functional.
