{- Tipi per IMP: preservazione e progresso

   Riferimento: Nipkow, Klein, Concrete Semantics 
                cap. 9 par. 9.1

   Codifica in Agda a cura di Andrea Laretto

   Soluzioni degli esercizi di Andrea Delmastro

-}

module MFI-Types-es where

open import Data.Nat     using (ℕ) renaming (_+_ to _+ᵢ_; _≤?_ to _≤?ᵢ_)
open import Agda.Builtin.Float renaming (primFloatPlus to _+ᵣ_; primFloatLess to _≤?ᵣ_)
open import Data.Bool    using (Bool; true; false; not; _∧_)
open import Data.Sum     using (inj₁; inj₂; _⊎_)
open import Data.Product using (_×_; _,_; -,_; _-,-_; ∃; ∃-syntax; proj₂)
open import Data.String  using (String; _≟_)
open import Relation.Nullary           using (¬_; yes; no)
open import Relation.Nullary.Decidable using (⌊_⌋)
open import Relation.Nullary.Negation  using (contradiction)
open import Relation.Binary.PropositionalEquality using (_≡_; refl; sym)
open Relation.Binary.PropositionalEquality.≡-Reasoning

{- Scopo di questo modulo è illustrare l'uso della nozione di tipo
   per garantire proprietà di safety, ossia:

   se c è un comando ben tipato che viene eseguito in uno stato
   coerente con le dichiarazioni dei tipi delle sue variabili
   allora l'esecuzione non si blocca prima di raggiungere un valore
   (se esiste)

-}

-- Estendiamo la nozione di valore ponendo val = ℕ ∪ ℝ

vname = String
int   = ℕ
real  = Float
bool  = Bool

data val : Set where
  Iv : int  → val
  Rv : real → val

-- Di conseguenza estendiamo la sintassi di IMP:

data aexp : Set where
  Ic   : int   → aexp
  Rc   : real  → aexp
  V    : vname → aexp
  Plus : aexp  → aexp → aexp

data bexp : Set where
  Bc   : Bool → bexp
  Not  : bexp → bexp
  And  : bexp → bexp → bexp
  Less : aexp → aexp → bexp
  
data com : Set where
  SKIP  : com
  _::=_ : String → aexp → com
  _::_  : com → com → com
  IF_THEN_ELSE_ : bexp → com → com → com
  WHILE_DO_     : bexp → com → com

{- Assumendo che non sia corretto sommare o confrontare
   un intero con un reale, ridefiniamo la valutazione
   delle espressioni attraverso predicati induttivi:
-}

state = vname → val

data taval : aexp → state → val → Set where    -- Nipkow Fig. 9.1

  tavalI : ∀{i s}
           ---------------------
         → taval (Ic i) s (Iv i)
         
  tavalR : ∀{r s}
           ---------------------
         → taval (Rc r) s (Rv r)
         
  tavalV : ∀{x s}
           -------------------
         → taval (V x) s (s x)
         
  tavalSI : ∀{s a₁ a₂ i₁ i₂}
          → taval a₁ s (Iv i₁)
          → taval a₂ s (Iv i₂)
            ------------------------------------
          → taval (Plus a₁ a₂) s (Iv (i₁ +ᵢ i₂))
          
  tavalSR : ∀{s a₁ a₂ r₁ r₂}
          → taval a₁ s (Rv r₁)
          → taval a₂ s (Rv r₂)
            ------------------------------------
          → taval (Plus a₁ a₂) s (Rv (r₁ +ᵣ r₂))

data tbval : bexp → state → bool → Set where

  tbvalC : ∀{s v}
           ----------------
         → tbval (Bc v) s v
         
  tbvalN : ∀{s b bv}
         → tbval b s bv
           ------------------------
         → tbval (Not b) s (not bv)
         
  tbvalA : ∀{s b₁ b₂ bv₁ bv₂}
         → tbval b₁ s bv₁
         → tbval b₂ s bv₂
           -------------------------------
         → tbval (And b₁ b₂) s (bv₁ ∧ bv₂)

  tbvalLI : ∀{s a₁ a₂ i₁ i₂}
         → taval a₁ s (Iv i₁)
         → taval a₂ s (Iv i₂)
           ------------------------------------
         → tbval (Less a₁ a₂) s (⌊ i₁ ≤?ᵢ i₂ ⌋)
         
  tbvalLR : ∀{s a₁ a₂ r₁ r₂}
         → taval a₁ s (Rv r₁)
         → taval a₂ s (Rv r₂)
           --------------------------------
         → tbval (Less a₁ a₂) s (r₁ ≤?ᵣ r₂)

