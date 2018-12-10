open Syntax
(* val gen_constraints env : Syntax.t -> unit *)

let gen_constraints env env e t =
    match e with
  | Unit -> [(Type.Unit, t)]
  | Bool _ -> [(Type.Bool, t)]
  | Int _ -> [(Type.Int, t)]
  | Float _ -> [(Type.Float, t)]
  | Not e1 -> (t, Type.Bool) :: gen_constraints env e1 Type.Bool
  | Neg e1 -> (t, Type.Int) :: gen_constraints env e1 Type.Int 
  | Add (e1, e2) -> 
          (t, Type.Int) :: (gen_constraints env e1 Type.Int) @
          (gen_constraints env e2 Type.Int)
  | Sub (e1, e2) ->
         (t, Type.Int) :: (gen_constraints env e1 Type.Int) @
          (gen_constraints env e2 Type.Int)
  | FNeg e1 -> (t, Type.Float) ::gen_constraints env e1 Type.Float 
  | FAdd (e1, e2)->
        (t, Type.Float) ::  (gen_constraints env e1 Type.Float) @
          (gen_constraints env e2 Type.Float)
  | FSub (e1, e2) ->
        (t, Type.Float) ::    (gen_constraints env e1 Type.Float) @
          (gen_constraints env e2 Type.Float)
  | FMult (e1, e2) ->
          (t, Type.Float) ::  (gen_constraints env e1 Type.Float) @
          (gen_constraints env e2 Type.Float)
  | FDiv (e1, e2) ->
          (t, Type.Float) ::  (gen_constraints env e1 Type.Float) @
          (gen_constraints env e2 Type.Float)
  | Eq (e1, e2) -> 
          let nt = gentyp () in
          (t, nt) :: 
          (gen_constraints env e1 nt) @
          (gen_constraints env e2 nt)
  | LE (e1, e2) ->
          let nt = gentyp () in
          (t, nt) :: 
          (gen_constraints env e1 nt) @
          (gen_constraints env e2 nt)
  | If (e1, e2, e3) -> 
          (t, Type.Unit) :: 
          (gen_constraints env e1 Type.Bool) @
          (gen_constraints env e2 Type.Unit) @
          (gen_constraints env e3 Type.Unit) 
  | Let ((id, t1), e1, e2) ->
          let nenv = ((id, t1) :: env) in
          (gen_constraints nenv e2 t) @
          (gen_constraints env e1 t1)
  | Var (id) ->
          let t1 = List.assoc id env in
          [(t,t1)]
  | LetRec (fd, e) -> []
  | App (e, es) -> []
  | Tuple (es) -> []
  | LetTuple (id_t_s, e1, e2) -> []
  | Array (e1, e2) -> []
  | Get (e1, e2) -> []
  | Put (e1, e2, e3) -> [] 
