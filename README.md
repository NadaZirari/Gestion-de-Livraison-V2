## Delivery Management System

# 🧩 Description du projet

Le projet Delivery est une application Spring basée sur une architecture n-tiers (Controller – Service – Repository).
Il permet de gérer les livraisons, les tournées et les entrepôts (warehouses).
Le système applique une configuration hybride :

Les propriétés de la base de données sont configurées dans application.properties.

L’injection de dépendances et la gestion des beans sont faites via applicationContext.xml, sans utiliser d’annotations comme @Autowired, @Service, @Repository, ou @Component.

## ⚙️ Technologies utilisées

Java 17

Spring Framework 6

Spring Data JPA

Hibernate

Maven

H2 Database (ou PostgreSQL selon le profil)

Swagger UI (pour la documentation REST)

JUnit 5 (tests unitaires)

applicationContext.xml (configuration manuelle des beans)



## 🗂️ Structure du projet

src/main/java/com/delivrey
 ├── config/
 │    └── applicationContext.xml
 ├── controller/
 │    └── DeliveryController.java
 ├── entity/
 │    ├── Delivery.java
 │    ├── Tour.java
 │    └── Warehouse.java
 ├── repository/
 │    ├── DeliveryRepository.java
 │    ├── TourRepository.java
 │    └── WarehouseRepository.java
 ├── service/
 │    ├── DeliveryService.java
 │    └── impl/DeliveryServiceImpl.java
 └── DeliveryApplication.java


## Endpoints REST – Delivery Management System
 # 1️⃣ Vehicles (/vehicles)

GET /vehicles : Liste tous les véhicules

GET /vehicles/{id} : Récupère un véhicule par ID

POST /vehicles : Crée un nouveau véhicule (JSON)

PUT /vehicles/{id} : Met à jour un véhicule existant (JSON)

DELETE /vehicles/{id} : Supprime un véhicule

# 2️⃣ Deliveries (/deliveries)

GET /deliveries : Liste toutes les livraisons

GET /deliveries/{id} : Récupère une livraison par ID

POST /deliveries : Crée une nouvelle livraison (JSON)

PUT /deliveries/{id} : Met à jour une livraison existante (JSON)

DELETE /deliveries/{id} : Supprime une livraison

# 3️⃣ Tours (/tours)

GET /tours : Liste toutes les tournées

GET /tours/{id} : Récupère une tournée par ID

POST /tours : Crée une nouvelle tournée (JSON)

PUT /tours/{id} : Met à jour une tournée existante (JSON)

DELETE /tours/{id} : Supprime une tournée

# 4️⃣ Warehouses (/warehouses)

GET /warehouses : Liste tous les entrepôts

GET /warehouses/{id} : Récupère un entrepôt par ID

POST /warehouses : Crée un nouvel entrepôt (JSON)

PUT /warehouses/{id} : Met à jour un entrepôt existant (JSON)

DELETE /warehouses/{id} : Supprime un entrepôt


## 💡 Règles respectées

✅ Pas d’injection via annotations (@Autowired, @Service, etc.)
✅ Beans configurés via XML (applicationContext.xml)
✅ Utilisation de <jpa:repositories> au lieu de @Repository
✅ Propriétés externes dans application.properties

## run the packaged jar:

java -jar target/Delivery-0.0.1-SNAPSHOT.jar


## Lancement du projet

# Compiler le projet :

mvn clean install


# Exécuter :

mvn spring-boot:run


# Accéder à :

Swagger UI → http://localhost:8080/swagger-ui.html

H2 Console → http://localhost:8080/h2-console

## Algorithmics / Logic and Optimization

This project implements routing and delivery optimization for a delivery management system. The main algorithmic components are:

# Nearest Neighbor Algorithm (NN)

Used to create initial delivery routes.

For each delivery, the algorithm selects the next closest unvisited delivery location.

Produces a fast initial solution but may not be globally optimal.

# Clarke-Wright Savings Algorithm (CW)

Used to optimize and merge delivery routes for efficiency.

Calculates “savings” by combining routes and minimizing the total distance traveled.

Iteratively merges routes until no further savings are possible.

## Seed data (executed on startup)

File: src/main/resources/data.sql

Creates:

1 Warehouse (id=1)
3 Vehicles (id=1..3): BIKE, VAN, TRUCK
Deliveries (id=100..119)


## 🧪 Tests

Les tests unitaires sont placés dans src/test/java/com/delivrey/service/impl.

Pour les exécuter :

#mvn test



## ScreenShots
<img width="1868" height="866" alt="image" src="https://github.com/user-attachments/assets/12fc295e-7626-49da-8948-8a624e140274" />


<img width="297" height="263" alt="image" src="https://github.com/user-attachments/assets/a4c328c8-5c51-46e6-9db8-862916773af7" />


## 👩‍💻 Auteur

Projet développé par Nada – Étudiante en développement Java Spring
📅 Année : 2025



