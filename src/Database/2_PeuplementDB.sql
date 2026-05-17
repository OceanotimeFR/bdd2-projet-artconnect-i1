-- =====================================
-- Peuplement de la base de données
-- =====================================

-- Utilisation de la base de données
USE ArtConnect;

INSERT INTO Discipline (name) VALUES 
('Peinture'),          -- ID 1
('Musique'),           -- ID 2
('Photographie'),      -- ID 3
('Street Art'),        -- ID 4
('Sculpture'),         -- ID 5
('Installation');      -- ID 6

INSERT INTO Artist (name, bio, birthYear, contactEmail, isActive, phone, city, website, socialMedia) VALUES 
(
    'Banksy', 
    'Artiste urbain anonyme britannique, connu pour son art satirique et subversif réalisé au pochoir.', 
    1974, 'contact@pestcontroloffice.com', TRUE, '+44 20 7123 4567', 'Bristol', 'banksy.co.uk', '@banksy'
),
(
    'Hans Zimmer', 
    'Compositeur de musique de film allemand, célèbre pour ses bandes originales immersives et épiques (Interstellar, Inception).', 
    1957, 'management@hanszimmer.com', TRUE, '+1 310 555 0199', 'Los Angeles', 'hanszimmer.com', '@hanszimmer'
),
(
    'Annie Leibovitz', 
    'Photographe américaine de renommée mondiale, célèbre pour ses portraits intimes et iconiques de célébrités.', 
    1949, 'studio@leibovitz.com', TRUE, '+1 212 555 0188', 'New York', 'annieleibovitz.com', '@annieleibovitz'
),
(
    'Yayoi Kusama', 
    'Artiste contemporaine japonaise avant-gardiste, reconnue pour ses installations immersives et son utilisation obsessionnelle des pois.', 
    1929, 'info@yayoi-kusama.jp', TRUE, '+81 3 3202 8500', 'Tokyo', 'yayoi-kusama.jp', '@yayoikusama_'
),
(
    'Salvador Dalí', 
    'Artiste peintre et sculpteur espagnol, figure de proue du mouvement surréaliste et maître de l''excentricité.', 
    1904, 'archives@salvador-dali.org', FALSE, NULL, 'Figueres', 'salvador-dali.org', '@salvadordali_art'
);

INSERT INTO Exerce (id_artist, id_discipline) VALUES 
-- Banksy (Street Art et Peinture)
(1, 4), 
(1, 1),

-- Hans Zimmer (Musique)
(2, 2),

-- Annie Leibovitz (Photographie)
(3, 3),

-- Yayoi Kusama (Installation, Peinture et Sculpture)
(4, 6), 
(4, 1), 
(4, 5),

-- Salvador Dalí (Peinture et Sculpture)
(5, 1), 
(5, 5);

INSERT INTO Gallery (name, street_number, street_name, city, zip_code, country, ownerName, openingHours, contactPhone, rating, website) VALUES 
('Galerie du Louvre d''Art', 10, 'Rue de Rivoli', 'Paris', 75001, 'France', 'Jean-Luc Martinez', '09:00 - 18:00', '+33140205050', 4.8, 'galerielouvreart.fr'),
('Tokyo Modern Pavilion', 3, 'Chome-1-1 Roppongi', 'Tokyo', 1060032, 'Japon', 'Kenjiro Monami', '10:00 - 20:00', '+81357778600', 4.7, 'tokyomodernpavilion.jp'),
('The London Contemporary Gallery', 53, 'Bankside', 'Londres', NULL, 'Royaume-Uni', 'Nicholas Serota', '10:00 - 18:00', '+442078878888', 4.6, 'londoncontemporary.co.uk'),
('Galleria Nazionale di Roma', 131, 'Viale delle Belle Arti', 'Rome', 197, 'Italie', 'Cristiana Collu', '09:00 - 19:30', '+3906322981', 4.5, 'lagallerianazionale.it');

INSERT INTO Exhibition (title, startDate, endDate, description, curatorName, theme, id_gallery) VALUES 
('Le Surréalisme au XXIe Siècle', '2026-06-01', '2026-08-31', 'Une exploration moderne des concepts surréalistes à travers des installations interactives.', 'Alice Martin', 'Surréalisme', 1),
('L''Éveil de l''Impressionnisme', '2026-09-15', '2026-11-15', 'Rétrospective sur les débuts du mouvement impressionniste.', 'Pierre Dubois', 'Impressionnisme', 1),
('Digital Visions Tokyo', '2026-05-20', '2026-07-20', 'Exposition d''art numérique et d''installations lumineuses immersives.', 'Takashi Saito', 'Art Numérique', 2),
('Estampes Contemporaines', '2026-08-01', '2026-10-01', 'Dialogue entre les techniques traditionnelles de l''ukiyo-e et l''art graphique moderne.', 'Yuki Tanaka', 'Estampe', 2),
('Masters of British Street Art', '2026-06-10', '2026-09-10', 'Une collection majeure dédiée à l''art urbain britannique contemporain.', 'Sarah Jenkins', 'Street Art', 3),
('Sculpting the Future', '2026-10-01', '2026-12-31', 'Exposition de sculptures abstraites en matériaux recyclés.', 'James Smith', 'Sculpture Moderne', 3),
('Renaissance Moderne', '2026-07-01', '2026-09-30', 'Réinterprétation des chefs-d''œuvre de la Renaissance par des artistes actuels.', 'Giovanni Rossi', 'Néo-Classicisme', 4),
('Ombre et Lumière : Photographie', '2026-10-15', '2027-01-15', 'Rétrospective de la photographie contrastée en noir et blanc.', 'Elena Bianchi', 'Photographie', 4);

