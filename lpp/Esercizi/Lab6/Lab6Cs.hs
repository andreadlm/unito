type Value = Int
type Var   = Int
type Stack = [Value]
type Frame = [Value]

load :: Var -> Frame -> Value
load _ []       = 0
load 0 (v : _)  = v
load n (_ : vs) = load (n - 1) vs

store :: Var -> Value -> Frame -> Frame
store 0 v []       = [v]
store 0 v (_ : vs) = v : vs
store n v []       = 0 : store (n - 1) v []
store n v (w : vs) = w : store (n - 1) v vs

type Code = [Instruction]

data Instruction
  = PUSH Value
  | LOAD Var
  | STORE Var
  | OP (Value -> Value -> Value)
  | UOP (Value -> Value)
  | IF (Value -> Value -> Bool) Code
  | DUP
  | SWAP
  | POP
  | NOP
  | RETURN

run :: Code -> Frame -> Value
run = aux []
  where
    aux :: Stack -> Code -> Frame -> Value
    aux (v : [])     (RETURN : _)   _  = v
    aux vs           (PUSH v : is)  fr = aux (v : vs) is fr
    aux vs           (LOAD x : is)  fr = aux (load x fr : vs) is fr
    aux (v : vs)     (STORE x : is) fr = aux vs is (store x v fr)
    aux (w : v : vs) (OP f : is)    fr = aux (f v w : vs) is fr
    aux (w : v : vs) (IF p is : _)  fr | p v w = aux vs is fr
    aux (_ : _ : vs) (IF _ _ : is)  fr = aux vs is fr
    aux (v : vs)     (DUP : is)     fr = aux (v : v : vs) is fr
    aux (w : v : vs) (SWAP : is)    fr = aux (v : w : vs) is fr
    aux (_ : vs)     (POP : is)     fr = aux vs is fr
    aux vs           (NOP : is)     fr = aux vs is fr
    aux (v : vs)     (UOP f : is)   fr = aux (f v : vs) is fr

testDUP :: Code
testDUP = [PUSH 1, DUP, STORE 0, RETURN]

testSWAP :: Code
testSWAP = [PUSH 1, PUSH 2, SWAP, STORE 0, RETURN]

testPOP :: Code
testPOP = [PUSH 1, PUSH 2, POP, RETURN]

testNOP :: Code
testNOP = [PUSH 1, NOP, RETURN]

testUOP :: Code
testUOP = [PUSH 1, UOP (\ v -> - v), RETURN]