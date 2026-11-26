# Architecture Roadmap & Structure du Dépôt — TripMint

Ce document décrit **l'organisation actuelle du monorepo** (déplacée hors du `README.md` pour garder celui-ci concis) ainsi que la **trajectoire d'évolution de l'architecture** au fil des jalons **V1 → V2 → V3 → Phase 4**.

Les justifications détaillées et faiblesses de chaque choix sont documentées dans [ARCHITECTURE.md](ARCHITECTURE.md).

---

## 1. Structure Actuelle du Monorepo (Phase 1 — V1)

```text
tripmint/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   └── user_story.md                  # Modèle de User Story
│   └── workflows/
│       └── ci.yml                         # CI GitHub Actions (Back + Front + Docker Compose)
├── backend/                               # API REST Spring Boot 4 / Spring MVC (Java 25 / Maven)
│   ├── docker-compose.yml                 # PostgreSQL 18 isolé pour le back-end
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/tripmint/
│       │   │   ├── TripmintBackendApplication.java
│       │   │   ├── config/                # Configuration transversale (Clock, ...)
│       │   │   ├── controller/            # Contrôleurs REST (/api/v1/*)
│       │   │   ├── exception/             # GlobalExceptionHandler (@RestControllerAdvice) & ApiError
│       │   │   ├── service/               # Logique métier & frontières @Transactional
│       │   │   ├── repository/            # Accès données Spring Data JPA
│       │   │   └── model/                 # Entités JPA & DTOs (records)
│       │   └── resources/
│       │       ├── application.yml        # Profil local actif par défaut
│       │       ├── application-local.yml  # H2 (MODE=PostgreSQL) + /h2-console + ddl-auto=validate
│       │       ├── application-prod.yml   # PostgreSQL via DB_URL / DB_USERNAME / DB_PASSWORD
│       │       └── db/migration/          # Scripts SQL Flyway (V1__init.sql, ...)
│       └── test/java/com/tripmint/        # Tests unitaires & intégration JUnit 5 / MockMvc
├── frontend/                              # SPA Angular 22+ (Standalone, Zoneless, Signals, SCSS)
│   ├── angular.json
│   ├── proxy.conf.json                    # Redirection /api -> http://localhost:8080
│   └── src/app/
├── docs/
│   ├── README.md                          # Cartographie documentaire
│   ├── USER_STORIES.md                    # EPICs Fonctionnels & Techniques + User Stories
│   ├── COMMIT_LOG.md                      # Journal chronologique des commits
│   ├── ARCHITECTURE.md                    # ADRs, faiblesses & analyses
│   ├── ArchitectureRoadMap.md             # Ce document
│   └── TECHNICAL_TARGETS.md               # Cibles techniques & compétences à travailler
├── AGENTS.md                              # Règles IA, principes code & stratégie de tests
├── .env.example                           # Modèle des variables d'environnement
├── .gitignore
└── README.md                              # Point d'entrée rapide (développeurs & IA)
```

---

## 2. Évolution Architecturale Planifiée

| Phase / Jalon | Déclencheur fonctionnel | Évolution Back-end | Évolution Données & Qualité | Évolution Front-end & Ops |
| :--- | :--- | :--- | :--- | :--- |
| **Phase 1 (V1 — en cours)** | `EPIC-F1` & `EPIC-F2` *(recherche de vols dates fixes & flexibles)* | Ajout de `client/<fournisseur>/` (pattern **Adapter** + interface `FlightProvider`), `FlightSearchController/Service`, frontières `@Transactional` courtes | Flyway `V2__...sql` si `search_logs` évolue ; H2 en local/CI + `backend/docker-compose.yml` PostgreSQL 18 | Formulaires Reactive Forms, état Signals, vues résultats & détail ; `docs/prd/` & `docs/uml/` |
| **Phase 2 (V2)** | `EPIC-F3` *(historique, comptes, alertes)* + `EPIC-T4` *(audit qualité)* | Pivot vers **Package by Feature** (`common/`, `search/`, `pricing/`, `alert/`, `user/`), Spring Security + JWT, tâches `@Scheduled` | **Testcontainers PostgreSQL 18**, requêtes SQL **Window Functions**, **Playwright (E2E)**, **SonarQube** | Intercepteur Auth, Guards, dashboard alertes & tendances ; CD `.github/workflows/cd.yml` sur PostgreSQL managé |
| **Phase 3 (V3)** | `EPIC-F4` *(train + hébergement + scoring)* | Pattern **Strategy** (`TransportProvider`, `StayProvider`), moteur de scoring configurable | Évaluation d'un cache **Redis** si les quotas fournisseurs ou temps de réponse le justifient | Comparateur multi-modal & filtre budget global ; éventuel `frontend/docker-compose.yml` dédié |
| **Phase 4 (Scale)** | Montée en charge réelle (`EPIC-T6`) | **Virtual Threads** bornés (`Semaphore`, timeouts) pour paralléliser les appels fournisseurs ; éventuelle extraction de service si goulot mesuré | Réévaluation NoSQL (**MongoDB** TTL pour offres brutes hétérogènes) ou **Kafka** (événements d'alertes asynchrones) uniquement sur métriques | Images `Dockerfile` multi-stage, déploiement **Kubernetes** (Helm/manifests, probes `/api/v1/health`) |
