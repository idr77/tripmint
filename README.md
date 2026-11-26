# TripMint — Optimiseur de Voyages Multi-Critères à Bas Coût

**TripMint** est une application web Full Stack qui aide les voyageurs soucieux de leur budget à trouver et organiser des voyages au meilleur prix (**vols en V1**, puis **suivi tarifaire & alertes en V2**, et **train + hébergement en V3**), sur **dates fixes** comme sur **fenêtres de dates flexibles**.

---

## 1. Besoin Métier & Utilisateurs Cibles

Les comparateurs classiques répondent bien à la question *« quel est le vol Paris → Lisbonne du 12 au 16 mai ? »*, mais restent limités lorsqu'un voyageur raisonne **à partir d'un budget plafond et d'une souplesse sur les dates**.

### Utilisateurs cibles (Personas)
* **Le voyageur opportuniste (Cible V1) :** connaît sa ville de départ, dispose d'un budget maximum et d'une fenêtre de dates souple ; veut identifier rapidement les meilleurs créneaux au meilleur prix.
* **Le planificateur attentif aux prix (Cible V2) :** souhaite suivre l'évolution d'un trajet dans le temps, savoir si un tarif est réellement une « bonne affaire » par rapport à son historique et recevoir une alerte lors d'une baisse.
* **L'organisateur de séjour complet (Cible V3) :** souhaite arbitrer entre avion et train et intégrer l'hébergement pour piloter un **budget global** (transport + logement).

---

## 2. Parcours Utilisateurs Clés

Chaque parcours métier se décompose directement en **EPICs** et **User Stories** dans [docs/USER_STORIES.md](docs/USER_STORIES.md) :

1. **Parcours P1 — Recherche directe à dates fixes (V1) :**
   Saisir départ, destination, dates exactes et budget optionnel → consulter les vols triés par prix croissant → ouvrir le détail d'un itinéraire (horaires, escales, compagnie).
2. **Parcours P2 — Exploration budgétaire à dates flexibles (V1) :**
   Saisir ville de départ, destination, budget maximum, durée de séjour et fenêtre de dates → découvrir automatiquement les combinaisons de dates les moins chères sous le plafond.
3. **Parcours P3 — Veille tarifaire & alertes (V2) :**
   Créer un compte → enregistrer une recherche favorite → visualiser la tendance par rapport au prix moyen historique → être alerté lorsqu'un tarif passe sous le seuil cible.
4. **Parcours P4 — Comparaison multi-modale & budget global (V3) :**
   Comparer avion et train sur un même écran → ajouter l'hébergement → obtenir des recommandations optimisées *prix / durée*.

---

## 3. Découpage par Versions & EPICs

> Le tableau complet type JIRA (**EPICs → User Stories → Statuts → Commits**) est tenu à jour dans **[docs/USER_STORIES.md](docs/USER_STORIES.md)**.

| Version | Objectif produit | EPICs associés | Hors périmètre volontaire |
| :--- | :--- | :--- | :--- |
| **V1 — MVP Recherche de Vols** | Livrer rapidement un moteur de recherche de vols fiable (dates fixes & flexibles) avec socle propre, tests et CI | **EPIC-01** Socle, Qualité & CI/CD<br>**EPIC-02** Recherche de Vols (Amadeus + UI Angular) | Authentification, Virtual Threads, NoSQL, Kubernetes |
| **V2 — Historique, Alertes & Qualité avancée** | Suivre les prix dans le temps, détecter les baisses, fidéliser les utilisateurs et industrialiser la qualité | **EPIC-03** Historisation & Tendances<br>**EPIC-04** Comptes, Alertes, E2E Playwright & SonarQube | Multi-modalité, orchestration K8s |
| **V3 / Phase 3–4 — Multi-Modal & Passage à l'échelle** | Comparer avion + train + hébergement et faire évoluer l'infrastructure si la charge le justifie | **EPIC-05** Extension Multi-Modale (Train & Hébergement)<br>**EPIC-06** Scalabilité & Ops (Virtual Threads, Kubernetes, étude NoSQL) | — |

---

## 4. Architecture Déduite du Besoin

L'architecture n'est pas choisie « pour la techno », mais **déduite des contraintes de la V1** (simplicité, rapidité de livraison, testabilité, évolutivité maîtrisée). Les décisions et leurs **faiblesses/compromis** sont détaillés dans **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)**.

| Couche | Choix V1 | Pourquoi ce choix pour la V1 |
| :--- | :--- | :--- |
| **Back-end** | **Java 25 (LTS)**, **Spring Boot 4.x**, **Spring MVC classique** | Monolithe en couches simple (`controller`, `service`, `client`, `repository`, `model`, `config`), DTOs `record`, `@RestControllerAdvice` global, sans surcoût Spring Modulith/microservices ni Virtual Threads prématurés. |
| **Données & Migrations** | **Flyway** dès la V1, **H2** (`MODE=PostgreSQL`) en local/tests, **PostgreSQL 18** via Docker Compose & `prod` | Démarrage instantané en local et en CI sur H2, même SQL standard validé par `ddl-auto=validate`, et environnement PostgreSQL 18 reproductible via `docker-compose.yml`. |
| **Front-end** | **Angular 22+**, **Standalone Components**, **Zoneless**, **Signals**, **SCSS**, TypeScript strict | Architecture Angular moderne, légère et réactive (sans `zone.js` ni `NgModule`), proxy local `/api` → `http://localhost:8080`. |
| **Qualité & CI/CD** | **JUnit 5**, **Vitest**, **GitHub Actions**, puis **Playwright (E2E)** & **SonarQube** | Validation automatique sur chaque PR vers `develop` et `main` (`./mvnw test`, `npm test`, `npm run build`, `docker compose config`). |

