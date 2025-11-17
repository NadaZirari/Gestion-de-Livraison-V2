## Delivery Management System — Version V2

Un système complet de gestion de livraisons développé avec Spring Boot 3, intégré avec H2, Spring Data JPA, SpringDoc Swagger, et une architecture optimisée en couches (Controller → Service → Repository).

 ## 📌 📖 Description du projet

La version V2 améliore la première version en ajoutant :

Une architecture logicielle propre (Controller / Service / Repository).

Une gestion complète des entités : Customer, Vehicle, Delivery, DeliveryHistory.

L’utilisation de DTOs pour séparer les couches et améliorer Swagger UI.

Une configuration H2 en mémoire avec Liquibase pour charger les données.

Une documentation API automatique avec Swagger (SpringDoc OpenAPI).

Une gestion correcte des relations JPA + optimisation des insertions.

Un système d’erreurs plus propre et standardisé.

## 🏗️ Architecture du Projet

src/main/java
└── com.delivery
    ├── controller
    │   └── DeliveryController.java
    ├── service
    │   ├── DeliveryService.java
    │   └── impl/DeliveryServiceImpl.java
    ├── repository
    │   └── DeliveryRepository.java
    ├── dto
    │   └── DeliveryDTO.java
    ├── entity
    │   ├── Customer.java
    │   ├── Delivery.java
    │   ├── Vehicle.java
    │   └── DeliveryHistory.java
    └── exception
        └── GlobalExceptionHandler.java

## 🗂️ Fonctionnalités principales
# ✔️ Gestion des Clients (Customer)

Ajouter un client

Trouver des clients par adresse, position, créneau horaire

Récupérer un client + détails

# ✔️ Gestion des Véhicules (Vehicle)

Ajouter un véhicule (type, capacité, volume, poids maximal)

Récupérer la liste des véhicules

# ✔️ Gestion des Livraisons (Delivery)

Créer une livraison

Assigner un véhicule

Mettre à jour le statut

Planifier des horaires préférés (preferredFrom / preferredTo)

# ✔️ Historique de Livraison (DeliveryHistory)

Suivre les événements :

CREATED

ASSIGNED

IN_PROGRESS

COMPLETED

FAILED

## ✔️ Documentation Swagger UI

Disponible automatiquement ici :
👉 /swagger-ui.html
👉 /api/v3/api-docs

## 🛢️ Base de données

Cette version utilise H2 en mémoire :

📍 Configuration H2 (application.yml)
spring:
  datasource:
    url: jdbc:h2:mem:deliverydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password: password
    driver-class-name: org.h2.Driver

  h2:
    console:
      enabled: true
      path: /h2-console

🔧 Liquibase activé

Chargement automatique des tables

Chargement des données d’exemple

## 🔗 Endpoints REST principaux
🚚 Delivery
Méthode	Endpoint	Description
POST	/api/delivery	Créer une livraison
GET	/api/delivery/{id}	Récupérer une livraison
PUT	/api/delivery/{id}/status	Mettre à jour le statut
👤 Customer

# Méthode	Endpoint	Description
POST	/api/customers	Ajouter un client
GET	/api/customers/address	Rechercher un client par adresse
GET	/api/customers/nearby	Rechercher par latitude/longitude
🚗 Vehicle

# Méthode	Endpoint	Description
POST	/api/vehicles	Ajouter un véhicule
GET	/api/vehicles	Liste des véhicules


## Diagramme de classe 

<img width="569" height="376" alt="Capture d&#39;écran 2025-11-15 234923" src="https://github.com/user-attachments/assets/147acbe6-2df4-42a3-86df-197de5079c9b" />

## 🧱 Technologies utilisées

Technologie	Rôle
Spring Boot 3	Framework backend
Spring Web	API REST
Spring Data JPA	Interaction BD
H2 Database	BD en mémoire
Liquibase	Migrations
Lombok	Réduction du boilerplate
SpringDoc OpenAPI	Documentation Swagger
JUnit / Mockito	Tests unitaires

## 🧪 Tests

La version V2 inclut :

Tests unitaires sur le Service Layer

Tests MockMvc sur les contrôleurs

Tests DAO avec H2

## 🚀 Lancement du projet

1️⃣ Cloner le projet
git clone https://github.com/username/Gestion-de-Livraison-V2.git
cd Gestion-de-Livraison-V2

2️⃣ Lancer l'application
mvn spring-boot:run

3️⃣ Accéder :

Swagger : http://localhost:8082/swagger-ui.html

H2 console : http://localhost:8082/h2-console

## 📦 Améliorations prévues (V3)

Optimisation du routing des véhicules

Algorithme d’optimisation (plus court chemin + disponibilité)

Ajout de Spring Security (JWT)



📝 Auteur

Nada — Full Stack Developer Student
