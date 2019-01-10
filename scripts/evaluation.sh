red='\033[0;31m'
green='\033[0;32m'
blue='\033[0;34m'
yellow='\033[0;33m'
bold='\033[0;1m'
normal='\033[0;0m'

printf "42" > expected

# creation of the test cases
cat << EOF > fortytwo.ml
print_int 42
EOF
cat << EOF > fortytwo_unit.ml
(); (print_int 42)
EOF
cat << EOF > let1.ml
let x = 42 in print_int x
EOF
cat << EOF > let2.ml
let x = 41 + 1 in print_int x
EOF
cat << EOF > let3.ml
let x = (let y = 41 in y) + 1 in print_int x
EOF
cat << EOF > let4.ml
let x = (let x = 41 in x) + 1 in print_int (let y = x - 1 in y + 1)
EOF
cat << EOF > let5_seq.ml
let x = (let x = true in ()); 42 in print_int x
EOF
cat << EOF > let_spill.ml
let x = (1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1) + (1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1) + 22 in print_int x
EOF
cat << EOF > simple_fun.ml
let rec f x = x + 1 in print_int (f 41)
EOF
cat << EOF > simple_fun2.ml
let rec f x = x + 1 in 
let rec g x = x + 2 in 
print_int (g 40)
EOF
cat << EOF > simple_fun3.ml
let rec f x = x + 1 in 
let rec f x = x + 2 in 
print_int (f 40)
EOF
cat << EOF > simple_fun4.ml
let rec f x = x + 40 in 
let y = 0 in 
let rec g x = x + 2 in 
print_int (y + (f 0) + (g 0)) 
EOF
cat << EOF > fun_many_params.ml
let rec f a b c d e f g = g in 
print_int (f 1 2 3 4 5 6 42)
EOF
cat << EOF > fun_many_params2.ml
let rec f a b c d e f g = a + b + c + d + e + f + g in 
print_int (f 0 0 0 0 0 0 42)
EOF
cat << EOF > nested_fun.ml
let rec f x = 
  let rec add x y = x + y in 
  add x 2 in 
print_int (f 40)
EOF
cat << EOF > simple_rec.ml
let rec sum x = if x = 0 then 42 else (sum (x - 1)) in
print_int (sum 10)
EOF
cat << EOF > simple_rec2.ml
let rec sum x = if x = 0 then 0 else 1 + (sum (x - 1)) in
print_int (sum 42)
EOF
cat << EOF > bigint.ml
let x = 10000000 in
if x = 10000000 then print_int 42 else print_int 0
EOF
cat << EOF > array1.ml
let ar = Array.create 100 42 in
print_int ar.(10)
EOF
cat << EOF > array2.ml
let ar = Array.create 100 0 in
ar.(10) <- 42;
(print_int ar.(10))
EOF
cat << EOF > array_unit1.ml
let ar = Array.create 100 () in
ar.(10) <- ();
(print_int 42)
EOF
cat << EOF > array_unit2.ml
let ar = Array.create 100 () in
ar.(10);
(print_int 42)
EOF
cat << EOF > array_bool_if.ml
let ar = Array.create 100 true in
if ar.(10) then
   print_int 42
else
   print_int 43
EOF
cat << EOF > array_sum.ml
let rec sum ar n = 
  if n = 0 then
    0
  else 
    ar.(n-1) + (sum ar (n-1)) 
  in
let ar = Array.create 42 1 in
print_int (sum ar 42)
EOF
cat << EOF > couple.ml
let rec f x = 
  let (a,b) = x in print_int (a + b)
in
  f (20,22)
EOF
cat << EOF > triple.ml
let rec f x = 
  let (a,b,c) = x in print_int (a + b + c)
in
  f (20,21,1)
EOF
cat << EOF > ifthenelse1.ml
if true then
  print_int 42
else
  print_int 43
EOF
cat << EOF > ifthenelse2.ml
let rec f x = true in
let res = 
if (f 0) then
  42
else
  43
in print_int res
EOF
cat << EOF > ifthenelse3.ml
let rec f x = if x > 0 then true else false in
let res = 
if (f 1) then
  (if true then 42 else 44)
else
  43
in print_int res
EOF
cat << EOF > ifthenelse4.ml
if 0 = 0 then
  print_int 42
