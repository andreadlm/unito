module plfa.Relations where

import Relation.Binary.PropositionalEquality as Eq
open Eq using (_≡_; refl; cong)
open import Data.Nat using (ℕ; zero; suc; _+_)
open import Data.Nat.Properties using (+-comm; +-identityʳ)

data _≤_ : ℕ → ℕ → Set where
  z≤n : ∀ {n : ℕ}
      ----------
      → zero ≤ n
  s≤s : ∀ {m n : ℕ} 
      → m ≤ n 
      ---------------
      → suc m ≤ suc n

infix 4 _≤_

_ : 2 ≤ 4
_ = s≤s {1} {3} (s≤s {0} {2} (z≤n {2}))

inv-s≤s : ∀ {m n : ℕ}
        → suc m ≤ suc n
          -------------
        → m ≤ n
inv-s≤s (s≤s u) = u

inv-z≤n : ∀ {m : ℕ}
        → m ≤ zero
          --------
        → m ≡ zero
inv-z≤n z≤n = refl

≤-refl : ∀ {n : ℕ}
         -----
       → n ≤ n 
≤-refl {zero}  = z≤n
≤-refl {suc n} = s≤s ≤-refl

≤-trans : ∀ {m n p : ℕ}
        → m ≤ n
        → n ≤ p
          -----
        → m ≤ p
≤-trans z≤n _           = z≤n
≤-trans (s≤s m≤n) (s≤s n≤p) = s≤s (≤-trans m≤n n≤p)

≤-antisym : ∀ {m n : ℕ}
          → m ≤ n
          → n ≤ m
          → m ≡ n
≤-antisym z≤n       z≤n       = refl
≤-antisym (s≤s m≤n) (s≤s n≤m) = cong (suc) (≤-antisym m≤n n≤m)

data Total (m n : ℕ) : Set where
  forward : m ≤ n
            ---------
          → Total m n

  flipped : n ≤ m
            ---------
          → Total m n

≤-total : ∀ (m n : ℕ) → Total m n
≤-total zero    n       = forward z≤n
≤-total (suc m) zero    = flipped z≤n
≤-total (suc m) (suc n) = helper(≤-total m n)
  where
    helper : Total m n → Total (suc m) (suc n)
    helper (forward m≤n) = forward (s≤s m≤n)
    helper (flipped m≤n) = flipped (s≤s m≤n)

infix 4 _<_

data _<_ : ℕ → ℕ → Set where
  z<s : ∀ {n : ℕ}
        ------------
      → zero < suc n

  s<s : ∀ {m n : ℕ}
      → m < n
        -------------
      → suc m < suc n

<-trans : ∀ {m n p : ℕ}
        → m < n
        → n < p
        → m < p
<-trans z<s       (s<s _)   = z<s
<-trans (s<s m<n) (s<s n<p) = s<s (<-trans m<n n<p)

data Trichotomy (m n : ℕ) : Set where
  lower   : m < n
            --------------
          → Trichotomy m n
  greater : n < m -- m > n
            --------------
          → Trichotomy m n
  equal   : m ≡ n
            --------------
          → Trichotomy m n

<-trichotomy : ∀ (m n : ℕ) → Trichotomy m n
<-trichotomy zero zero    = equal refl
<-trichotomy zero (suc n) = lower z<s
<-trichotomy (suc m) zero = greater z<s
<-trichotomy (suc m) (suc n) = helper( <-trichotomy m n)
  where
    helper : Trichotomy m n → Trichotomy (suc m) (suc n)
    helper (lower m<n) = lower (s<s m<n)
    helper (greater n<m) = greater (s<s n<m)
    helper (equal m≡n) = equal (cong (suc) m≡n)
  
<-if-≤ : ∀ {m n : ℕ}
       → suc m ≤ n 
       → m < n
<-if-≤ (s≤s z≤n)       = z<s
<-if-≤ (s≤s (s≤s m≤n)) = s<s (<-if-≤ (s≤s m≤n))

≤-if-< : ∀ {m n : ℕ}
       → m < n
       → suc m ≤ n
≤-if-< z<s       = s≤s z≤n
≤-if-< (s<s m<n) = s≤s (≤-if-< m<n)