INSERT INTO Artwork (title, creationYear, type, medium, dimensions, description, price, status, id_exhibition) VALUES 
('Girl with Balloon', 2002, 'Peinture', 'Pochoir et aérosol', '40x40 cm', 'Une des œuvres les plus célèbres du street art contemporain.', 500000.0, 'EXHIBITED', 5),
('Love is in the Bin', 2018, 'Peinture', 'Acrylique et aérosol avec déchiqueteuse', '101x78 cm', 'L''œuvre qui s''est auto-détruite lors d''une vente aux enchères.', 1500000.0, 'SOLD', 5),
('Time - Partition Originale', 2010, 'Manuscrit', 'Papier et encre', '21x29.7 cm', 'Partition originale manuscrite du thème principal d''un célèbre film.', 15000.0, 'FOR_SALE', 3),
('John Lennon and Yoko Ono', 1980, 'Photographie', 'Tirage argentique', '60x40 cm', 'Portrait iconique pris quelques heures avant le décès de l''artiste.', 80000.0, 'EXHIBITED', 8),
('Queen Elizabeth II Portrait', 2007, 'Photographie', 'Tirage numérique', '80x60 cm', 'Portrait officiel aux teintes sombres et contrastées.', 45000.0, 'SOLD', 8),
('Infinity Mirror Room', 1965, 'Installation', 'Miroirs, LED, bois', 'Variable', 'Installation immersive jouant sur la perception de l''infini.', 4000000.0, 'EXHIBITED', 3),
('Yellow Pumpkin', 1994, 'Sculpture', 'Fibre de verre et peinture', '200x200 cm', 'Sculpture monumentale ornée de pois noirs symétriques.', 2000000.0, 'EXHIBITED', 6),
('La Persistance de la mémoire', 1931, 'Peinture', 'Huile sur toile', '24x33 cm', 'Représentation onirique de montres fondantes dans un paysage désertique.', 150000000.0, 'EXHIBITED', 1),
('Cygnes reflétant des éléphants', 1937, 'Peinture', 'Huile sur toile', '51x77 cm', 'Illusion d''optique typique de la méthode paranoïaque-critique.', 35000000.0, 'EXHIBITED', 1),
('Téléphone-homard', 1936, 'Sculpture', 'Plâtre, plastique et métal', '15x30x17 cm', 'Assemblage absurde et emblématique du mouvement surréaliste.', 5000000.0, 'EXHIBITED', 1);

INSERT INTO Appartient (id_artist, id_artwork) VALUES 
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(3, 5),
(4, 6),
(4, 7),
(5, 8),
(5, 9),
(5, 10);

INSERT INTO ArtworkTag (name) VALUES 
('Surréalisme'),
('Street Art'),
('Portrait'),
('Immersif'),
('Iconique'),
('Contemporain');

INSERT INTO Reference (id_artwork, id_artworkTag) VALUES 
(1, 2),
(1, 5),
(1, 6),
(2, 2),
(2, 5),
(2, 6),
(3, 5),
(4, 3),
(4, 5),
(5, 3),
(6, 4),
(6, 6),
(7, 5),
(7, 6),
(8, 1),
(8, 5),
(9, 1),
(10, 1);

INSERT INTO Workshop (title, date_, durationMinutes, maxParticipants, price, location, description, level, id_artist) VALUES 
('Initiation au Pochoir Urbain', '2026-07-10 14:00:00', 120, 15, 45.0, 'Bristol, Rues du centre', 'Apprenez les bases de la découpe de pochoirs et l''utilisation des aérosols.', 'Débutant', 1),
('Message et Subversion', '2026-07-15 16:00:00', 120, 20, 60.0, 'The London Contemporary Gallery', 'Atelier théorique et pratique sur la transmission d''un message social fort.', 'Avancé', 1),
('Masterclass Composition Épique', '2026-10-12 09:00:00', 240, 50, 200.0, 'Conservatoire de Paris', 'Techniques de composition hybride mêlant orchestre symphonique et synthétiseurs.', 'Avancé', 2),
('Portrait et Lumière Naturelle', '2026-08-05 10:00:00', 180, 10, 150.0, 'Studio Leibovitz, New York', 'Comment capturer l''intimité d''un sujet en utilisant uniquement la lumière du jour.', 'Intermédiaire', 3),
('Exploration des Motifs Infinis', '2026-09-20 15:00:00', 90, 20, 40.0, 'Tokyo Modern Pavilion', 'Création d''œuvres immersives basées sur la répétition de formes et de couleurs.', 'Débutant', 4),
('Introduction à l''Art Numérique', '2026-11-01 13:00:00', 120, 15, 80.0, 'En ligne (Visioconférence)', 'Les bases de la retouche photographique et de la composition numérique moderne.', 'Intermédiaire', 3);

