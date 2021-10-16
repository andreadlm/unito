module plfa.Induction where

import Relation.Binary.PropositionalEquality as Eq
open Eq using (_≡_; refl; cong; sym)
open Eq.≡-Reasoning using (begin_; _≡⟨⟩_; step-≡; _∎)
open import Data.Nat using (ℕ; zero; suc; _+_; _*_; _∸_)

+-assoc : ∀ (m n p : ℕ) → (m + n) + p ≡ m + (n + p)
+-assoc zero n p =
  begin
    (zero + n) + p
  ≡⟨⟩
    n + p
  ≡⟨⟩
    zero + (n + p)
  ∎
+-assoc (suc m) n p =
  begin
    (suc m + n) + p
  ≡⟨⟩
    suc (m + n) + p
  ≡⟨⟩
    suc ((m + n) + p)
  ≡⟨ cong suc (+-assoc m n p) ⟩
    suc (m + (n + p))
  ≡⟨⟩
    suc m + (n + p)
  ∎

-- Commutativity proof
+-identityᵣ : ∀ (m : ℕ) → m + zero ≡ m
+-identityᵣ zero =
  begin
    zero + zero
  ≡⟨⟩
    zero
  ∎
+-identityᵣ (suc m) =
  begin
    suc m + zero
  ≡⟨⟩
    suc (m + zero)
  ≡⟨ cong suc (+-identityᵣ m) ⟩
    suc m
  ∎

-- Alternatively
+-identityᵣ′ : ∀ (m : ℕ) → m + zero ≡ m
+-identityᵣ′ zero                           = refl
+-identityᵣ′ (suc m) rewrite +-identityᵣ′ m = refl

+-suc : ∀ (m n : ℕ) → m + suc n ≡ suc (m + n)
+-suc zero n = 
  begin
    zero + suc n
  ≡⟨⟩
    suc n
  ≡⟨⟩
    suc (zero + n)
  ∎
+-suc (suc m) n =
  begin
    suc m + suc n
  ≡⟨⟩
    suc (m + suc n)
  ≡⟨ cong suc (+-suc m n) ⟩
    suc (suc (m + n))
  ≡⟨⟩
    suc ((suc m) + n)
  ∎

-- Alternatively
+-suc′ : ∀ (m n : ℕ) → m + suc n ≡ suc (m + n)
+-suc′ zero n                          = refl
+-suc′ (suc m) n  rewrite +-suc′ m n = refl

+-comm : ∀ (m n : ℕ) → m + n ≡ n + m
+-comm m zero =
  begin
    m + zero
  ≡⟨ +-identityᵣ m ⟩
    m
  ∎
+-comm m (suc n) =
  begin
    m + suc n
  ≡⟨ +-suc m n ⟩
    suc (m + n)
  ≡⟨ cong suc (+-comm m n) ⟩
    suc (n + m)
  ≡⟨⟩
    suc n + m
  ∎

-- Alternatively
+-comm′ : ∀ (m n : ℕ) → m + n ≡ n + m
+-comm′ m zero rewrite +-identityᵣ m             = refl
+-comm′ m (suc n) rewrite +-suc m n | +-comm m n = refl

+-assoc′ : ∀ (m n p : ℕ) → (m + n) + p ≡ m + (n + p)
+-assoc′ zero n p                            = refl
+-assoc′ (suc m) n p  rewrite +-assoc′ m n p = refl

+-swap : ∀ (m n p : ℕ) → m + (n + p) ≡ n + (m + p)
+-swap m n p = 
  begin
    m + (n + p)
  ≡⟨ +-comm m (n + p) ⟩
    (n + p) + m
  ≡⟨ +-assoc′ n p m ⟩
    n + (p + m)
  ≡⟨ cong (n +_) (+-comm p m) ⟩
    n + (m + p)
  ∎

-- Alternatively
+-swap′ : ∀ (m n p : ℕ) → m + (n + p) ≡ n + (m + p)
+-swap′ m n p rewrite +-comm m (n + p) | +-assoc′ n p m | +-comm p m = refl
    
*-distrib-+ : ∀ (m n p :  ℕ) → (m + n) * p ≡ m * p + n * p
*-distrib-+ zero n p =
  begin
    (zero + n) * p
  ≡⟨⟩
    n * p
  ≡⟨⟩
    zero * p + n * p
  ∎
*-distrib-+ (suc m) n p =
  begin
    (suc m + n) * p
  ≡⟨⟩
    suc(m + n) * p
  ≡⟨⟩
    p + ((m + n) * p)
  ≡⟨ cong ( p +_) (*-distrib-+ m n p) ⟩
    p + (m * p + n * p)
  ≡⟨ sym (+-assoc p (m * p) (n * p)) ⟩
    (p + m * p) + n * p
  ≡⟨⟩
    (suc m) * p + n * p
  ∎

*-assoc : ∀ (m n p : ℕ) → m * (n * p) ≡ (m * n) * p
*-assoc zero n p =
  begin
    zero * (n * p)
  ≡⟨⟩
    zero
  ≡⟨⟩
    zero * p
  ≡⟨⟩
    (zero * n) * p
  ∎
*-assoc (suc m) n p =
  begin
    suc m * (n * p)
  ≡⟨⟩
    (n * p) + m * (n * p)
  ≡⟨ cong ((n * p) +_) (*-assoc m n p) ⟩
    (n * p) + ((m * n) * p)
  ≡⟨ sym (*-distrib-+ n (m * n) p) ⟩
    (n + m * n) * p
  ≡⟨⟩
    (suc m * n) * p
  ∎
    


    