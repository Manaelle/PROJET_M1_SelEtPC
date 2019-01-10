let rec f x = x > 0 in
let res = 
if (f 1) then
  (if true then 42 else 44)
else
  43
in print_int res
