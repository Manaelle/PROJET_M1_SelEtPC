(let ?v1 = (let ?v2 = 123 in (print_int ?v2)) in (let ?v0 = (let ?v4 = (let ?v3 = 456 in (- ?v3)) in (print_int ?v4)) in (let ?v7 = (let ?v5 = 789 in (let ?v6 = 0 in (?v5 + ?v6))) in (print_int ?v7))))
