CREATE TYPE tipoMedico AS ENUM ('Base', 'Specialistico');

CREATE TYPE tipoVaccino AS ENUM('Monodose', 'Multidose');

CREATE TYPE tipoPrenotazione AS ENUM('Web', 'App');

CREATE DOMAIN tipoEta AS numeric(3)
    DEFAULT NULL
    CHECK(VALUE >= 0 AND VALUE <= 150);

CREATE TABLE CITTA(
    Codice char(4),
    Nome varchar(35) NOT NULL,
    CONSTRAINT Pk_CITTA PRIMARY KEY(Codice)
);

CREATE TABLE CITTADINO(
    CF char(16),
    Nome varchar(50) NOT NULL,
    Cognome varchar(50) NOT NULL,
    QualificatoreIndirizzo varchar(10) NOT NULL,
    ViaIndirizzo varchar(20) NOT NULL,
    CivicoIndirizzo numeric(4) NOT NULL,
    CodiceCittaResidenza char(4) NOT NULL,
    Eta tipoEta,
    CONSTRAINT Pk_CITTADINO PRIMARY KEY(CF),
    CONSTRAINT Fk_CITTA FOREIGN KEY(CodiceCittaResidenza)
        REFERENCES CITTA(Codice)
        ON UPDATE CASCADE
);

CREATE TABLE CENTRO_VACCINALE(
    Codice char(5),
    CodiceCitta char(4) NOT NULL,
    QualificatoreIndirizzo varchar(10) NOT NULL,
    ViaIndirizzo varchar(20) NOT NULL,
    CivicoIndirizzo numeric(4) NOT NULL,
    CONSTRAINT Pk_CENTRO_VACCINALE PRIMARY KEY(Codice),
    CONSTRAINT Fk_CITTA FOREIGN KEY(CodiceCitta)
        REFERENCES CITTA(Codice)
        ON UPDATE CASCADE,
    CONSTRAINT CittaIndirizzoUnivoci
        UNIQUE(CodiceCitta,
               QualificatoreIndirizzo,
               ViaIndirizzo,
               CivicoIndirizzo)
);

CREATE TABLE MEDICO(
    CF char(16),
    Tipo tipoMedico NOT NULL,
    CodiceCentroVaccinale char(5),
    CONSTRAINT Pk_MEDICO PRIMARY KEY(CF),
    CONSTRAINT Fk_CITTADINO FOREIGN KEY(CF)
        REFERENCES CITTADINO(CF)
        ON UPDATE CASCADE,
    CONSTRAINT Fk_CENTRO_VACCINALE FOREIGN KEY(CodiceCentroVaccinale)
        REFERENCES CENTRO_VACCINALE(Codice)
        ON UPDATE CASCADE
);

CREATE TABLE VACCINO(
    Nome varchar(20),
    Efficacia decimal(4,2) NOT NULL,
    EtaMinima tipoEta NOT NULL,
    EtaMassima tipoEta NOT NULL,
    Intervallo numeric(3),
    Tipo tipoVaccino NOT NULL,
    CONSTRAINT Pk_VACCINO PRIMARY KEY(Nome),
    CONSTRAINT ValiditaIntervalloEta CHECK(EtaMinima <= EtaMassima),
    CONSTRAINT EfficaciaPositiva CHECK(Efficacia >= 0.0),
    CONSTRAINT IntervalloMultidose CHECK((Tipo = 'Multidose' AND Intervallo IS NOT NULL) OR
                                         (Tipo = 'Monodose' AND Intervallo IS NULL))
);

CREATE TABLE CATEGORIA(
    Nome varchar(20),
    CONSTRAINT Pk_CATEGORIA PRIMARY KEY(Nome)
);

CREATE TABLE ALLERGENE(
    Nome varchar(20),
    CONSTRAINT Pk_ALLERGENE PRIMARY KEY(Nome)
);

CREATE TABLE PRENOTAZIONE(
    Codice serial,
    CFCittadino char(16) NOT NULL,
    Tipo tipoPrenotazione NOT NULL,
    NumeroDiCellulare varchar(14),
    IndirizzoEmail varchar(254),
    PositivitaPregressa boolean NOT NULL,
    NomeCategoria varchar(20) NOT NULL,
    VaccinoIniettato varchar(20),
    CodiceCentroVaccinale char(5),
    CONSTRAINT Pk_PRENOTAZIONE PRIMARY KEY(Codice),
    CONSTRAINT Fk_CITTADINO FOREIGN KEY(CFCittadino)
        REFERENCES CITTADINO(CF)
        ON UPDATE CASCADE,
    CONSTRAINT Fk_CATEGORIA FOREIGN KEY(NomeCategoria)
        REFERENCES CATEGORIA(Nome)
        ON UPDATE CASCADE,
    CONSTRAINT Fk_VACCINO FOREIGN KEY(VaccinoIniettato)
        REFERENCES VACCINO(Nome)
        ON UPDATE CASCADE,
    CONSTRAINT Fk_CENTRO_VACCINALE FOREIGN KEY(CodiceCentroVaccinale)
        REFERENCES CENTRO_VACCINALE(Codice)
        ON UPDATE CASCADE,
    CONSTRAINT CFUnivoco UNIQUE(CFCittadino),
    CONSTRAINT ValiditaRecapiti CHECK((Tipo = 'Web' AND
                                       IndirizzoEmail IS NOT NULL AND
                                       NumeroDiCellulare IS NULL) OR
                                      (Tipo = 'App' AND
                                       NumeroDiCellulare IS NOT NULL AND
                                       IndirizzoEmail IS NULL))
);