else
  print_int 43
EOF
cat << EOF > ifthenelse5.ml
let rec f x = (0 = 0) in
let res = 
if (f 0) then
  42
else
  43
in print_int res
EOF
cat << EOF > ifthenelse6.ml
let rec f x = x > 0 in
let res = 
if (f 1) then
  (if true then 42 else 44)
else
  43
in print_int res
EOF
cat << EOF > closure.ml
let rec make_adder x =
  let rec adder y = x + y in
  adder in
print_int ((make_adder 39) 3)
EOF
cat << EOF > closure1.ml
let rec f y =
  let rec g x = x in
  g 
in
print_int ((f 0) 42)
EOF
cat << EOF > closure2.ml
let rec f y z =
  let rec g x = y + z + x in
  g 
in
print_int ((f 20 21) 1)
EOF
cat << EOF > closure_hard.ml
let t = 42 in
let f = 45 in
let rec even x =
  let rec odd x =
    if x > 0 then even (x - 1) else
    if x < 0 then even (x + 1) else
    f in
  if x > 0 then odd (x - 1) else
  if x < 0 then odd (x + 1) else
  t in
print_int (even 50)
EOF
cat << EOF > closure_array.ml
let rec f x = 42 in
let ar = Array.create 100 f in
(print_int (ar.(0) 0))
EOF
cat << EOF > closure_tuple.ml
let rec f x = 42 in
let rec g x = 43 in
let t = (f, g) in 
let (a,b) = t in
(a 0)
EOF
cat << EOF > bad1.ml
let rec x = false + 10 in
print_int x
EOF
cat << EOF > bad2.ml
10
EOF
cat << EOF > bad3.ml
1 + false
EOF
cat << EOF > bad4.ml
true + false
EOF
cat << EOF > bad5.ml
let f x = x in
f + 1
EOF
cat << EOF > bad6.ml
let x = (1, true) in
print_int x 
EOF
cat << EOF > bad7.ml
let x = (1, true) in
print_int x
EOF
cat << EOF > bad8.ml
let rec f x = x + 1 in 
let y = f true in 
print_int 42
EOF
cat << EOF > bad9.ml
let rec f x = x + 1 in 
let y = f 1 true in 
print_int 42
EOF
cat << EOF > bad10.ml
let a = Array.create 10 true in 
print_int (a.(0)) 
EOF
cat << EOF > bad10.ml
let a = Array.create 10 true in 
print_int (a.(0)) 
EOF
cat << EOF > bad11.ml
let f g = g 1 2 in
let h a b = a + b in
print_int (f h)
EOF

TC="fortytwo.ml fortytwo_unit.ml let1.ml let2.ml let3.ml let4.ml let5_seq.ml let_spill.ml simple_fun.ml simple_fun2.ml simple_fun3.ml simple_fun4.ml\
  fun_many_params.ml fun_many_param2.ml nested_fun.ml simple_rec.ml simple_rec2.ml bigint.ml array1.ml array2.ml array_unit1.ml array_unit2.ml array_bool_if.ml array_sum.ml couple.ml\
 triple.ml ifthenelse1.ml ifthenelse2.ml ifthenelse3.ml ifthenelse4.ml ifthenelse5.ml ifthenelse6.ml closure.ml closure1.ml closure2.ml closure_hard.ml closure_array.ml closure_tuple.ml"
TC_VALID_TYPECHECKING=$TC
TC_INVALID_TYPECHECKING="bad1.ml bad2.ml bad3.ml bad4.ml bad5.ml bad6.ml bad7.ml bad8.ml bad9.ml bad10.ml bad11.ml"

MINCAMLC=scripts/mincamlc
ASML=tools/asml
GENARMEXE=scripts/genarmexe

function die() {
    printf "${red}ERROR${normal}: $@\n"
    exit 1
}

SCORE_TYPE=0
TOTAL_SCORE_TYPE=0

SCORE_ARM=0
TOTAL_SCORE_ARM=0