------------------------------
-- Semantica operazionale
------------------------------

_[_::=_] : state → vname → val → state
(s [ X ::= n ]) Y with Y ≟ X
... | yes _ = n
... | no  _ = s Y

data ⦅_,_⦆→⦅_,_⦆ : com → state → com → state → Set where

  Loc : ∀{x a s v}
      → taval a s v
      → ⦅ x ::= a , s ⦆→⦅ SKIP , s [ x ::= v ] ⦆
      
  Comp₁ : ∀{c s}
      → ⦅ SKIP :: c , s ⦆→⦅ c , s ⦆
      
  Comp₂ : ∀{c₁ c₁′ c₂ s s′}
     →  ⦅ c₁       , s ⦆→⦅ c₁′       , s′ ⦆
        ----------------------------------
     →  ⦅ c₁ :: c₂ , s ⦆→⦅ c₁′ :: c₂ , s′ ⦆
     
  IfTrue  : ∀{b s c₁ c₂}
          → tbval b s true
            --------------------------------------
          → ⦅ IF b THEN c₁ ELSE c₂ , s ⦆→⦅ c₁ , s ⦆
          
  IfFalse : ∀{b s c₁ c₂}
          → tbval b s false
            --------------------------------------
          → ⦅ IF b THEN c₁ ELSE c₂ , s ⦆→⦅ c₂ , s ⦆
          
  While : ∀{b s c}
          ---------------------------------------------------------------------
        → ⦅ WHILE b DO c , s ⦆→⦅ IF b THEN (c :: (WHILE b DO c)) ELSE SKIP , s ⦆


data  ⦅_,_⦆→*⦅_,_⦆ : com → state → com → state → Set where

  Ref : ∀{c s} → ⦅ c , s ⦆→*⦅ c , s ⦆
  
  Step : ∀{c c′ c² s s′ s²}
       → ⦅ c  , s  ⦆→⦅ c′ , s′ ⦆
       → ⦅ c′ , s′ ⦆→*⦅ c² , s² ⦆
         -----------------------
       → ⦅ c  , s  ⦆→*⦅ c² , s² ⦆

trans : ∀{c c′ c² s s′ s²}
      → ⦅ c  , s  ⦆→*⦅ c′ , s′ ⦆
      → ⦅ c′ , s′ ⦆→*⦅ c² , s² ⦆
        -----------------------
      → ⦅ c  , s  ⦆→*⦅ c² , s² ⦆
      
-- Dim.      
trans Ref b = b
trans (Step x a) b = Step x (trans a b) 

--------------------------
-- Sistema di tipo per IMP
--------------------------

-- Introduciamo i tipi:

-- Ity per le espressioni che hanno valore int
-- Rty per le espressioni che hanno valore real

data ty : Set where
  Ity : ty
  Rty : ty

-- Definiamo un ambiente (contesto) Γ come una mappa dalle var. ai tipi

tyenv = vname → ty

-- Giudizi:

-- Γ ⊢ₐ a :: τ   a ∈ aexp ha tipo τ se le sue var. hanno i tipi in Γ

data _⊢ₐ_∷_ : tyenv → aexp → ty → Set where

  taexpI : ∀{Γ i}
         ---------------
       → Γ ⊢ₐ Ic i ∷ Ity
       
  taexpR : ∀{Γ r}
         ---------------
       → Γ ⊢ₐ Rc r ∷ Rty
       
  taexpV : ∀{Γ x}
         --------------
       → Γ ⊢ₐ V x ∷ Γ x
       
  taexpP : ∀{Γ a₁ a₂ τ}
      → Γ ⊢ₐ a₁ ∷ τ
      → Γ ⊢ₐ a₂ ∷ τ
        -------------------
      → Γ ⊢ₐ Plus a₁ a₂ ∷ τ

-- Γ ⊢₆ b   b ∈ bexp è ben tipato se le sue var. hanno i tipi in Γ

data _⊢₆_ : tyenv → bexp → Set where

  tbexpC : ∀{Γ v}
         ---------
       → Γ ⊢₆ Bc v
       
  tbexpN : ∀{Γ b}
       → Γ ⊢₆ b
         ----------
       → Γ ⊢₆ Not b
       
  tbexpA : ∀{Γ b₁ b₂}
      → Γ ⊢₆ b₁
      → Γ ⊢₆ b₂
        --------------
      → Γ ⊢₆ And b₁ b₂

  tbexpL : ∀{Γ a₁ a₂ τ}
      → Γ ⊢ₐ a₁ ∷ τ
      → Γ ⊢ₐ a₂ ∷ τ
        ---------------
      → Γ ⊢₆ Less a₁ a₂

