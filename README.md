# 📦 Application de Gestion de Commandes

API REST professionnelle de gestion de commandes clients, développée avec **Spring Boot 3**, sécurisée avec **JWT**.

> Projet réalisé dans le cadre du module **Développement d'Applications Backend** — Institut Polytechnique de Saint Louis

---

## 🛠️ Technologies utilisées

- **Java 21**
- **Spring Boot 3.2.2**
- **Spring Security + JWT (JJWT 0.11.5)**
- **Spring Data JPA**
- **MySQL 8.0**
- **Lombok**
- **OpenAPI 3.0 / Swagger UI**
- **Maven**

---

## ⚙️ Prérequis

- Java 21+
- Maven 3.8+
- MySQL 8.0+
- IntelliJ IDEA (recommandé)

---

## 🗄️ Configuration de la base de données

Créer la base de données MySQL :

```sql
CREATE DATABASE commandes_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 🚀 Lancement de l'application

### 1. Cloner le projet

```bash
git clone https://github.com/bolyabdoulaye-sketch/commandes.git
cd commandes
```

### 2. Définir les variables d'environnement

#### Sous Windows (PowerShell) :

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
$env:DB_URL="jdbc:mysql://localhost:3306/commandes_dev"
$env:DB_USERNAME="root"
$env:DB_PASSWORD=""
$env:SERVER_PORT="8080"
$env:JWT_SECRET="cle_secrete_polytech_commandes_2024_tres_longue"
$env:JWT_EXPIRATION="86400000"
```

#### Sous Linux/Mac :

```bash
export SPRING_PROFILES_ACTIVE=dev
export DB_URL=jdbc:mysql://localhost:3306/commandes_dev
export DB_USERNAME=root
export DB_PASSWORD=
export SERVER_PORT=8080
export JWT_SECRET=cle_secrete_polytech_commandes_2024_tres_longue
export JWT_EXPIRATION=86400000
```

### 3. Lancer l'application

```bash
./mvnw spring-boot:run
```

---

## 🔀 Profils Spring

| Profil | Usage | Commande |
|--------|-------|---------|
| `dev` | Développement local (données initialisées automatiquement) | `SPRING_PROFILES_ACTIVE=dev` |
| `test` | Tests automatisés | `SPRING_PROFILES_ACTIVE=test` |
| `prod` | Production | `SPRING_PROFILES_ACTIVE=prod` |

---

## 🔐 Authentification JWT

L'API est sécurisée par **JSON Web Token (JWT)**. Tous les endpoints (sauf `/api/auth/**`) nécessitent un token valide.

### 1. Créer un compte

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "abdoulaye",
  "password": "monmotdepasse"
}
```

**Réponse (201 Created) :**
```json
{
  "message": "Compte créé avec succès pour : abdoulaye"
}
```

### 2. Se connecter et obtenir le token

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "boly",
  "password": "passer123"
}
```

**Réponse (200 OK) :**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "abdoulaye",
  "role": "ROLE_USER",
  "expiresIn": 86400
}
```

### 3. Utiliser le token

Ajouter le token dans le header de chaque requête :

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📚 Documentation Swagger

Une fois l'application démarrée, accéder à la documentation interactive :

```
http://localhost:8080/swagger-ui.html
```

Spécifications OpenAPI :

```
http://localhost:8080/api-docs
```

> 💡 **Dans Swagger**, cliquer sur le bouton **Authorize 🔒** et entrer `Bearer <votre_token>` pour tester les endpoints protégés.

---

## 📋 Exemples d'appels API

### Clients

```http
# Créer un client
POST /api/clients
Authorization: Bearer <token>
Content-Type: application/json

{
  "nom": "Abdoulaye Boly",
  "email": "boly.abdoulaye@ugb.edu.sn"
}

# Lister tous les clients
GET /api/clients
Authorization: Bearer <token>

# Récupérer un client par ID
GET /api/clients/1
Authorization: Bearer <token>
```

### Produits

```http
# Créer un produit
POST /api/produits
Authorization: Bearer <token>
Content-Type: application/json

{
  "nom": "Laptop HP",
  "prix": 750000,
  "stock": 10
}

# Produits avec stock faible
GET /api/produits/stock-faible?seuil=5
Authorization: Bearer <token>
```

### Commandes

```http
# Créer une commande
POST /api/commandes
Authorization: Bearer <token>
Content-Type: application/json

{
  "clientId": 1,
  "lignes": [
    { "produitId": 1, "quantite": 2 },
    { "produitId": 2, "quantite": 1 }
  ]
}

# Valider une commande
PUT /api/commandes/1/valider
Authorization: Bearer <token>

# Annuler une commande
PUT /api/commandes/1/annuler
Authorization: Bearer <token>

# Commandes par client
GET /api/commandes/client/1
Authorization: Bearer <token>

# Commandes par statut
GET /api/commandes/status/CREATED
Authorization: Bearer <token>
```

---

## 🏗️ Architecture du projet

```
com.polytech.commandes
├── entity/          # Entités JPA (Client, Produit, Commande, LigneCommande, Utilisateur)
├── repository/      # Couche d'accès aux données (Spring Data JPA)
├── dto/             # Objets de transfert (AuthRequest, AuthResponse, ...)
├── service/         # Interfaces de la logique métier
│   └── impl/        # Implémentations des services
├── controller/      # Contrôleurs REST (AuthController, ClientController, ...)
├── security/        # JWT (JwtUtil, JwtFilter, UserDetailsServiceImpl)
├── exception/       # Gestion globale des exceptions
├── config/          # Configuration Spring Security (SecurityConfig)
└── CommandesApplication.java
```

---

## 📌 Règles métier implémentées

1. **Stock insuffisant** → commande refusée automatiquement
2. **Total calculé automatiquement** à la création de la commande
3. **Stock mis à jour** après validation d'une commande
4. **Stock restitué** après annulation d'une commande

---

## 👨‍💻 Auteur

**Abdoulaye Boly** — Institut Polytechnique de Saint Louis  
TP encadré par **Dr Samba SIDIBE**
