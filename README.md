# TripMint — Optimiseur de Voyages Multi-Critères à Bas Coût

**TripMint** est une application web Full Stack permettant de trouver et d'organiser des voyages au meilleur prix (**avion, train, et à terme hébergement**) à partir d'un budget maximum et d'une fenêtre de dates flexibles.

---

## Vision & Proposition de Valeur

**TripMint** combine la précision d'un comparateur classique et la puissance d'un moteur d'exploration budgétaire :
* **Double mode de recherche (Dates fixes & flexibles) :** Recherche directe sur des dates précises (aller simple ou aller-retour exact) ou recherche exploratoire à partir d'une ville de départ, d'un plafond budgétaire, d'une durée de séjour et d'une plage de dates flexibles.
* **Historique & Tendances tarifaires :** Relevé périodique des prix afin de détecter les baisses significatives par rapport à la moyenne historique d'un trajet.
* **Architecture extensible multi-fournisseurs :** Conception modulaire permettant d'agréger différents modes de transport (vols, trains) et d'intégrer l'hébergement (hôtels, appartements) sans refonte du moteur de recherche.
---

## Stack Technique & Choix d'Architecture

| Couche | Technologies | Points clés d'implémentation |
| :--- | :--- | :--- |
| **Back-end** | **Java 25 (LTS)**, **Spring Boot 4.x**, **Spring Framework 7.x** | API RESTful, `Records` pour les DTOs, `Sealed Classes`, parallélisation des appels fournisseurs via **Virtual Threads** et *Scoped Values*, authentification JWT. |
| **Architecture** | **Monolithe Modulaire (Spring Modulith)**, **SOLID**, **Design Patterns (Strategy / Adapter)** | Abstraction des sources de données (`TransportProvider`, `StayProvider`) permettant l'ajout indépendant des connecteurs Avion, Train et Hébergement. |
| **Base de données** | **PostgreSQL 18+**, **Spring Data JPA 5**, **Flyway** | Migrations SQL versionnées, prévention du problème **N+1** (`JOIN FETCH`, `@EntityGraph`), indexation composite et **Window Functions** pour l'analyse des prix. |
| **Front-end** | **Angular 22+**, **TypeScript 5.8+**, **RxJS** | **Standalone Components**, détection de changement **Zoneless**, gestion d'état réactive avec **Angular Signals**, **Reactive Forms** avec validateurs personnalisés. |
| **Tests & Qualité** | **JUnit 5**, **Mockito**, **Testcontainers**, **Jasmine** | Tests unitaires sur le moteur de scoring/budget, tests d'intégration API/BDD (`@WebMvcTest`, `@DataJpaTest`) et tests de composants/services Angular. |
| **DevOps & CI/CD** | **Docker Compose**, **GitHub Actions** | Environnement local conteneurisé (PostgreSQL) et pipeline CI automatisé sur chaque Pull Request (build + tests Back & Front). |

---

## Structure du Dépôt (Monorepo)

```text
tripmint/
├── .github/
│   ├── ISSUE_TEMPLATE/        # Modèles de User Stories
│   └── workflows/             # Pipelines CI/CD GitHub Actions
├── backend/                   # API REST Spring Boot 4 (Java 25 / Maven)
│   ├── src/main/java/...
│   └── src/main/resources/
│       └── db/migration/      # Scripts SQL Flyway
├── frontend/                  # Application SPA Angular 22+
├── docs/                      # Architecture, schémas MCD et décisions techniques
├── docker-compose.yml         # Infrastructure locale (PostgreSQL 18+)
├── .env.example               # Variables d'environnement requises
└── README.md
```

---

## Roadmap Technique

* [ ] **Phase 1 — Socle & API Core :** Initialisation du monorepo, modélisation PostgreSQL/Flyway, API REST de gestion des recherches flexibles, intégration d'un premier fournisseur de vols (sandbox) avec parallélisation via Virtual Threads.
* [ ] **Phase 2 — Historisation, SQL Avancé & CI/CD :** Tâche planifiée de suivi des tarifs, requêtes analytiques PostgreSQL (tendances et variations de prix), couverture de tests JUnit/Mockito/Testcontainers et pipeline GitHub Actions.
* [ ] **Phase 3 — Interface Utilisateur Angular :** Développement du dashboard de recherche multi-critères (Reactive Forms), affichage dynamique des résultats piloté par Signals/Zoneless et fiche détaillée d'itinéraire.
* [ ] **Phase 4 — Extensions Multi-Modales (V2) :** Ajout du connecteur ferroviaire (train), module de comparaison d'hébergements et alertes automatiques de baisse de prix.

---

## Démarrage Rapide (Local)

### Prérequis
* **Java 25 (LTS)** & **Maven 3.9+**
* **Node.js 24 LTS** & **Angular CLI**
* **Docker** & **Docker Compose**

### Installation
```bash
# 1. Cloner le dépôt
git clone [https://github.com/](https://github.com/)<ton-username>/tripmint.git
cd tripmint

# 2. Configurer les variables d'environnement
cp .env.example .env

# 3. Démarrer la base de données PostgreSQL
docker compose up -d

# 4. Lancer le back-end Spring Boot
cd backend && ./mvnw spring-boot:run

# 5. Lancer le front-end Angular (dans un second terminal)
cd frontend && npm install && ng serve
```

---

## Workflow de Développement

Le projet est piloté via **GitHub Projects** :
1. Chaque fonctionnalité ou tâche technique fait l'objet d'une **User Story** (Issue).
2. Développement isolé sur une branche dédiée (`feature/<id>-nom-fonctionnalite`).
3. Ouverture d'une **Pull Request** vers `main` soumise à la validation automatique de la CI avant merge.
