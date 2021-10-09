scambia :: (Int, Int) -> (Int, Int)
scambia (x , y) = (y , x)

ordina :: (Int, Int, Int) -> (Int, Int, Int)
ordina (a, b, c) | a > b = ordina (b, a, c)
                 | b > c = ordina (a, c, b)
                 | otherwise = (a, b, c)

sommaNumC :: (Int, Int) -> (Int, Int) -> (Int, Int)
sommaNumC (a, b) (c, d) = (a + c, b + d)

negaNumC :: (Int, Int) -> (Int, Int)
negaNumC (a, b) = (negate a, b)

sottraiNumC :: (Int, Int) -> (Int, Int) -> (Int, Int)
sottraiNumC (a, b) (c , d) = (a - c, b - d)

coniugaNumC :: (Int, Int) -> (Int, Int)
coniugaNumC (a, b) = (a, negate b)