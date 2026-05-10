DROP DATABASE IF EXISTS ArtConnect;
CREATE DATABASE ArtConnect;
USE ArtConnect;

-- =====================================
-- DATA INSERTION
-- =====================================

INSERT INTO Artist
(name, bio, birthYear, contactEmail, isActive, phone, city, website, socialMedia)
VALUES
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

INSERT INTO Discipline(name)
VALUES
('Peinture'),
('Musique'),
('Photographie');

INSERT INTO Exerce(id_artist, id_discipline)
VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO Gallery
(name, street_number, street_name, city, zip_code, country,
ownerName, openingHours, contactPhone, rating, website)
VALUES
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

INSERT INTO Exhibition
(title, startDate, endDate, description, curatorName, theme, id_gallery)
VALUES
('Couleurs Urbaines',
'2026-06-10',
'2026-06-20',
'Exposition autour des paysages urbains',
'Claire Dubois',
'Ville moderne',
1);

INSERT INTO Artwork
(title, creationYear, type, medium, dimensions,
description, price, status, id_exhibition)
VALUES
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

INSERT INTO Appartient(id_artist, id_artwork)
VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO ArtworkTag(name)
VALUES
('Urbain'),
('Jazz'),
('Noir et Blanc');

INSERT INTO Reference(id_artwork, id_artworkTag)
VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO Workshop
(title, date_, durationMinutes, maxParticipants,
price, location, description, level, id_artist)
VALUES

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

INSERT INTO CommunityMember
(name, email, birthYear, phone, membershipType)
VALUES

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

INSERT INTO Aimer(id_member, id_discipline)
VALUES
(1,1),
(2,2),
(3,3),
(4,2);

INSERT INTO Booking
(bookingDate, paymentStatus, id_workshop, id_member)
VALUES

(NOW(), 'PAID', 1, 1),
(NOW(), 'PAID', 1, 2),
(NOW(), 'PENDING', 2, 3),
(NOW(), 'PAID', 3, 4);

INSERT INTO Review
(rating, comment, reviewDate, id_artwork, id_member)
VALUES

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

-- =====================================
-- VIEWS
-- =====================================

CREATE VIEW view_artwork_artist AS
SELECT
    aw.title AS artwork_title,
    ar.name AS artist_name,
    aw.type,
    aw.price,
    aw.status
FROM Artwork aw
JOIN Appartient ap
ON aw.id_artwork = ap.id_artwork
JOIN Artist ar
ON ap.id_artist = ar.id_artist;

CREATE VIEW view_workshop_bookings AS
SELECT
    w.id_workshop,
    w.title,
    w.maxParticipants,
    COUNT(b.id_member) AS current_bookings
FROM Workshop w
LEFT JOIN Booking b
ON w.id_workshop = b.id_workshop
GROUP BY
    w.id_workshop,
    w.title,
    w.maxParticipants;

CREATE VIEW view_public_exhibitions AS
SELECT
    e.title,
    g.name AS gallery_name,
    e.startDate,
    e.endDate,
    e.theme
FROM Exhibition e
JOIN Gallery g
ON e.id_gallery = g.id_gallery;

-- =====================================
-- INDEXES
-- =====================================

CREATE INDEX idx_artist_city
ON Artist(city);

CREATE INDEX idx_artwork_status
ON Artwork(status);

CREATE INDEX idx_workshop_date
ON Workshop(date_);

CREATE INDEX idx_booking_workshop
ON Booking(id_workshop);

-- =====================================
-- AUDIT TABLE
-- =====================================

CREATE TABLE ArtworkAudit (
    id_audit INT AUTO_INCREMENT PRIMARY KEY,
    id_artwork INT,
    old_price DOUBLE,
    new_price DOUBLE,
    modificationDate DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =====================================
-- TRIGGERS
-- =====================================

DELIMITER //

CREATE TRIGGER trg_check_workshop_capacity
BEFORE INSERT ON Booking
FOR EACH ROW
BEGIN

    DECLARE current_count INT;
    DECLARE max_places INT;

    SELECT COUNT(*)
    INTO current_count
    FROM Booking
    WHERE id_workshop = NEW.id_workshop;

    SELECT maxParticipants
    INTO max_places
    FROM Workshop
    WHERE id_workshop = NEW.id_workshop;

    IF current_count >= max_places THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Workshop full';

    END IF;

END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER trg_check_exhibition_dates
BEFORE INSERT ON Exhibition
FOR EACH ROW
BEGIN

    IF NEW.endDate <= NEW.startDate THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid exhibition dates';

    END IF;

END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER trg_artwork_price_audit
AFTER UPDATE ON Artwork
FOR EACH ROW
BEGIN

    IF OLD.price <> NEW.price THEN

        INSERT INTO ArtworkAudit
        (id_artwork, old_price, new_price)
        VALUES
        (OLD.id_artwork, OLD.price, NEW.price);

    END IF;

END //

DELIMITER ;

-- =====================================
-- PROCEDURES & FUNCTIONS
-- =====================================

DELIMITER //

CREATE PROCEDURE book_workshop(
    IN p_member INT,
    IN p_workshop INT
)
BEGIN

    INSERT INTO Booking
    (bookingDate, paymentStatus, id_workshop, id_member)
    VALUES
    (NOW(), 'PAID', p_workshop, p_member);

END //

DELIMITER ;

DELIMITER //

CREATE FUNCTION get_workshop_participants(
    p_workshop INT
)
RETURNS INT
DETERMINISTIC
BEGIN

    DECLARE total INT;

    SELECT COUNT(*)
    INTO total
    FROM Booking
    WHERE id_workshop = p_workshop;

    RETURN total;

END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE add_artwork(
    IN p_title VARCHAR(255),
    IN p_year INT,
    IN p_type VARCHAR(100),
    IN p_price DOUBLE,
    IN p_artist INT,
    IN p_exhibition INT
)
BEGIN

    DECLARE new_artwork_id INT;

    INSERT INTO Artwork
    (title, creationYear, type, price, status, id_exhibition)
    VALUES
    (p_title, p_year, p_type, p_price, 'FOR_SALE', p_exhibition);

    SET new_artwork_id = LAST_INSERT_ID();

    INSERT INTO Appartient(id_artist, id_artwork)
    VALUES (p_artist, new_artwork_id);

END //

DELIMITER ;

-- =====================================
-- TRANSACTION TEST
-- =====================================

START TRANSACTION;

INSERT INTO Booking
(bookingDate, paymentStatus, id_workshop, id_member)
VALUES
(NOW(), 'PAID', 2, 1);

INSERT INTO Booking
(bookingDate, paymentStatus, id_workshop, id_member)
VALUES
(NOW(), 'PAID', 3, 1);

COMMIT;
