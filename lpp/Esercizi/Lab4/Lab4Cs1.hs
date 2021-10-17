_foldl :: (a -> b -> a) -> a -> [b] -> a
_foldl _ x []       = x
_foldl f x (y : ys) = _foldl f (f x y) ys

_concat :: [[a]] -> [a]
_concat = foldl (++) []

_any :: (a -> Bool) -> [a] -> Bool
_any p = not . null . map p

_all :: (a -> Bool) -> [a] -> Bool
_all p = foldr (&&) True . map p

massimo :: Ord a => [a] -> a
massimo (x : xs) = foldr (max) x xs

occorrenze :: Eq a => a -> [a] -> Int
occorrenze x = length . filter (== x)

membro :: Eq a => a -> [a] -> Bool
membro x = any (== x)