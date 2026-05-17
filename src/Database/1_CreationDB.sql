-- Création de la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS ArtConnect;

-- Utilisation de la base de données
USE ArtConnect;

-- =====================
-- Création des tables
-- =====================

CREATE TABLE CommunityMember(
   id_member INT auto_increment not null,
   name VARCHAR(50) NOT NULL,
   email VARCHAR(50) NOT NULL,
   birthYear INT,
   phone VARCHAR(50),
   membershipType ENUM('FREE', 'PREMIUM') DEFAULT 'FREE',
   PRIMARY KEY(id_member)
);

CREATE TABLE Artist(
   id_artist INT auto_increment not null,
   name VARCHAR(50),
   bio VARCHAR(1000),
   birthYear INT,
   contactEmail VARCHAR(50),
   isActive BOOLEAN,
   phone VARCHAR(50),
   city VARCHAR(50),
   website VARCHAR(50),
   socialMedia VARCHAR(50),
   PRIMARY KEY(id_artist)
);

CREATE TABLE ArtworkTag(
   id_artworkTag INT auto_increment not null,
   name VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_artworkTag)
);

CREATE TABLE Discipline(
   id_discipline INT auto_increment not null,
   name VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_discipline)
);

CREATE TABLE Gallery(
   id_gallery INT auto_increment not null,
   name VARCHAR(50) NOT NULL,
   street_number INT,
   street_name VARCHAR(50) NOT NULL,
   city VARCHAR(50) NOT NULL,
   zip_code INT,
   country VARCHAR(50),
   ownerName VARCHAR(50),
   openingHours VARCHAR(50),
   contactPhone VARCHAR(50),
   rating DOUBLE,
   website VARCHAR(50),
   PRIMARY KEY(id_gallery)
);

CREATE TABLE Workshop(
   id_workshop INT auto_increment not null,
   title VARCHAR(50) NOT NULL,
   date_ DATETIME NOT NULL,
   durationMinutes INT,
   maxParticipants INT,
   price DOUBLE NOT NULL,
   location VARCHAR(100),
   description VARCHAR(2000),
   level VARCHAR(50),
   id_artist INT NOT NULL,
   PRIMARY KEY(id_workshop),
   FOREIGN KEY(id_artist) REFERENCES Artist(id_artist)
);

CREATE TABLE Booking(
   id_booking INT auto_increment not null,
   bookingDate DATETIME NOT NULL,
   paymentStatus VARCHAR(50),
   id_workshop INT NOT NULL,
   id_member INT NOT NULL,
   PRIMARY KEY(id_booking),
   FOREIGN KEY(id_workshop) REFERENCES Workshop(id_workshop),
   FOREIGN KEY(id_member) REFERENCES CommunityMember(id_member)
);

CREATE TABLE Exhibition(
   id_exhibition INT auto_increment not null,
   title VARCHAR(50) NOT NULL,
   startDate DATE,
   endDate DATE,
   description VARCHAR(2000),
   curatorName VARCHAR(50),
   theme VARCHAR(50),
   id_gallery INT NOT NULL,
   PRIMARY KEY(id_exhibition),
   FOREIGN KEY(id_gallery) REFERENCES Gallery(id_gallery)
);

CREATE TABLE Artwork(
   id_artwork INT auto_increment not null,
   title VARCHAR(50),
   creationYear INT,
   type VARCHAR(50),
   medium VARCHAR(50),
   dimensions VARCHAR(50),
   description VARCHAR(2000),
   price DOUBLE,
   status ENUM('FOR_SALE', 'SOLD', 'EXHIBITED') NOT NULL,
   id_exhibition INT,
   PRIMARY KEY(id_artwork),
   FOREIGN KEY(id_exhibition) REFERENCES Exhibition(id_exhibition)
);

CREATE TABLE Review(
   id_review INT auto_increment not null,
   rating CHAR(1) NOT NULL,
   comment VARCHAR(5000),
   reviewDate DATE,
   id_artwork INT NOT NULL,
   id_member INT NOT NULL,
   PRIMARY KEY(id_review),
   FOREIGN KEY(id_artwork) REFERENCES Artwork(id_artwork),
   FOREIGN KEY(id_member) REFERENCES CommunityMember(id_member)
);

CREATE TABLE Appartient(
   id_artist INT not null,
   id_artwork INT not null,
   PRIMARY KEY(id_artist, id_artwork),
   FOREIGN KEY(id_artist) REFERENCES Artist(id_artist),
   FOREIGN KEY(id_artwork) REFERENCES Artwork(id_artwork)
);

CREATE TABLE Exerce(
   id_artist INT not null,
   id_discipline INT not null,
   PRIMARY KEY(id_artist, id_discipline),
   FOREIGN KEY(id_artist) REFERENCES Artist(id_artist),
   FOREIGN KEY(id_discipline) REFERENCES Discipline(id_discipline)
);

CREATE TABLE Reference(
   id_artwork INT not null,
   id_artworkTag INT not null,
   PRIMARY KEY(id_artwork, id_artworkTag),
   FOREIGN KEY(id_artwork) REFERENCES Artwork(id_artwork),
   FOREIGN KEY(id_artworkTag) REFERENCES ArtworkTag(id_artworkTag)
);

CREATE TABLE Aimer(
   id_member INT not null,
   id_discipline INT not null,
   PRIMARY KEY(id_member, id_discipline),
   FOREIGN KEY(id_member) REFERENCES CommunityMember(id_member),
   FOREIGN KEY(id_discipline) REFERENCES Discipline(id_discipline)
);