-- Γ ⊢ c   c ∈ com è ben tipato se le sue variabili hanno i tipi in Γ

data _⊢_ : tyenv → com → Set where

  TSkip : ∀{Γ}
        --------
      → Γ ⊢ SKIP
      
  TLoc : ∀{Γ a x}
      → Γ ⊢ₐ a ∷ Γ x
        -------------      
      → Γ ⊢ (x ::= a)
      
  TSeq : ∀{Γ c₁ c₂}
       → Γ ⊢ c₁
       → Γ ⊢ c₂
         --------------
       → Γ ⊢ (c₁ :: c₂)
       
  TIf : ∀{Γ b c₁ c₂}
      → Γ ⊢₆ b
      → Γ ⊢ c₁
      → Γ ⊢ c₂
        --------------------------
      → Γ ⊢ (IF b THEN c₁ ELSE c₂)
      
  TWhile : ∀{Γ b c}
         → Γ ⊢₆ b
         → Γ ⊢ c
           ------------------
         → Γ ⊢ (WHILE b DO c)

---------------------------------------
-- Preservazione del tipo per riduzione
---------------------------------------

-- associamo tipi ai valori

type : val → ty
type (Iv i) = Ity
type (Rv r) = Rty


-- Γ ⊢ s     s ∈ state rispetta Γ ∈ tyenv se
--           per ogni x ∈ vname il valore che s associa ad x
--           ha tipo Γ x

_⊢ₛ_ : tyenv → state → Set
Γ ⊢ₛ s = ∀ x → type (s x) ≡ Γ x


-- Lemma: se a ∈ aexp ha tipo τ in Γ,  s rispetta Γ
--        e v è il valore di a ∈ Γ, allora v ha tipo τ


preservation-aval : ∀{Γ a s τ v} -- Nipkow 9.2
-- se
    → Γ ⊢ₐ a ∷ τ
    → Γ ⊢ₛ s
    → taval a s v
-- allora
    → type v ≡ τ

---------------------------------------------------
-- Esercizio: dimostrare il lemma preservation-aval
---------------------------------------------------

preservation-aval 
  {τ = .Ity} {v = .(Iv i)} 
  taexpI 
  Γ⊢ₛs 
  (tavalI {i = i}) = 
    begin type (Iv i) ≡⟨⟩ Ity ∎

preservation-aval 
  {τ = .Rty} {v = .(Rv r)} 
  taexpR 
  Γ⊢ₛs 
  (tavalR {r = r}) = 
    begin type (Rv r) ≡⟨⟩ Rty ∎

preservation-aval 
  {Γ = Γ} {a = .(V x)} {τ = .(Γ x)} {v = .(s x)} 
  taexpV 
  Γ⊢ₛs 
  (tavalV {x} {s}) = 
    Γ⊢ₛs x

preservation-aval 
  {τ = τ} {v = .(Iv (i₁ +ᵢ i₂))} 
  (taexpP Γ⊢ₐa₁∷τ Γ⊢ₐa₂∷τ) 
  Γ⊢ₛs 
  (tavalSI {i₁ = i₁} {i₂ = i₂} taval_a₁sv₁ taval_a₂sv₂) = 
    begin
      type (Iv (i₁ +ᵢ i₂))
    ≡⟨⟩
      Ity
    ≡⟨ preservation-aval Γ⊢ₐa₁∷τ Γ⊢ₛs taval_a₁sv₁ ⟩ 
      τ
    ∎

preservation-aval 
  {τ = τ} {v = .(Rv (r₁ +ᵣ r₂))} 
  (taexpP Γ⊢ₐa₁∷τ Γ⊢ₐa₂∷τ) 
  Γ⊢ₛs 
  (tavalSR {r₁ = r₁} {r₂ = r₂} taval_a₁sv₁ taval_a₂sv₂) = 
    begin
      type (Rv (r₁ +ᵣ r₂))
    ≡⟨⟩
      Rty
    ≡⟨ preservation-aval Γ⊢ₐa₁∷τ Γ⊢ₛs taval_a₁sv₁ ⟩ 
      τ
    ∎

-- Lemma: 
extract-ity : ∀ v
-- se
  → type v ≡ Ity
-- allora
  → ∃[ i ] (v ≡ Iv i)

-- Dim.
extract-ity (Iv x) r = x , refl 

-- Lemma:
extract-rty : ∀ v
-- se
  → type v ≡ Rty
-- allora
  → ∃[ i ] (v ≡ Rv i)

-- Dim.
extract-rty (Rv x) r = x , refl