function test_typechecker_expected_success() {
  printf "${bold}test typechecking (expecting success) ${normal} "
  echo "$MINCAMLC -t $1"
  let TOTAL_SCORE_TYPE=TOTAL_SCORE_TYPE+5
  if $MINCAMLC -t $1 &> /dev/null
  then 
    let SCORE_TYPE=SCORE_TYPE+5
    printf "${green}OK${normal}\n"
  else
    printf "${red}KO${normal}\n"
  fi
}

function test_typechecker_expected_failure() {
  printf "${bold}test typechecking (expecting failure) ${normal} "
  echo "$MINCAMLC -t $1"
  let TOTAL_SCORE_TYPE=TOTAL_SCORE_TYPE+5
  if $MINCAMLC -t $1 &> /dev/null
  then 
    printf "${red}KO${normal}\n"
  else
    let SCORE_TYPE=SCORE_TYPE+5
    printf "${green}OK${normal}\n"
  fi
}

function test_asml() {
  test_case=$1
  test_case_asml=${test_case%.ml}.asml
  test_case_arm=${test_case%.ml}.s
  printf "${bold}test asml/arm${normal} $1\n"
  printf "  ${bold}asml${normal} $MINCAMLC -asml $1 -o $test_case_asml\n"
  let TOTAL_SCORE_ARM=TOTAL_SCORE_ARM+10
  if $MINCAMLC -asml $1 -o $test_case_asml &> /dev/null
  then 
    if [ ! -f $test_case_asml ]; then
      printf "  ${red}KO${normal} can't find $test_case_asml\n"
    else
      actual=${test_case%.ml}.actual  
      rm -f $actual
      # expected=${test_case%.ml}.expected
      if $ASML $test_case_asml 1> $actual 2> /dev/null
      then
         if diff -q $actual expected
         then
          let SCORE_ARM=SCORE_ARM+3
          printf "  ${green}OK${normal}\n"
        else 
          printf "  ${red}KO${normal} expected differs from actual\n"
        fi 
      else
         printf "  ${red}KO${normal} can't run $test_case_asml file\n"
      fi
    fi
  else
    printf "  ${red}KO${normal} unable to compile $1\n"
  fi

  printf "  ${bold}arm${normal} $MINCAMLC $1 -o $test_case_arm\n"
  if $MINCAMLC $1 -o $test_case_arm &> /dev/null
  then 
    if [ ! -f $test_case_asml ]; then
      printf "  ${red}KO${normal} can't find $test_case_arm\n"
    else
      exe=${test_case_arm%.s}
      actual=${test_case%.ml}.actual  
      rm -f $actual
      $GENARMEXE $test_case_arm &> /dev/null
      if [ -f $exe ]
      then 
         qemu-arm $exe > $actual
         if diff -q $actual expected
         then
             let SCORE_ARM=SCORE_ARM+7
             printf "  ${green}OK${normal}\n"
         else 
            printf "  ${red}KO${normal} expected differs from actual\n"
         fi 
      else
         printf "  ${red}KO${normal} can't find $exe\n"
      fi
    fi
  else
    printf "  ${red}KO${normal} unable to compile $1\n"
  fi
}

function test_options() {
  printf "${bold}test $MINCAMLC ${normal} $1\n"
  $MINCAMLC
  printf "${bold}test $MINCAMLC -h${normal} $1\n"
  $MINCAMLC -h
}

function test_basics() {
  if [ -f makefile ]; then
    mv makefile Makefile
  fi
  for i in $MINCAMLC $ASML $GENARMEXE Makefile
  do
    if [ ! -f "$i" ]; then
        die "Cannot find $i"
    fi
  done
}

printf "${blue} -- Basics${normal}\n"
test_basics
printf "${blue} -- Option${normal}\n"
test_options

printf "${blue} -- Typechecking valid${normal}\n"
for f in $TC_VALID_TYPECHECKING
do
  test_typechecker_expected_success $f
done
printf "${blue} -- Typechecking invalid${normal}\n"
for f in $TC_INVALID_TYPECHECKING
do
  test_typechecker_expected_failure $f
done

printf "${blue} -- ASML/ARM${normal}\n"
for f in $TC
do
  test_asml $f
done

printf "SCORE TYPE = %d/%d\n" $SCORE_TYPE $TOTAL_SCORE_TYPE
printf "SCORE ARM = %d/%d\n" $SCORE_ARM $TOTAL_SCORE_ARM

