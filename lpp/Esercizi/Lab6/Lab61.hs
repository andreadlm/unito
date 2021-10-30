-- Es1
data PuntoCardinale = Nord | Sud | Est | Ovest

sinistra :: PuntoCardinale -> PuntoCardinale
sinistra Nord = Est
sinistra Sud = Ovest
sinistra Est = Sud
sinistra Ovest = Nord

indietro :: PuntoCardinale -> PuntoCardinale
indietro = sinistra . sinistra

destra :: PuntoCardinale -> PuntoCardinale
destra = indietro . sinistra

--Es2
data Giorno = Lun | Mar | Mer | Gio | Ven | Sab | Dom
  deriving Show

domani :: Giorno -> Giorno
domani Lun = Mar
domani Mar = Mer
domani Mer = Gio
domani Gio = Ven
domani Ven = Sab
domani Sab = Dom
domani Dom = Lun

fra :: Int -> Giorno -> Giorno
fra n gg | n == 0    = gg
         | otherwise = fra (n - 1) (domani gg)

fraRep :: Int -> Giorno -> Giorno
fraRep n = foldr (.) id (replicate n domani)