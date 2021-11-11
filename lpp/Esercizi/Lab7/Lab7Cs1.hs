data Tree a = Leaf | Branch a (Tree a) (Tree a)
  deriving Show

empty :: Tree a -> Bool
empty Leaf = True
empty _    = False

depth :: Tree a -> Int 
depth Leaf             = 0
depth (Branch _ t₁ t₂) = 1 + max (depth t₁) (depth t₂)

nElements :: Tree a -> Int
nElements Leaf             = 0
nElements (Branch _ t₁ t₂) = 1 + nElements t₁ + nElements t₂

elements :: Tree a -> [a]
elements Leaf             = []
elements (Branch x t1 t2) = elements t1 ++ [x] ++ elements t2

tmax :: Tree a -> a
tmax (Branch x _ Leaf) = x
tmax (Branch _ _ t)    = tmax t

tmin :: Tree a -> a
tmin (Branch x Leaf _) = x
tmin (Branch _ t _)    = tmin t

tmin_ :: Tree a -> Maybe a
tmin_ Leaf = Nothing
tmin_ (Branch x t _) = case tmin_ t of
                        Nothing -> Just x
                        Just y  -> Just y

insert :: Ord a => a -> Tree a -> Tree a
insert x Leaf = Branch x Leaf Leaf
insert x t@(Branch y t₁ t₂) | x == y    = t
                            | x < y     = Branch y (insert x t₁) t₂
                            | otherwise = Branch y t₁ (insert x t₂)

treeSort :: Ord a => [a] -> [a]
treeSort = elements . foldr insert Leaf 

{-
treeSort :: Ord a => [a] -> [a]
treeSort = elements . aux Leaf
  where
    aux t [] = t
    aux t (x : xs) = aux (insert x t) xs
-}

bst :: Ord a => Tree a -> Bool
bst Leaf             = True
bst (Branch x t₁ t₂) = bst t₁ && bst t₂ &&
                        (empty t₁ || tmax t₁ < x) &&
                        (empty t₂ || tmin t₂ > x)