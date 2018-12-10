
let break l1 l2 = 
    List.map2 (fun x -> fun y -> (x, y)) l1 l2

let subst x t =
    match t with
      | Unit -> Unit
      | Bool -> Bool
      | Int -> Int
      | Float -> Float
      | Fun (tl, t) -> 
      | Tuple (tl) -> Tuple (List.map subst tl)
      | Array (tl) -> Array (List.map subst tl)
      | Var y -> if y = x then t else Var y  

let unify l =
    match l with 
   | [] -> true
   | (t1, t2) :: ls ->  
     match (t1, t2) with
    | (Unit, Unit) -> unify ls 
    | (Bool, Bool) -> unify ls 
    | (Int, Int) -> unify ls 
    | (Float, Float) -> unify ls 
    | (Fun (tl1, t1), Fun (tl2, t2)) -> 
            unify (break tl1 tl2) :: (t1, t2) :: ls 
    | (Tuple tl1, Tuple tl2) -> unify (break tl1 tl2) :: ls
    | (Array tl1, Array tl2) -> unify (break tl1 tl2) :: ls
    | (Var x, t) -> unify (subst x t ls)  
    | (t, Var x) -> unify (Var x, t) :: ls
    | _ -> raise Not_unifiable


