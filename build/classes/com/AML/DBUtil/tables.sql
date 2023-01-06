
DROP DATABASE IF EXISTS AMLBoutique;
CREATE DATABASE AMLBoutique;
USE AMLBoutique;
-- la table clients : 
CREATE TABLE `clients` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Email` varchar(40) NOT NULL,
  `Nom` varchar(20) NOT NULL,
  `Prenom` varchar(20) NOT NULL,
  `Adresse` text NOT NULL,
  `CodePostal` int NOT NULL,
  `Ville` varchar(40) NOT NULL,
  `Tel` int NOT NULL,
  `MotPasse` varchar(30) NOT NULL,
  `isAdmin` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Email` (`Email`)
);
-- la table userSession : 
CREATE TABLE UserSession (
    `id` INT NOT NULL AUTO_INCREMENT,
    `CodeClient` INT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `clients_CodeClient_userSession` FOREIGN KEY (`CodeClient`)
    REFERENCES `clients` (`Id`) on delete cascade
);

-- la table AdresseLivraison :
CREATE TABLE `AdresseLivraison` (
  `AdresseLivraisonId` int NOT NULL AUTO_INCREMENT,
  `adresse` text NOT NULL,
  `CodePostal` int NOT NULL,
  `tel` int NOT NULL,
  `ville` varchar(100) NOT NULL,
  `pays` varchar(100) NOT NULL,
  `CodeClient` int NOT NULL,
  PRIMARY KEY (`AdresseLivraisonId`),
  KEY `clients_AdresseLivraison_CodeClient` (`CodeClient`),
  CONSTRAINT `clients_AdresseLivraison_CodeClient` FOREIGN KEY (`CodeClient`) REFERENCES `clients` (`Id`) on delete cascade
);

-- la table commandes : 
CREATE TABLE `commandes` (
  `NumCommande` int NOT NULL AUTO_INCREMENT,
  `CodeClient` int NOT NULL,
  `DateCommande` date NOT NULL,
--   `Etat` varchar(100) NOT NULL,
--   `isConfirmed` tinyint(1) NOT NULL DEFAULT '0',
--   `shippingId` int Not NULL,
  `AdresseLivraisonId` int NOT NULL,
  PRIMARY KEY (`NumCommande`),
  CONSTRAINT `commandes_AdresseLivraison_AdresseLivraisonId` FOREIGN KEY (`AdresseLivraisonId`) REFERENCES `AdresseLivraison` (`AdresseLivraisonId`) on delete cascade, 
  CONSTRAINT `commandes_CodeClient` FOREIGN KEY (`CodeClient`) REFERENCES `clients` (`Id`) on delete cascade,
--   CONSTRAINT `shipping_shippingId` FOREIGN KEY (`shippingId`) REFERENCES `shipping` (`shippingId`) on delete cascade
);

-- la table categories : 
CREATE TABLE `categories` (
  `RefCat` int NOT NULL AUTO_INCREMENT,
  `CatTitle` varchar(80) Not NULL,
  `CatDescription` text NOT NULL,
  PRIMARY KEY (`RefCat`)
);

-- la table produits : 
CREATE TABLE `produits` (
  `Codeproduits` int NOT NULL AUTO_INCREMENT,
  `Designation` varchar(100) NOT NULL,
  `Description` text NOT NULL,
  `Prix` double NOT NULL,
  `Stock` int NOT NULL,
  `Photo` LONGBLOB NOT NULL,
  `RefCat` int NOT NULL,
  PRIMARY KEY (`Codeproduits`),
  KEY `produits_RefCat` (`RefCat`),
  CONSTRAINT `produits_RefCat` FOREIGN KEY (`RefCat`) REFERENCES `categories` (`RefCat`) on delete cascade
);

-- la table lignecommandes : 
CREATE TABLE `lignecommandes` (
  `NumCommande` int NOT NULL,
  `Codeproduits` int NOT NULL,
  `QteCde` int NOT NULL,
  PRIMARY KEY (`NumCommande`,`Codeproduits`),
  CONSTRAINT `lignecommandes_Codeproduits` FOREIGN KEY (`Codeproduits`) REFERENCES `produits` (`Codeproduits`) on delete cascade, 
  CONSTRAINT `lignecommandes_NumCommande` FOREIGN KEY (`NumCommande`) REFERENCES `commandes` (`NumCommande`) on delete cascade
);

-- la table panier : 
CREATE TABLE `panier` (
  `codeproduit` int NOT NULL,
  `qtecde` int DEFAULT NULL,
  PRIMARY KEY (`codeproduit`),
  CONSTRAINT `panier_produit_codeproduit` FOREIGN KEY (`codeproduit`) REFERENCES `produits` (`Codeproduits`)
);