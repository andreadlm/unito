module Bin where

open import Naturals

import Relation.Binary.PropositionalEquality as Eq
open Eq using (_≡_; refl)
open Eq.≡-Reasoning using (begin_; _≡⟨⟩_; _∎)

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