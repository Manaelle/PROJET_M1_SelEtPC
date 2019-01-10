let rec f x = true in
let res = 
if (f 0) then
  42
else
  43
in print_int res
