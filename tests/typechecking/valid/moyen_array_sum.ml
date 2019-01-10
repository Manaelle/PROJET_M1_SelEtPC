let rec sum ar n = 
  if n = 0 then
    0
  else 
    ar.(n-1) + (sum ar (n-1)) 
  in
let ar = Array.create 42 1 in
print_int (sum ar 42)
