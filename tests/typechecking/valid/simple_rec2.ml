let rec sum x = if x = 0 then 0 else 1 + (sum (x - 1)) in
print_int (sum 42)