INSERT INTO CommunityMember (name, email, birthYear, phone, membershipType) VALUES 
('Lucas Dupont', 'lucas.dupont@email.fr', 2005, '+33612345678', 'FREE'),
('Emma Martin', 'emma.martin@email.com', 1992, '+33623456789', 'PREMIUM'),
('John Doe', 'john.doe@email.com', 1985, '+15551234567', 'PREMIUM'),
('Yuki Takahashi', 'yuki.t@email.jp', 1998, '+819012345678', 'FREE'),
('Giulia Rossi', 'giulia.rossi@email.it', 1990, '+393331234567', 'FREE'),
('Thomas Leroy', 'thomas.leroy@email.fr', 1988, '+33634567890', 'PREMIUM'),
('Sarah Smith', 'sarah.smith@email.co.uk', 1995, '+447912345678', 'FREE'),
('Kenji Sato', 'kenji.sato@email.jp', 2001, '+818012345678', 'FREE'),
('Marie Lambert', 'marie.lambert@email.fr', 1980, '+33645678901', 'PREMIUM'),
('Pablo Gomez', 'pablo.g@email.es', 1993, '+34612345678', 'FREE'),
('Alice Wonderland', 'alice.w@email.co.uk', 1997, '+447812345678', 'PREMIUM'),
('Oliver Hansen', 'oliver.h@email.dk', 1982, '+4512345678', 'FREE'),
('Chloe Tremblay', 'chloe.t@email.ca', 1999, '+15141234567', 'PREMIUM'),
('David Müller', 'david.m@email.de', 1975, '+4915112345678', 'FREE'),
('Hugo Dubois', 'hugo.dubois@email.fr', 2004, '+33656789012', 'FREE');

INSERT INTO Aimer (id_member, id_discipline) VALUES
(1, 1), 
(1, 4), 
(2, 2), 
(2, 3), 
(3, 5), 
(4, 6), 
(5, 1), 
(6, 3), 
(7, 4), 
(8, 2), 
(9, 6), 
(10, 1), 
(11, 5), 
(12, 3), 
(13, 1), 
(14, 4), 
(15, 2);

INSERT INTO Booking (bookingDate, paymentStatus, id_workshop, id_member) VALUES
('2026-05-10 10:30:00', 'PAID', 1, 1),
('2026-05-15 14:45:00', 'PAID', 2, 2),
('2026-06-01 09:15:00', 'PENDING', 3, 3),
('2026-06-05 18:20:00', 'PAID', 3, 15),
('2026-06-20 11:10:00', 'PAID', 4, 5),
('2026-07-02 16:00:00', 'CANCELLED', 5, 8),
('2026-08-10 20:30:00', 'PAID', 6, 10),
('2026-06-25 13:40:00', 'PAID', 1, 12),
('2026-07-10 08:50:00', 'PENDING', 4, 7);

INSERT INTO Review (rating, comment, reviewDate, id_artwork, id_member) VALUES
('5', 'Une œuvre fascinante qui pousse à la réflexion. La technique au pochoir est incroyable.', '2026-06-12', 1, 2),
('4', 'Très beau travail sur la lumière et le contraste, même si je m''attendais à un format plus imposant.', '2026-06-15', 5, 7),
('5', 'L''installation est totalement bluffante, on perd complètement la notion de l''espace et du temps !', '2026-05-25', 6, 4),
('3', 'Concept intéressant et audacieux, mais le message me semble un peu trop évident.', '2026-07-02', 2, 11),
('5', 'Un chef-d''œuvre absolu du surréalisme. Voir les détails de cette toile en vrai est une expérience unique.', '2026-06-05', 8, 14),
('4', 'J''adore la composition de cette photographie. C''est une vraie source d''inspiration.', '2026-07-12', 4, 1),
('2', 'Je n''ai pas vraiment accroché avec cette sculpture, c''est un peu trop abstrait et répétitif pour moi.', '2026-05-30', 7, 9),
('5', 'Émouvant de voir un manuscrit original d''une telle composition musicale. L''histoire s''y ressent.', '2026-06-22', 3, 3);
