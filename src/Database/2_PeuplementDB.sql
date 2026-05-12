-- =====================================
-- Peuplement de la base de données
-- =====================================

INSERT INTO Artist (name, bio, birthYear, contactEmail, isActive, phone, city, website, socialMedia) VALUES
('Lina Moreau', 'Peintre contemporaine française', 1992,
'lina.moreau@mail.com', TRUE,
'0601020304', 'Paris',
'www.linamoreau.fr', '@linamoreau'),

('Noah Diallo', 'Musicien jazz moderne', 1988,
'noah.diallo@mail.com', TRUE,
'0602030405', 'Lyon',
'www.noahdiallo.com', '@noahjazz'),

('Emma Laurent', 'Photographe urbaine', 1995,
'emma.laurent@mail.com', TRUE,
'0603040506', 'Marseille',
'www.emmalaurent.fr', '@emmaphoto');

INSERT INTO Discipline (name) VALUES
('Peinture'),
('Musique'),
('Photographie');

INSERT INTO Exerce (id_artist, id_discipline) VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO Gallery (name, street_number, street_name, city, zip_code, country,
ownerName, openingHours, contactPhone, rating, website) VALUES
('Galerie Horizon',
12,
'Rue Victor Hugo',
'Paris',
75001,
'France',
'Sophie Martin',
'10h-18h',
'0147258899',
4.7,
'www.galeriehorizon.fr');

INSERT INTO Exhibition (title, startDate, endDate, description, curatorName, theme, id_gallery) VALUES
('Couleurs Urbaines',
'2026-06-10',
'2026-06-20',
'Exposition autour des paysages urbains',
'Claire Dubois',
'Ville moderne',
1);

INSERT INTO Artwork (title, creationYear, type, medium, dimensions,
description, price, status, id_exhibition) VALUES
('Ville Bleue',
2024,
'Peinture',
'Huile sur toile',
'120x80',
'Paysage urbain nocturne',
2500,
'FOR_SALE',
1),

('Jazz Night',
2023,
'Musique',
'Audio numérique',
'3min45',
'Composition jazz moderne',
900,
'FOR_SALE',
1),

('Street Faces',
2025,
'Photographie',
'Photo numérique',
'60x40',
'Portraits urbains noir et blanc',
1200,
'SOLD',
1);

INSERT INTO Appartient (id_artist, id_artwork) VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO ArtworkTag (name) VALUES
('Urbain'),
('Jazz'),
('Noir et Blanc');

INSERT INTO Reference (id_artwork, id_artworkTag) VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO Workshop (title, date_, durationMinutes, maxParticipants,
price, location, description, level, id_artist) VALUES

('Initiation Photo de Rue',
'2026-06-15 14:00:00',
120,
10,
35,
'Marseille',
'Atelier pratique de photographie urbaine',
'Débutant',
1),

('Peinture Moderne',
'2026-06-18 10:00:00',
180,
8,
50,
'Paris',
'Découverte de la peinture moderne',
'Intermédiaire',
1),

('Improvisation Jazz',
'2026-07-05 16:00:00',
90,
12,
40,
'Lyon',
'Atelier de création musicale jazz',
'Avancé',
2);

INSERT INTO CommunityMember (name, email, birthYear, phone, membershipType) VALUES

('Alice Martin',
'alice@mail.com',
2001,
'0611223344',
'premium'),

('Hugo Petit',
'hugo@mail.com',
1999,
'0622334455',
'free'),

('Sarah Lopez',
'sarah@mail.com',
2000,
'0633445566',
'premium'),

('Mehdi Benali',
'mehdi@mail.com',
1998,
'0644556677',
'free');

INSERT INTO Aimer (id_member, id_discipline) VALUES
(1,1),
(2,2),
(3,3),
(4,2);

INSERT INTO Booking (bookingDate, paymentStatus, id_workshop, id_member) VALUES
(NOW(), 'PAID', 1, 1),
(NOW(), 'PAID', 1, 2),
(NOW(), 'PENDING', 2, 3),
(NOW(), 'PAID', 3, 4);

INSERT INTO Review (rating, comment, reviewDate, id_artwork, id_member) VALUES

(5,
'Magnifique œuvre moderne',
CURDATE(),
1,
1),

(4,
'Très belle ambiance musicale',
CURDATE(),
2,
2),

(5,
'Photographies impressionnantes',
CURDATE(),
3,
3);