-- Lemma: un'espressione a ∈ aexp di tipo τ in Γ
--        ha un valore definito in ogni s che rispetti Γ

progress-aval : ∀{Γ a s τ} -- Nipkow 9.3
-- se
  → Γ ⊢ₐ a ∷ τ
  → Γ ⊢ₛ s
-- allora
  → ∃[ v ] (taval a s v)    -- una costruzione di queto tipo ha la forma
                            --  <testimone t per v>, <evidenza di "taval a s t">

-- Dim. 
progress-aval taexpI b = -, tavalI
progress-aval taexpR b = -, tavalR
progress-aval taexpV b = -, tavalV
progress-aval {τ = Ity} (taexpP a₁ a₂) b with progress-aval a₁ b | progress-aval a₂ b
... | rv , r  | mv , m with extract-ity rv (preservation-aval a₁ b r)
                          | extract-ity mv (preservation-aval a₂ b m)
... | v1 , e1 | v2 , e2 rewrite e1 | e2 = -, tavalSI r m
progress-aval {τ = Rty} (taexpP a₁ a₂) b with progress-aval a₁ b | progress-aval a₂ b
... | rv , r  | mv , m with extract-rty rv (preservation-aval a₁ b r)
                          | extract-rty mv (preservation-aval a₂ b m)
... | v1 , e1 | v2 , e2 rewrite e1 | e2 = -, tavalSR r m


-- Lemma: un'espressione b ∈ bexp ben tipata in Γ
--        ha un valore definito in ogni s ∈ state
--        che rispetti Γ

progress-bval : ∀{Γ b s}   -- Nipkow 9.4
-- se
  → Γ ⊢₆ b
  → Γ ⊢ₛ s
-- allora
  → ∃[ v ] (tbval b s v)  -- una costruzione di queto tipo ha la forma
                          --  <testimone t per v>, <evidenza di "tbval a b t">

-- Dim.
progress-bval tbexpC x₁ = -, tbvalC
progress-bval (tbexpN x) x₁ = -, tbvalN (proj₂ (progress-bval x x₁))
progress-bval (tbexpA a b) x = -, tbvalA (proj₂ (progress-bval a x))
                                         (proj₂ (progress-bval b x))
progress-bval (tbexpL {τ = Ity} a₁ a₂) b with progress-aval a₁ b | progress-aval a₂ b
... | rv , r  | mv , m with extract-ity rv (preservation-aval a₁ b r)
                          | extract-ity mv (preservation-aval a₂ b m)
... | v1 , e1 | v2 , e2 rewrite e1 | e2 = -, tbvalLI r m
progress-bval (tbexpL {τ = Rty} a₁ a₂) b with progress-aval a₁ b | progress-aval a₂ b
... | rv , r  | mv , m with extract-rty rv (preservation-aval a₁ b r)
                          | extract-rty mv (preservation-aval a₂ b m)
... | v1 , e1 | v2 , e2 rewrite e1 | e2 = -, tbvalLR r m

--------------------------------------------------
-- Teorema di preservazione del tipo per riduzione
-------------------------------------------------

-- Suddiviso nei due enunciati:

preservation-com : ∀{Γ c s c′ s′}     -- Nipkow 9.5
-- se
  → ⦅ c , s ⦆→⦅ c′ , s′ ⦆
  → Γ ⊢ c
-- allora
  → Γ ⊢ c′

---------------------------------------------------------
-- Esercizio: dimostrare il teor. preservation-com

-- Sugg. induzione simultanea su x : ⦅ c , s ⦆→⦅ c′ , s′ ⦆
--                               y : Γ ⊢ c
---------------------------------------------------------

preservation-com 
  {c′ = .SKIP} 
  (Loc taval_asv) 
  (TLoc  Γ⊢ₐa∷Γx) = 
    TSkip

preservation-com 
  {c′ = .c₂} 
  (Comp₁ {c = .c₂})
  (TSeq {c₁ = .SKIP} {c₂ = c₂} Γ⊢c₁ Γ⊢c₂) = 
    Γ⊢c₂

preservation-com 
  {c′ = .(c₁′ :: c₂)} 
  (Comp₂ {c₁ = .c₁} {c₁′ = c₁′} ⦅c₁,s⦆→⦅c₁′,s′⦆) 
  (TSeq {c₁ = c₁} {c₂ = c₂} Γ⊢c₁ Γ⊢c₂) = 
    TSeq (preservation-com ⦅c₁,s⦆→⦅c₁′,s′⦆ Γ⊢c₁) Γ⊢c₂

