# TripMint — Optimiseur de Voyages Multi-Critères à Bas Coût

**TripMint** est une application web Full Stack permettant de trouver et d'organiser des voyages au meilleur prix (**avion, puis train et hébergement**) à partir d'un budget maximum et d'une fenêtre de dates fixes ou flexibles.

---

## Vision & Proposition de Valeur

**TripMint** combine la précision d'un comparateur classique et la souplesse d'un moteur d'exploration budgétaire :

* **Recherche multi-critères (Dates fixes & flexibles) :** Recherche directe sur des dates précises ou exploration budgétaire à partir d'une ville de départ, d'un budget plafond et d'une fenêtre de dates.
* **Historisation & Tendances tarifaires :** Suivi des recherches et relevé périodique des prix pour identifier les meilleures opportunités.
* **Architecture pragmatique et évolutive :** Monolithe **Spring MVC** classique et lisible dès la V1, prêt à intégrer de nouveaux fournisseurs (train, hébergement) sans sur-ingénierie.

---

## Stack Technique & Choix d'Architecture

| Couche | Technologies | Points clés d'implémentation |
| :--- | :--- | :--- |
| **Back-end** | **Java 25 (LTS)**, **Spring Boot 4.x** (Spring MVC) | Architecture en couches classique (`controller`, `service`, `client`, `repository`, `model`, `config`), DTOs en Java `record`, validation Bean Validation et `@RestControllerAdvice` global. |
| **Base de données & Migrations** | **Flyway** dès la V1, **H2** (local/tests), **PostgreSQL** (déploiement) | Migrations SQL standard compatibles H2 (`MODE=PostgreSQL`) et PostgreSQL, validation stricte du schéma par Hibernate (`ddl-auto=validate`). |
| **Front-end** | **Angular 22+**, **TypeScript strict**, **SCSS** | **Standalone Components**, détection de changement **Zoneless**, proxy local `/api` → `http://localhost:8080`. |
| **Qualité & CI** | **JUnit 5**, **Spring Boot Test**, **GitHub Actions** | Pipeline CI automatique sur chaque Pull Request vers `main` : `./mvnw test` (démarrage Spring + Flyway sur H2) et `npm ci && npm run build`. |

---

## Structure du Dépôt (Monorepo)

```text
tripmint/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   └── user_story.md         # Modèle de User Story
│   └── workflows/
│       └── ci.yml                # Pipeline CI GitHub Actions (Back + Front)
├── backend/                      # API REST Spring Boot 4 / Spring MVC (Java 25 / Maven)
│   ├── src/main/java/com/tripmint/
│   │   ├── config/               # Configuration globale
│   │   ├── controller/           # Contrôleurs REST & @RestControllerAdvice
│   │   ├── service/              # Logique métier
│   │   ├── client/               # Clients HTTP externes (ex. Amadeus)
│   │   ├── repository/           # Accès données Spring Data JPA
│   │   └── model/                # Entités JPA & DTOs
│   └── src/main/resources/
│       ├── application.yml       # Profil local actif par défaut
│       ├── application-local.yml # H2 en mémoire (mode PostgreSQL) + console H2
│       ├── application-prod.yml  # PostgreSQL via variables d'environnement
│       └── db/migration/         # Scripts SQL Flyway (V1__init.sql, ...)
├── frontend/                     # SPA Angular 22+ (Standalone, Zoneless, SCSS, strict)
│   └── proxy.conf.json           # Redirection /api -> http://localhost:8080
├── docs/                         # Diagrammes UML, schémas MCD et contrats JSON
├── .env.example                  # Modèle de variables d'environnement
├── .gitignore
└── README.md
```

---

## Roadmap Fonctionnelle

### V1 — Socle & Recherche de Vols (MVP)
* Initialisation du monorepo (Spring MVC, Flyway, H2 local / PostgreSQL prod, Angular 22+ Zoneless, CI GitHub Actions).
* Endpoint de santé (`/api/v1/health`), gestion globale des erreurs API et journalisation des recherches (`search_logs`).
* Intégration du fournisseur de vols **Amadeus** (dates fixes puis dates flexibles sous contrainte de budget).
* Interface Angular de recherche de vols et d'affichage des résultats triés par prix.

### V2 — Historisation, Tendances & Comptes Utilisateurs
* Relevé périodique des prix et détection de baisses tarifaires par rapport à l'historique.
* Sauvegarde de recherches favorites et alertes de prix.
* Authentification utilisateur et déploiement continu sur PostgreSQL managé.

### V3 — Extension Multi-Modale (Train & Hébergement)
* Intégration d'un connecteur ferroviaire (comparaison avion vs train).
* Intégration de l'hébergement (hôtels / séjours) pour un calcul de budget global transport + logement.
* Recommandations d'itinéraires optimisés budget / durée.

---

## Démarrage Rapide (Local)

Aucun serveur de base de données externe n'est requis en local : le profil `local` (actif par défaut) démarre automatiquement une base **H2 en mémoire** en mode compatibilité PostgreSQL avec exécution des migrations **Flyway**.

### Prérequis
* **Java 25 (LTS)**
* **Node.js 24 LTS** (ou `>= 22.22.3`) & **npm**

### Lancement
```bash
# 1. Cloner le dépôt
git clone https://github.com/<ton-username>/tripmint.git
cd tripmint

# 2. Copier les variables d'environnement (utile pour Amadeus et le profil prod)
cp .env.example .env

# 3. Lancer le back-end Spring Boot (profil local par défaut, port 8080)
cd backend
./mvnw spring-boot:run
# API health : http://localhost:8080/api/v1/health
# Console H2 : http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:tripmint)

# 4. Lancer le front-end Angular avec proxy /api (dans un second terminal)
cd frontend
npm ci
npm start
# Application : http://localhost:4200
```

### Profil Production (`prod`)
Pour exécuter l'application sur **PostgreSQL**, définir `SPRING_PROFILES_ACTIVE=prod` ainsi que `DB_URL`, `DB_USERNAME` et `DB_PASSWORD` (voir `.env.example`).

---

## Workflow de Développement

1. Chaque fonctionnalité ou tâche technique fait l'objet d'une **User Story** créée depuis `.github/ISSUE_TEMPLATE/user_story.md`.
2. Développement isolé sur une branche dédiée (`feature/<id>-nom-fonctionnalite`).
3. Ouverture d'une **Pull Request** vers `main`, validée automatiquement par la CI GitHub Actions (`./mvnw test` + `npm ci && npm run build`).