---

## 5. Structure du Dépôt (Monorepo)

```text
tripmint/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   └── user_story.md         # Modèle de User Story
│   └── workflows/
│       └── ci.yml                # Pipeline CI GitHub Actions (Back + Front + Docker)
├── backend/                      # API REST Spring Boot 4 / Spring MVC (Java 25 / Maven)
│   ├── src/main/java/com/tripmint/
│   │   ├── config/               # Configuration globale (Clock, ...)
│   │   ├── controller/           # Contrôleurs REST & @RestControllerAdvice
│   │   ├── service/              # Logique métier & frontières transactionnelles
│   │   ├── client/               # Clients HTTP externes isolés (ex. client.amadeus)
│   │   ├── repository/           # Accès données Spring Data JPA
│   │   └── model/                # Entités JPA & DTOs (records)
│   └── src/main/resources/
│       ├── application.yml       # Profil local actif par défaut
│       ├── application-local.yml # H2 en mémoire (MODE=PostgreSQL) + /h2-console
│       ├── application-prod.yml  # PostgreSQL via variables d'environnement
│       └── db/migration/         # Scripts SQL Flyway (V1__init.sql, ...)
├── frontend/                     # SPA Angular 22+ (Standalone, Zoneless, Signals, SCSS)
│   └── proxy.conf.json           # Redirection /api -> http://localhost:8080
├── docs/
│   ├── README.md                 # Index de la documentation
│   ├── ARCHITECTURE.md           # Décisions d'architecture, faiblesses & analyses (ADR)
│   └── USER_STORIES.md           # Suivi JIRA (EPICs, User Stories, statuts & commits)
├── AGENTS.md                     # Principes KISS/DRY/SOLID, règles IA & workflow
├── docker-compose.yml            # PostgreSQL 18 isolé (volume persistant + healthcheck)
├── .env.example                  # Modèle de variables d'environnement
├── .gitignore
└── README.md
```

---

## 6. Démarrage Rapide (Local)

Le profil `local` (actif par défaut) démarre immédiatement sur une base **H2 en mémoire** (`MODE=PostgreSQL`) avec exécution automatique des migrations **Flyway**. Pour tester sur une véritable instance **PostgreSQL 18** en local, `docker-compose.yml` est disponible à la racine.

### Prérequis
* **Java 25 (LTS)**
* **Node.js 24 LTS** (ou `>= 22.22.3`) & **npm**
* **Docker** & **Docker Compose** *(pour PostgreSQL 18 en local)*

### Lancement
```bash
# 1. Cloner le dépôt
git clone https://github.com/idr77/tripmint.git
cd tripmint

# 2. Copier les variables d'environnement
cp .env.example .env

# 3. (Optionnel) Démarrer PostgreSQL 18 via Docker Compose
docker compose up -d
# Vérifier l'état (healthy) : docker compose ps

# 4. Lancer le back-end Spring Boot (profil local H2 par défaut, port 8080)
cd backend
./mvnw spring-boot:run
# API health : http://localhost:8080/api/v1/health
# Console H2 : http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:tripmint)

# 5. Lancer le front-end Angular avec proxy /api (dans un second terminal)
cd frontend
npm ci
npm start
# Application : http://localhost:4200
```

### Exécution sur PostgreSQL 18 (Docker Compose ou Production)
Pour connecter le back-end à **PostgreSQL 18**, définir `SPRING_PROFILES_ACTIVE=prod` avec `DB_URL`, `DB_USERNAME` et `DB_PASSWORD` (voir `.env.example`).

---

## 7. Workflow de Développement & Qualité

1. Chaque fonctionnalité est rattachée à un **EPIC** et une **User Story** suivis dans [docs/USER_STORIES.md](docs/USER_STORIES.md) (et créés depuis [.github/ISSUE_TEMPLATE/user_story.md](.github/ISSUE_TEMPLATE/user_story.md)).
2. Développement sur une branche dédiée depuis `develop` (`feature/<id>-...`, `fix/<id>-...`, `chore/<id>-...`).
3. À chaque commit : mise à jour du suivi JIRA dans [docs/USER_STORIES.md](docs/USER_STORIES.md).
4. Avant chaque PR : **revue critique** des décisions techniques et de leurs faiblesses/compromis.
5. **Pull Request vers `develop`** validée automatiquement par la CI GitHub Actions (`./mvnw test`, `npm test`, `npm run build`, `docker compose config`).
6. À chaque jalon (**V1**, **V2**, **V3**…) : **Pull Request de `develop` vers `main`** pour livrer la version.
