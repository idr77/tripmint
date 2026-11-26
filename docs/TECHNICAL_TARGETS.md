# Cibles Techniques & Compétences à Travailler — TripMint

Ce fichier liste les **compétences et sujets techniques précis** que je souhaite pratiquer, maîtriser et savoir défendre (en revue de code et en mentorat) à travers la construction de **TripMint**.

> **Principe :** chaque cible technique est rattachée à une **phase** et à une **EPIC technique** ([USER_STORIES.md](USER_STORIES.md)) afin de ne jamais introduire une technologie hors contexte.

---

| Domaine | Cible technique précise | Phase / EPIC | Application concrète dans TripMint | Statut |
| :--- | :--- | :---: | :--- | :--- |
| **Architecture Back-end** | Spring MVC propre → pivot *Package by Feature* / Monolithe modulaire (et comprendre les limites face à l'Hexagonale / Microservices) | V1 (`EPIC-T1`) → V2 (`EPIC-T5`) | Séparation stricte `controller` / `exception` / `service` / `client` / `repository` / `model` puis découpage par domaine | 🟡 En cours |
| **Conception & POO** | Principes **SOLID**, **KISS**, **DRY** et Design Patterns (**Adapter**, **Strategy**, **DTO/Record**) | V1 (`EPIC-T2`) → V3 (`EPIC-T6`) | `FlightProvider` + Adapter fournisseur externe, puis `TransportProvider` / `StayProvider` en Strategy | 🟡 En cours |
| **API REST & Erreurs** | Contrats REST `/api/v1/*`, validation `@Valid`, codes HTTP cohérents, `@RestControllerAdvice` (`ApiError`) | V1 (`EPIC-T1`, `EPIC-T2`) | `GlobalExceptionHandler` dans `com.tripmint.exception`, gestion des timeouts/erreurs fournisseur | 🟡 En cours |
| **Transactions & JPA** | `@Transactional` (`readOnly`, propagation, rollback), `open-in-view: false`, prévention **N+1** (`JOIN FETCH`, `@EntityGraph`), cycle de vie JPA (`@PrePersist`, etc.) | V1 (`EPIC-T2`) → V2 (`EPIC-T5`) | Journalisation `search_logs` hors appel HTTP externe, requêtes optimisées sur favoris/historique | ⚪ À travailler |
| **SQL & Migrations** | **Flyway** dès le début, SQL standard H2/PostgreSQL, puis **SQL avancé PostgreSQL** (indexation composite, **Window Functions**, `JSONB`) | V1 (`EPIC-T1`) → V2 (`EPIC-T5`) | `V1__init.sql`, puis requêtes analytiques de tendances tarifaires sur `price_snapshots` | 🟡 En cours |
| **Sécurité** | Authentification **Spring Security + JWT**, rôles sécurisés, hachage mots de passe, protection routes Angular | V2 (`EPIC-T5`) | Inscription, connexion, favoris & alertes protégés par utilisateur | ⚪ Planifié |
| **Front-end Angular** | **Angular 22+ Standalone, Zoneless, Signals** vs historique (`Zone.js`, `OnPush`, `RxJS / BehaviorSubject`), **Reactive Forms** typés | V1 (`EPIC-T1`, `EPIC-T2`) | Formulaire recherche multi-critères, gestion d'état par Signals + RxJS pour HTTP | 🟡 En cours |
| **Stratégie de Tests** | Tests unitaires (logique complexe), tests d'intégration (MockMvc, Flyway, **Testcontainers PostgreSQL 18**), tests E2E **Playwright** | V1 (`EPIC-T2`) → V2 (`EPIC-T4`) | Couverture complète du moteur dates flexibles, erreurs/transactions, et parcours P1/P2 en E2E | 🟡 En cours |
| **Qualité Continue** | Analyse statique **SonarQube / SonarCloud**, couverture JaCoCo + Vitest, Quality Gate | V2 (`EPIC-T4`) | Intégration CI GitHub Actions sur les PR | ⚪ Planifié |
| **Git & CI/CD** | Monorepo Git (`feature/*` → `develop`, jalons → `main`), commits atomiques, **GitHub Actions** CI puis CD | V1 (`EPIC-T1`) → V2 (`EPIC-T5`) | `.github/workflows/ci.yml` + protection de branches + futur `cd.yml` | 🟢 Socle acquis |
| **Conteneurs & Infra** | **Docker Compose** isolé (`backend/docker-compose.yml`), `Dockerfile` multi-stage, **Kubernetes** (Phase 3–4) | V1 (`EPIC-T1`) → Ph. 4 (`EPIC-T6`) | PostgreSQL 18 conteneurisé en V1, puis manifests/Helm K8s en Phase 3–4 | 🟡 En cours |
| **Bonus / Challenge** | **Cache Redis**, **Kafka**, **NoSQL (MongoDB / Spark)**, **Virtual Threads** | V3 / Ph. 4 (`EPIC-T6`) | Déjà challengés dans `ARCHITECTURE.md` ; à n'introduire que sur besoin mesuré | ⚪ En veille |
