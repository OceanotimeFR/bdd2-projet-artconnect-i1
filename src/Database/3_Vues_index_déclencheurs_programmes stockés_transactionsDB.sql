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
