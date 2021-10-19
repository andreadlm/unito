module plfa.Bin where

import Relation.Binary.PropositionalEquality as Eq
open Eq using (_≡_; refl; cong; sym)
open Eq.≡-Reasoning using (begin_; _≡⟨⟩_; step-≡; _∎)
open import Data.Nat using (ℕ; zero; suc; _+_; _*_; _∸_)

open import plfa.Induction

data Bin : Set where
  ⟨⟩ : Bin
  _O : Bin → Bin
  _I : Bin → Bin

inc : Bin → Bin
inc ⟨⟩    = ⟨⟩ I
inc (b O) = b I
inc (b I) = inc b O

_ : inc (⟨⟩ I O I I) ≡ ⟨⟩ I I O O
_ = 
  begin
    inc (⟨⟩ I O I I)
  ≡⟨⟩
    inc (⟨⟩ I O I) O
  ≡⟨⟩
    inc (⟨⟩ I O) O O
  ≡⟨⟩
    ⟨⟩ I I O O
  ∎

to : ℕ → Bin
to 0       = ⟨⟩
to (suc n) = inc (to n)

from : Bin → ℕ
from ⟨⟩    = 0
from (b O) = 2 * (from b)
from (b I) = 2 * (from b) + 1

law₁-lemma : ∀ (n : ℕ) → suc n ≡ n + 1
law₁-lemma zero =
  begin
    suc zero
  ≡⟨⟩
    zero + suc zero
  ≡⟨⟩
    zero + 1
  ∎
law₁-lemma (suc m) =
  begin
    suc (suc m)
  ≡⟨ cong suc (law₁-lemma m) ⟩
    suc (m + 1)
  ≡⟨⟩
    (suc m) + 1
  ∎

law₁ : ∀ (b : Bin) → from (inc b) ≡ suc (from b)
law₁ ⟨⟩ = 
  begin
    from (inc ⟨⟩)
  ≡⟨⟩
    from (⟨⟩ I)
  ≡⟨⟩
    2 * (from ⟨⟩) + 1
  ≡⟨⟩
    suc (2 * from ⟨⟩)
  ≡⟨⟩
    suc (2 * zero)
  ≡⟨⟩
    suc zero
  ≡⟨⟩
    suc (from ⟨⟩)
  ∎
law₁ (b O) =
  begin
    from (inc (b O))
  ≡⟨⟩
    from (b I)
  ≡⟨⟩
    2 * (from b) + 1
  ≡⟨⟩
    from (b O) + 1
  ≡⟨⟩
    from (b O) + suc zero
  ≡⟨ +-comm (from (b O)) (suc zero) ⟩
    suc zero + from (b O)
  ≡⟨⟩
    suc (zero + from (b O))
  ≡⟨⟩
    suc (from (b O))
  ∎

-- Completare