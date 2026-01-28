# Application de Gestion de Commandes

API REST pour la gestion de commandes clients développée avec Spring Boot.

## Prérequis

- Java 21
- Maven 3.8+
- MySQL 8.0+
- IntelliJ IDEA 

## Configuration

### 1. Créer les bases de données
```sql
CREATE DATABASE commandes_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

```

### 2. Définir les variables d'environnement

#### Sous Windows (PowerShell) :
```powershell
# Profil dev
$env:SPRING_PROFILES_ACTIVE="dev"
$env:DB_URL="jdbc:mysql://localhost:3306/commandes_dev"
$env:DB_USERNAME="root"
$env:DB_PASSWORD=""
$env:SERVER_PORT="8080"
```

##  Documentation API (Swagger)

Une fois l'application démarrée, accédez à la documentation interactive :
```
http://localhost:8080/swagger-ui.html
```

Ou accédez directement aux spécifications OpenAPI :
```
http://localhost:8080/api-docs
```

## Exemples d'appels API

### 1. Créer un client
```http
POST http://localhost:8080/api/clients
Content-Type: application/json

{
  "nom": "Abdoulaye Boly",
  "email": "boly.abdoulaye@ugb.edu.sn"
}
```

**Réponse (201 Created) :**
```json
{
  "id": 1,
  "nom": "Abdoulaye Boly",
  "email": "boly.abdoulaye@ugb.edu.sn"
}
```

### 2. Créer un produit
```http
POST http://localhost:8080/api/produits
Content-Type: application/json

{
  "nom": "Laptop HP",
  "prix": 750000,
  "stock": 10
}
```

**Réponse (201 Created) :**
```json
{
  "id": 1,
  "nom": "Laptop HP",
  "prix": 750000,
  "stock": 10
}
```

### 3. Créer une commande
```http
POST http://localhost:8080/api/commandes
Content-Type: application/json

{
  "clientId": 1,
  "lignes": [
    {
      "produitId": 1,
      "quantite": 2
    },
    {
      "produitId": 2,
      "quantite": 1
    }
  ]
}
```

**Réponse (201 Created) :**
```json
{
  "id": 1,
  "dateCommande": "2026-01-27T12:00:00",
  "status": "CREATED",
  "clientId": 1,
  "clientNom": "Jean Dupont",
  "lignes": [
    {
      "id": 1,
      "produitId": 1,
      "produitNom": "Laptop HP",
      "quantite": 2,
      "prixUnitaire": 750000,
      "sousTotal": 1500000
    },
    {
      "id": 2,
      "produitId": 2,
      "produitNom": "Clavier Mécanique",
      "quantite": 1,
      "prixUnitaire": 35000,
      "sousTotal": 35000
    }
  ],
  "montantTotal": 1535000
}
```

### 4. Valider une commande
```http
PUT http://localhost:8080/api/commandes/1/valider
```

**Réponse (200 OK) :**
```json
{
  "id": 1,
  "dateCommande": "2026-01-27T12:00:00",
  "status": "VALIDATED",
  "clientId": 1,
  "clientNom": "Abdoulaye Boly",
  "montantTotal": 1535000
}
```

### 5. Récupérer toutes les commandes d'un client
```http
GET http://localhost:8080/api/commandes/client/1
```

### 6. Récupérer les commandes par statut
```http
GET http://localhost:8080/api/commandes/status/CREATED
```

### 7. Récupérer les produits avec stock faible
```http
GET http://localhost:8080/api/produits/stock-faible?seuil=10
```

### 8. Annuler une commande
```http
PUT http://localhost:8080/api/commandes/1/annuler
```

##  Fonctionnalités

### Gestion des Clients
- Créer, modifier, supprimer un client
- Rechercher par ID ou email
- Lister tous les clients

### Gestion des Produits
- CRUD complet
- Rechercher les produits disponibles (stock > 0)
- Alertes stock faible

### Gestion des Commandes
- Créer une commande avec validation du stock
- Calcul automatique du montant total
- Valider une commande (mise à jour du stock)
- Annuler une commande (remise du stock)
- Rechercher par client, statut ou période

### Règles Métier Implémentées
1.  **Interdiction de commander si stock insuffisant**
2.  **Calcul automatique du total de la commande**
3.  **Mise à jour du stock après validation**

## Architecture
```
com.polytech.commandes
├── entity/          # Entités JPA (SANS relations)
├── repository/      # Couche d'accès aux données
├── dto/             # Objets de transfert de données
├── service/         # Logique métier
│   └── impl/        # Implémentations
├── controller/      # Contrôleurs REST
├── exception/       # Gestion des exceptions
└── config/          # Configuration


## Technologies utilisées

- **Spring Boot 3.2.2**
- **Spring Data JPA** (sans relations JPA)
- **MySQL 8.0**
- **Lombok**
- **OpenAPI 3.0** (Swagger UI)
- **Maven**
- **Jakarta Validation**
