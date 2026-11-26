# TripMint — Monorepo Full Stack

**TripMint** est un optimiseur de voyages multi-critères à bas coût (dates fixes et flexibles sous contrainte de budget).

Ce README sert de **point d'entrée rapide (développeurs & agents IA)**. La vision produit détaillée, l'arborescence complète et les décisions d'architecture sont isolées dans leurs documents dédiés ci-dessous afin d'éviter toute redondance.

---

## Navigation de la Documentation (Sources de Vérité)

| Document | Rôle unique |
| :--- | :--- |
| **[AGENTS.md](AGENTS.md)** | Règles d'ingénierie (KISS/DRY/SOLID), conventions IA, stratégie de tests détaillée, règles erreurs/transactions et workflow PR |
| **[docs/README.md](docs/README.md)** | Cartographie des liens entre documentations + emplacement des futurs **PRD** et **diagrammes UML** |
| **[docs/USER_STORIES.md](docs/USER_STORIES.md)** | Backlog structuré : **EPICs Fonctionnels → US Fonctionnelles → PRD → EPICs Techniques → US Techniques** (priorités & charges) |
| **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)** | Décisions d'architecture (**ADR**) reliées aux besoins fonctionnels, **faiblesses/compromis** et analyses (Signals, NoSQL, fournisseurs) |
| **[docs/ArchitectureRoadMap.md](docs/ArchitectureRoadMap.md)** | **Structure du dépôt** et feuille de route d'évolution architecturale (Phase 1 → Phase 4) |
| **[docs/TECHNICAL_TARGETS.md](docs/TECHNICAL_TARGETS.md)** | Cibles techniques & compétences à travailler sur le projet (SQL, Patterns, Playwright, SonarQube, K8s…) |
| **[docs/COMMIT_LOG.md](docs/COMMIT_LOG.md)** | Journal chronologique des commits reliés aux EPICs / User Stories |

---

## Stack Technique (V1)

* **Back-end (`backend/`) :** Java 25 (LTS), Spring Boot 4.1.x (Spring MVC classique), Spring Data JPA, Flyway, H2 (`MODE=PostgreSQL` en local/tests), PostgreSQL 18 (`backend/docker-compose.yml` & profil `prod`).
* **Front-end (`frontend/`) :** Angular 22+ (Standalone Components, Zoneless, Angular Signals, SCSS, TypeScript strict), Vitest, proxy local `/api` → `http://localhost:8080`.
* **CI/CD (`.github/workflows/ci.yml`) :** GitHub Actions sur `pull_request` et `push` vers `develop` et `main` (`./mvnw test`, `npm test`, `npm run build`, validation `backend/docker-compose.yml`).

---

## Démarrage Rapide (Local)

### Prérequis
* **Java 25 (LTS)**
* **Node.js 24 LTS** (ou `>= 22.22.3`) & **npm**
* **Docker** & **Docker Compose** *(optionnel en local H2, requis pour PostgreSQL 18)*

### Commandes essentielles
```bash
# 1. Configurer les variables d'environnement locales (jamais commitées)
cp .env.example .env

# 2. (Optionnel) Démarrer PostgreSQL 18 dédié au back-end
docker compose -f backend/docker-compose.yml --env-file .env up -d
docker compose -f backend/docker-compose.yml ps

# 3. Lancer le back-end (profil local H2 actif par défaut, port 8080)
cd backend && ./mvnw spring-boot:run
# Health : http://localhost:8080/api/v1/health
# H2 Console : http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:tripmint)

# 4. Lancer le front-end Angular avec proxy /api (second terminal, port 4200)
cd frontend && npm ci && npm start
```

> **Profil PostgreSQL (`prod`) :** exécuter avec `SPRING_PROFILES_ACTIVE=prod` et les variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (voir [.env.example](.env.example)).

### Commandes de validation (CI locale)
```bash
# Back-end
cd backend && ./mvnw test

# Front-end
cd frontend && npm test -- --watch=false && npm run build

# Docker Compose back-end
docker compose -f backend/docker-compose.yml --env-file .env.example config --quiet
```

---

## Workflow Git

1. **Branches de travail** créées depuis `develop` : `feature/<id>-...`, `fix/<id>-...`, `chore/<id>-...`.
2. **Pull Requests courantes → `develop`** (1 PR = 1 périmètre cohérent, revue critique obligatoire avant merge, CI verte).
3. **Pull Requests de jalon (`V1`, `V2`, `V3`…) → `main`** depuis `develop`.
