let rec f x = if x > 0 then true else false in
let res = 
if (f 1) then
  (if true then 42 else 44)
else
  43
in print_int res