CREATE TABLE DICHIARAZIONE(
    CodicePrenotazione integer,
    NomeAllergene varchar(20),
    CONSTRAINT Pk_DICHIARAZIONE PRIMARY KEY(CodicePrenotazione, NomeAllergene),
    CONSTRAINT Fk_PRENOTAZIONE FOREIGN KEY(CodicePrenotazione)
        REFERENCES PRENOTAZIONE(Codice)
        ON UPDATE CASCADE,
    CONSTRAINT Fk_ALLERGENE FOREIGN KEY(NomeAllergene)
        REFERENCES ALLERGENE(Nome)
        ON UPDATE CASCADE
);

CREATE TABLE PRESENZA(
    NomeVaccino varchar(20),
    NomeAllergene varchar(20),
    CONSTRAINT Pk_PRESENZA PRIMARY KEY(NomeVaccino, NomeAllergene),
    CONSTRAINT Fk_VACCINO FOREIGN KEY(NomeVaccino)
        REFERENCES VACCINO(Nome)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT Fk_ALLERGENE FOREIGN KEY(NomeAllergene)
        REFERENCES ALLERGENE(Nome)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE LOTTO(
    NumeroLotto char(7),
    NomeVaccino varchar(20),
    DataDiProduzione date NOT NULL,
    DataDiScadenza date NOT NULL,
    CONSTRAINT Pk_LOTTO PRIMARY KEY(NumeroLotto, NomeVaccino),
    CONSTRAINT Fk_VACCINO FOREIGN KEY(NomeVaccino)
        REFERENCES VACCINO(Nome)
        ON UPDATE CASCADE,
    CONSTRAINT ValiditaIntervalloData CHECK(DataDiProduzione < DataDiScadenza)
);

CREATE TABLE MAGAZZINO(
    NumeroLotto char(7),
    NomeVaccinoLotto varchar(20),
    CodiceCentroVaccinale char(5),
    NumeroDosi decimal(5, 0) NOT NULL,
    CONSTRAINT Pk_MAGAZZINO PRIMARY KEY(NumeroLotto, NomeVaccinoLotto, CodiceCentroVaccinale),
    CONSTRAINT Fk_LOTTO FOREIGN KEY(NumeroLotto, NomeVaccinoLotto)
        REFERENCES LOTTO(NumeroLotto, NomeVaccino)
        ON UPDATE CASCADE,
    CONSTRAINT Fk_CENTRO_VACCINALE FOREIGN KEY(CodiceCentroVaccinale)
        REFERENCES CENTRO_VACCINALE(Codice)
        ON UPDATE CASCADE,
    CONSTRAINT RangeNumeroDosi CHECK(NumeroDosi >= 0)
);

CREATE TABLE SEGNALAZIONE(
    CodicePrenotazione integer,
    Data date,
    NumeroLotto char(7) NOT NULL,
    NomeVaccinoLotto varchar(20) NOT NULL,
    CONSTRAINT Pk_SEGNALAZIONE PRIMARY KEY(CodicePrenotazione, Data),
    CONSTRAINT Fk_PRENOTAZIONE FOREIGN KEY(CodicePrenotazione)
        REFERENCES PRENOTAZIONE(Codice)
        ON UPDATE CASCADE,
    CONSTRAINT Fk_LOTTO FOREIGN KEY(NumeroLotto, NomeVaccinoLotto)
        REFERENCES LOTTO(NumeroLotto, NomeVaccino)
        ON UPDATE CASCADE
);

CREATE TABLE SOMMINISTRAZIONE(
    CodicePrenotazione integer,
    Data date,
    Ora time NOT NULL,
    CFMedico char(16),
    CONSTRAINT Pk_SOMMINISTRAZIONE PRIMARY KEY(CodicePrenotazione, Data),
    CONSTRAINT Fk_PRENOTAZIONE FOREIGN KEY(CodicePrenotazione)
        REFERENCES PRENOTAZIONE(Codice)
        ON UPDATE CASCADE
);

CREATE TABLE UTILIZZO(
    NomeCategoria varchar(20),
    NomeVaccino varchar(20),
    CONSTRAINT Pk_UTILIZZO PRIMARY KEY(NomeCategoria, NomeVaccino),
    CONSTRAINT Fk_CATEGORIA FOREIGN KEY(NomeCategoria)
        REFERENCES CATEGORIA(Nome)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT Fk_VACCINO FOREIGN KEY(NomeVaccino)
        REFERENCES VACCINO(Nome)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);