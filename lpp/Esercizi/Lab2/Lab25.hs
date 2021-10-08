-- public static int fattoriale(int n) {
--  assert n >= 0;
--  int res = 1;
--  while (n > 0) {
--     res = res * n;
--     n = n - 1;
--  }
--  return res;
-- }

fattoriale :: Int -> Int 
fattoriale = fattorialeAux 1
    where 
        fattorialeAux res 0 = res
        fattorialeAux res n = fattorialeAux (res * n) (n - 1)

-- public static int bits(int n) {
--    assert n >= 0;
--    int bits = 0;
--    while (n > 0) {
--        bits = bits + n % 2;
--        n = n / 2;
--    }
--    return bits;
-- }

bits :: Int -> Int 
bits = bitsAux 0
    where
        bitsAux bits 0 = bits
        bitsAux bits n = bitsAux (bits + n `mod` 2) (n `div` 2)

-- public static int euclide(int m, int n) {
--     assert m > 0 && n > 0;
--     while (m != n)
--         if (m < n) n -= m; else m -= n;
--     return n;
-- }

euclide :: Int -> Int -> Int
euclide m n | m == n = n
            | m < n = euclide m (n - m)
            | otherwise = euclide (m - n) n