preservation-com 
  {c′ = .c₁}
  (IfTrue {c₁ = .c₁} {c₂ = .c₂} tbval_bsT) 
  (TIf {c₁ = c₁} {c₂ = c₂} Γ⊢b Γ⊢c₁ Γ⊢c₂) = 
    Γ⊢c₁

preservation-com
  {c′ = .c₂}
  (IfFalse {c₁ = .c₁} {c₂ = .c₂} tbval_bsF) 
  (TIf {c₁ = c₁} {c₂ = c₂} Γ⊢b Γ⊢c₁ Γ⊢c₂) = 
    Γ⊢c₂

preservation-com 
  {c′ = .(IF b THEN (c :: (WHILE b DO c)) ELSE SKIP)} 
  (While {b = .b} {c = .c}) 
  (TWhile {b = b} {c = c} Γ⊢b Γ⊢c) = 
    TIf Γ⊢b (TSeq Γ⊢c (TWhile Γ⊢b Γ⊢c)) TSkip


preservation-state : ∀{Γ c s c′ s′} -- Nipkow 9.6
-- se
  → ⦅ c , s ⦆→⦅ c′ , s′ ⦆
  → Γ ⊢ c
  → Γ ⊢ₛ s
-- allora
  → Γ ⊢ₛ s′

-- Dim.
preservation-state (Loc {x₃} x₁) (TLoc x₂) r x₄ with x₄ ≟ x₃
... | no ¬p = r x₄
... | yes p with preservation-aval x₂ r x₁
... | z rewrite p = z
preservation-state Comp₁ (TSeq c c₁) r = r
preservation-state (Comp₂ d) (TSeq c c₁) r = preservation-state d c r
preservation-state (IfTrue x) (TIf x₁ c c₁) r = r
preservation-state (IfFalse x) (TIf x₁ c c₁) r = r
preservation-state While (TWhile x c) r = r

------------------------------------------------------
-- Teorema di preservazione del tipo (Nipkow 9.5, 9.6)
------------------------------------------------------

preservation : ∀{Γ c s c′ s′}
-- se
  → Γ ⊢  c
  → Γ ⊢ₛ s
  → ⦅ c , s ⦆→⦅ c′ , s′ ⦆
-- allora
  → Γ ⊢ c′ × Γ ⊢ₛ s′    -- tipo abitato dalla coppia x , y dove
                        -- x :  Γ ⊢ c′   e    y :  Γ ⊢ₛ s′
  
-- Dim.
preservation a b c = preservation-com c a , preservation-state c a b


-------------------------------------
-- Teorema del progresso (Nipkow 9.7)
-------------------------------------

-- Lemma:
either-skip : ∀ c → c ≡ SKIP ⊎ ¬ c ≡ SKIP
-- Dim.
either-skip SKIP = inj₁ refl            -- allora c ≡ SKIP
either-skip (x ::= x₁) = inj₂ (λ ())    -- questo ed i seguenti sono
                                        -- casi in cui c ≢ SKIP
either-skip (c :: c₁) = inj₂ (λ ())
either-skip (IF x THEN c ELSE c₁) = inj₂ (λ ())
either-skip (WHILE x DO c) = inj₂ (λ ())



postulate
  progress : ∀{Γ c s}
-- se
    → Γ ⊢  c
    → Γ ⊢ₛ s
    → ¬ c ≡ SKIP
-- allora
    → ∃[ c′ ] (∃[ s′ ] ( ⦅ c , s ⦆→⦅ c′ , s′ ⦆ ))
             -- tipo abitato da una tripla x , y , z dove
             -- x testimone di c′, y testimone di s′,
             -- z : ⦅ c , s ⦆→⦅ x , y ⦆
             
------------------------------------------------------------------------
-- Esercizio: dimostrare il teorema progress

-- Sugg. usare contradiction, progress-aval, either-skip e progress-bval 
------------------------------------------------------------------------


----------------------------------------------------
-- Teorema di cottettezza (Nipkow 9.8)

-- Combina la preservazione del tipo ed il progresso
----------------------------------------------------

type-soundness : ∀{c s c′ s′ Γ}
-- se
  → ⦅ c , s ⦆→*⦅ c′ , s′ ⦆
  → Γ ⊢  c
  → Γ ⊢ₛ s
  → ¬ c′ ≡ SKIP
-- allora
  → ∃[ c″ ] (∃[ s″ ] ( ⦅ c′ , s′ ⦆→⦅ c″ , s″ ⦆ ))


type-soundness Ref c d e =  progress c d e
type-soundness (Step x r) c d e with preservation c d x
... | ct , st = type-soundness r ct st e