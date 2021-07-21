INSERT INTO CATEGORIA VALUES('Scolastico'),
                            ('Sanitario'),
                            ('Immunodepresso');

INSERT INTO CITTA VALUES('B101', 'Boves'),
                        ('D205', 'Cuneo'),
                        ('B033', 'Borgo San Dalmazzo');

INSERT INTO VACCINO VALUES('COVIDIN', 92.5, 16, 65, NULL, 'Monodose'),
                          ('CORONAX', 86.5, 18, 85, 28, 'Multidose'),
                          ('FLUSTOP', 88.75, 18, 70, 35, 'Multidose');

INSERT INTO LOTTO VALUES('AX10655', 'COVIDIN', '2021-01-05', '2022-01-05'),
                        ('AX10775', 'COVIDIN', '2021-01-08', '2022-01-08'),
                        ('RT7C8A9', 'CORONAX', '2021-02-06', '2021-08-06'),
                        ('FT001GH', 'FLUSTOP', '2021-01-17', '2022-09-17');

INSERT INTO UTILIZZO VALUES('Scolastico', 'COVIDIN'),
                           ('Scolastico', 'CORONAX'),
                           ('Sanitario', 'COVIDIN'),
                           ('Sanitario', 'CORONAX'),
                           ('Immunodepresso', 'CORONAX'),
                           ('Immunodepresso', 'FLUSTOP');

INSERT INTO CITTADINO VALUES('MRCRSS61S13D205O', 'Marco', 'Rossi', 'via', 'Roma', 26, 'D205', 59),
                            ('BNCMRA80D45D205Y', 'Bianchi', 'Maria', 'largo', 'Argentera', 34, 'B033', 41),
                            ('MRCPLG80A01D205C', 'Marco', 'Pellegrino', 'piazza', 'Italia', 29, 'B101', 41),
                            ('GDUGDN71D03F351F', 'Guido', 'Giordano', 'corso', 'Bisalta', 13, 'B101', 50),
                            ('FDRRSS77A01B111U', 'Federico', 'Rosso', 'viale', 'angeli', 24, 'D205', 34);

INSERT INTO CENTRO_VACCINALE VALUES('CN001', 'D205', 'corso', 'Monviso', 4),
                                   ('CN002', 'D205', 'via', 'Gimmi Curreno', 10),
                                   ('CN003', 'B101', 'via', 'Castello di Godego', 8),
                                   ('CN004', 'B033', 'via', 'Vittorio Veneto', 19);

INSERT INTO PRENOTAZIONE VALUES(DEFAULT, 'MRCPLG80A01D205C', 'Web', NULL, 'marco.pellegrino70@gmail.com', TRUE, 'Scolastico', NULL, 'CN002'),
                               (DEFAULT, 'MRCRSS61S13D205O', 'App', '3289750643', NULL, FALSE, 'Immunodepresso', 'CORONAX', 'CN001'),
                               (DEFAULT, 'GDUGDN71D03F351F', 'Web', NULL, 'guidogiordano@yahoo.it', TRUE, 'Sanitario', 'COVIDIN', 'CN003'),
                               (DEFAULT, 'BNCMRA80D45D205Y', 'Web', NULL, 'mariab@rocketmail.it', FALSE, 'Sanitario', 'CORONAX', 'CN004');

INSERT INTO SOMMINISTRAZIONE VALUES(2, '2021-07-10', '17:35:00', 'BNCMRA80D45D205Y'),
                                   (2, '2021-08-07', '18:00:00', NULL),
                                   (3, '2021-07-11', '17:42:00', 'GDUGDN71D03F351F'),
                                   (4, '2021-07-10', '09:17:00', 'FDRRSS77A01B111U');

INSERT INTO MEDICO VALUES('BNCMRA80D45D205Y', 'Specialistico', 'CN001'),
                         ('MRCRSS61S13D205O', 'Base', 'CN002'),
                         ('GDUGDN71D03F351F', 'Specialistico', 'CN003'),
                         ('FDRRSS77A01B111U', 'Specialistico', 'CN004');

INSERT INTO MAGAZZINO VALUES('AX10655', 'COVIDIN', 'CN001', 1000),
                            ('AX10655', 'COVIDIN', 'CN002', 500),
                            ('AX10775', 'COVIDIN', 'CN001', 750),
                            ('FT001GH', 'FLUSTOP', 'CN003', 450),
                            ('RT7C8A9', 'CORONAX', 'CN003', 1200),
                            ('RT7C8A9', 'CORONAX', 'CN004', 150);

INSERT INTO ALLERGENE VALUES('Soia'),
                            ('Latte'),
                            ('Frutta a guscio'),
                            ('Miorilassanti'),
                            ('Antitubercolari');

INSERT INTO DICHIARAZIONE VALUES(1, 'Latte'),
                                (3, 'Miorilassanti'),
                                (3, 'Soia');

INSERT INTO PRESENZA VALUES('COVIDIN', 'Miorilassanti'),
                           ('CORONAX', 'Latte'),
                           ('CORONAX', 'Antitubercolari');

INSERT INTO SEGNALAZIONE VALUES(4, '10/07/2021', 'RT7C8A9', 'CORONAX');

