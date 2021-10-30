-- Es1
data ForseInt = Niente | Proprio Int
  deriving Show

testa :: [Int] -> ForseInt
testa [] = Niente
testa (x : _) = Proprio x

-- Es2
data Numero = I Int | F Float
  deriving Show

somma :: Numero -> Numero -> Numero
somma (I n) (I m) = I (n + m)
somma (F f) (I n) = F (f + fromIntegral n)
somma (I n) (F f) = F (fromIntegral n + f)
somma (F f) (F g) = F (f + g)

-- Es3
sommatoria :: [Numero] -> Numero
sommatoria = foldr somma (I 0)

-- Es4
proprio :: [ForseInt] -> [Int]
proprio []                = []
proprio (Niente : xs)    = proprio xs
proprio (Proprio x : xs) = x : proprio xs