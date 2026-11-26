# Suivi Projet (JIRA) — EPICs, User Stories & Commits

Ce document sert de **tableau de suivi type JIRA** pour **TripMint**. Conformément à [AGENTS.md](../AGENTS.md), **chaque commit** doit mettre à jour le statut des User Stories concernées et enrichir le journal des commits ci-dessous.

> **Rappel du workflow Git :**
> * Branches de travail depuis `develop` (`feature/<id>-...`, `fix/<id>-...`, `chore/<id>-...`) → **PR vers `develop`**.
> * Livraisons de jalons (**V1**, **V2**, **V3**…) → **PR de `develop` vers `main`**.

---

## 1. Vue d'ensemble par EPIC

| EPIC | Jalon | Parcours / Objectif | US | Statut |
| :--- | :---: | :--- | :---: | :--- |
| **EPIC-01** | **V1** | Socle Monorepo, Conventions, Docker & CI | `US-01` → `US-04` | 🟡 En cours *(prêt à merger sur `develop`)* |
| **EPIC-02** | **V1** | Moteur de Recherche de Vols (Parcours P1 & P2) | `US-05` → `US-10` | ⚪ À faire |
| **EPIC-03** | **V2** | Historisation & Tendances Tarifaires (Parcours P3) | `US-11` → `US-12` | ⚪ À faire |
| **EPIC-04** | **V2** | Comptes, Alertes, Qualité (Playwright / SonarQube) & Prod | `US-13` → `US-17` | ⚪ À faire |
| **EPIC-05** | **V3** | Extension Multi-Modale : Train & Hébergement (Parcours P4) | `US-18` → `US-20` | ⚪ À faire |
| **EPIC-06** | **Phase 3–4** | Scalabilité & Ops (Virtual Threads, Kubernetes, étude NoSQL) | `US-21` → `US-22` | ⚪ À faire |

---

## 2. Tableau de Bord (Issues / User Stories)

| ID | EPIC | Jalon | Titre | Issue GitHub | Statut | Commits liés |
| :--- | :---: | :---: | :--- | :---: | :--- | :--- |
| **US-01** | EPIC-01 | V1 | Configuration du dépôt Git, `AGENTS.md`, doc métier/ADR & CI (`develop`/`main`) | `#1` | 🟡 Prêt à merger *(protections GitHub à vérifier)* | `dc00ded`, `1d79c98`, `2d9339a`, `25a6d37` |
| **US-02** | EPIC-01 | V1 | Environnement local PostgreSQL 18 via Docker Compose | `#2` | 🟡 Prêt à merger sur `develop` *(validé en runtime + Flyway `prod` OK)* | `329574f` |
| **US-03** | EPIC-01 | V1 | Socle Back-end Spring MVC (Java 25, Spring Boot 4, Flyway, H2/PostgreSQL, erreurs) | `#3` *(adaptée)* | 🟡 Prêt à merger sur `develop` | `dc00ded` |
| **US-04** | EPIC-01 | V1 | Socle Front-end Angular 22+ Standalone Zoneless / Signals & proxy `/api` | — | 🟡 Prêt à merger sur `develop` | `dc00ded` |
| **US-05** | EPIC-02 | V1 | Contrats JSON `/api/v1/*`, diagrammes UML & modèle de données V1 (`docs/`) | — | ⚪ À faire | — |
| **US-06** | EPIC-02 | V1 | Intégration client Amadeus (OAuth2 + vols dates fixes + Adapter) | — | ⚪ À faire | — |
| **US-07** | EPIC-02 | V1 | API REST de recherche à dates fixes, transactions & journalisation (`search_logs`) | — | ⚪ À faire | — |
| **US-08** | EPIC-02 | V1 | Moteur de recherche à dates flexibles sous contrainte de budget | — | ⚪ À faire | — |
| **US-09** | EPIC-02 | V1 | Interface Angular : formulaire (Reactive Forms + Signals) & résultats triés par prix | — | ⚪ À faire | — |
| **US-10** | EPIC-02 | V1 | Détail d'itinéraire, gestion des états/erreurs UI & release **V1** (`develop` → `main`) | — | ⚪ À faire | — |
| **US-11** | EPIC-03 | V2 | Relevé périodique & historisation des prix (`price_snapshots`) | — | ⚪ À faire | — |
| **US-12** | EPIC-03 | V2 | Analyse de tendances (Window Functions PostgreSQL) & détection de baisses | — | ⚪ À faire | — |
| **US-13** | EPIC-04 | V2 | Authentification utilisateur (JWT & Spring Security) | — | ⚪ À faire | — |
| **US-14** | EPIC-04 | V2 | Recherches favorites & alertes automatiques de baisse de prix | — | ⚪ À faire | — |
| **US-15** | EPIC-04 | V2 | Tests E2E **Playwright** sur les parcours critiques (P1 & P2) | — | ⚪ À faire | — |
| **US-16** | EPIC-04 | V2 | Analyse qualité continue **SonarQube / SonarCloud** dans la CI | — | ⚪ À faire | — |
| **US-17** | EPIC-04 | V2 | Déploiement continu sur PostgreSQL managé (`prod`) & release **V2** | — | ⚪ À faire | — |
| **US-18** | EPIC-05 | V3 | Abstraction multi-fournisseurs (Strategy) & connecteur ferroviaire (train) | — | ⚪ À faire | — |
| **US-19** | EPIC-05 | V3 | Module hébergement (`StayProvider`) & calcul du budget global transport + logement | — | ⚪ À faire | — |
| **US-20** | EPIC-05 | V3 | Recommandations d'itinéraires optimisés budget / durée & release **V3** | — | ⚪ À faire | — |
| **US-21** | EPIC-06 | Ph. 3–4 | Parallélisation multi-fournisseurs (Virtual Threads bornés) & benchmark | — | ⚪ À faire | — |
| **US-22** | EPIC-06 | Ph. 3–4 | Conteneurisation complète & déploiement **Kubernetes** (+ réévaluation NoSQL) | — | ⚪ À faire | — |

---

## 3. Journal des Commits (Rempli par l'IA à chaque commit)

| Date | Commit | Branche | US / EPIC | Résumé |
| :--- | :--- | :--- | :---: | :--- |
| 2026-11-19 | `dc00ded` | `chore/init-skeleton` | US-01, US-03, US-04 / EPIC-01 | Initialisation du squelette monorepo V1 (Spring MVC Java 25, Flyway/H2/PostgreSQL, Angular 22.3 Zoneless, `AGENTS.md`, CI) |
| 2026-11-25 | `1d79c98` | `chore/init-skeleton` | US-01 / EPIC-01 | Alignement workflow PR (`develop` pour features, `main` pour jalons), déclenchement CI sur `develop`/`main` et création du backlog |
| 2026-11-26 | `329574f` | `chore/init-skeleton` | US-02 / EPIC-01 | Ajout de `docker-compose.yml` (`postgres:18-alpine`, volume `/var/lib/postgresql`, healthcheck `pg_isready`) et variables `DB_NAME`/`DB_PORT` |
| 2026-11-26 | `2d9339a` | `chore/init-skeleton` | US-01 / EPIC-01 | Enrichissement de la CI (tests Vitest, validation Docker Compose, `push` sur `develop`/`main`, `concurrency`) |
| 2026-11-26 | `25a6d37` | `chore/init-skeleton` | US-01 / EPIC-01 | Recentrage métier du `README.md` (personas, parcours, EPICs → archi), ajout `docs/ARCHITECTURE.md` (ADR, faiblesses, Signals, NoSQL, transactions), suivi JIRA et règles IA dans `AGENTS.md` |
| 2026-11-26 | *(commit courant)* | `chore/init-skeleton` | US-02 / EPIC-01 | Validation runtime Docker Compose PostgreSQL 18.7 (`healthy`, persistance volume après redémarrage, tests Spring Boot/Flyway en profil `prod` OK) |

---

## 4. Alignement avec les Issues GitHub existantes (`#1`, `#2`, `#3`)

| Issue GitHub | Titre actuel | Statut / Action recommandée |
| :--- | :--- | :--- |
| **#1** | *Mise en place du dépot Git* | **Alignée sur US-01** — Tous les fichiers dépôt/doc/CI sont prêts sur `chore/init-skeleton`. Activer les protections de branches sur `develop` et `main` dans GitHub puis fermer au merge vers `develop`. |
| **#2** | *Mise en place de l'environnement local PostgreSQL via Docker Compose* | **Alignée sur US-02** — Validée en runtime (`postgres:18-alpine` `healthy`, persistance volume après restart, migrations Flyway + Hibernate `validate` OK en profil `prod`). Prête à fermer au merge vers `develop`. |
| **#3** | *Initialisation du squelette Back-end Spring Boot 4 & Spring Modulith* | **Adaptée en US-03 (Spring MVC classique)** — Recentrage V1 sans Spring Modulith, sans Virtual Threads et sans Auth. Mettre à jour le titre/description de `#3` puis fermer au merge vers `develop`. |

---

## 5. Détail des User Stories

### EPIC-01 — Socle Monorepo, Conventions, Docker & CI (V1)

#### US-01 — Configuration du dépôt Git, conventions, documentation & CI
* **Statut :** 🟡 Prêt à merger sur `develop` *(Issue `#1`)*
* **Besoin :** En tant que développeur, je veux un dépôt structuré autour du besoin métier, des conventions IA (`AGENTS.md`), des ADR et d'une CI automatisée sur `develop`/`main`, afin de garantir qualité et traçabilité dès le départ.
* **Critères d'acceptation :**
  - [x] `.gitignore` complet (Java/Maven, Node/Angular, `.env`, IDE/OS)
  - [x] `.env.example` documentant toutes les variables requises
  - [x] `README.md` structuré *Besoin métier → Personas → Parcours → EPICs → Architecture*
  - [x] `docs/ARCHITECTURE.md` (ADR, faiblesses/compromis, transactions, Signals vs historique, challenge MongoDB/Spark)
  - [x] `AGENTS.md` (KISS/DRY/SOLID, erreurs/transactions, mise à jour JIRA par commit, revue critique avant PR)
  - [x] `.github/workflows/ci.yml` (tests back + tests/build front + validation Docker Compose sur `develop` et `main`)
  - [ ] Protections de branches activées sur GitHub (`develop` et `main`)
  - [ ] PR `chore/init-skeleton` → `develop` mergée avec CI verte

#### US-02 — Environnement local PostgreSQL 18 via Docker Compose
* **Statut :** 🟡 Prêt à merger sur `develop` *(Issue `#2`)*
* **Besoin :** En tant que développeur back-end, je veux démarrer une instance PostgreSQL 18 isolée via Docker Compose, afin de tester mon application sur la vraie base cible sans installation système.
* **Critères d'acceptation :**
  - [x] `docker-compose.yml` créé avec `postgres:18-alpine`
  - [x] Identifiants, nom de base et port (`DB_PORT`, défaut `5432`) configurés via `.env` / `.env.example`
  - [x] Volume persistant `tripmint_postgres_data:/var/lib/postgresql` (conforme PostgreSQL 18)
  - [x] Healthcheck `pg_isready` configuré et validé par `docker compose config`
  - [x] Démarrage `docker compose up -d`, statut `healthy`, persistance volume après redémarrage et exécution Flyway/Hibernate (`SPRING_PROFILES_ACTIVE=prod`) validés sur Docker Desktop
  - [ ] Mergé dans `develop`

#### US-03 — Socle Back-end Spring MVC + Flyway (H2 / PostgreSQL)
* **Statut :** 🟡 Prêt à merger sur `develop` *(Issue `#3` adaptée)*
* **Besoin :** En tant que développeur back-end, je veux un socle Spring Boot 4 / Java 25 en Spring MVC classique avec Flyway, H2 local et gestion d'erreurs centralisée, afin de développer la V1 rapidement et proprement.
* **Critères d'acceptation :**
  - [x] Projet Maven Java 25 + Spring Boot 4.1.2 (`webmvc`, `data-jpa`, `validation`, `flyway`, `h2`, `postgresql`, `test`)
  - [x] Arborescence `com.tripmint` (`config`, `controller`, `service`, `client`, `repository`, `model`)
  - [x] `GET /api/v1/health` et `@RestControllerAdvice` global (`GlobalExceptionHandler` → `ApiError`)
  - [x] Profils `local` (H2 `MODE=PostgreSQL`, `/h2-console`, `ddl-auto=validate`) et `prod` (PostgreSQL env vars, `ddl-auto=validate`)
  - [x] Migration `V1__init.sql` (`search_logs`) + entité `SearchLog`
  - [x] `./mvnw test` au vert (3/3 tests d'intégration)
  - [ ] Mergé dans `develop`

#### US-04 — Socle Front-end Angular 22+ Standalone Zoneless
* **Statut :** 🟡 Prêt à merger sur `develop`
* **Besoin :** En tant que développeur front-end, je veux une SPA Angular 22+ Standalone Zoneless / Signals avec proxy `/api`, afin de bâtir l'interface V1 sur une base moderne et stricte.
* **Critères d'acceptation :**
  - [x] Angular 22.3 Standalone, Zoneless (sans `zone.js`), SCSS, TypeScript strict (`strict: true`, `strictTemplates: true`)
  - [x] `proxy.conf.json` (`/api` → `http://localhost:8080`) branché dans `angular.json`
  - [x] Composant d'accueil minimal TripMint + tests Vitest passants
  - [x] `npm test -- --watch=false` et `npm run build` au vert
  - [ ] Mergé dans `develop`

---

### EPIC-02 — Moteur de Recherche de Vols (V1 — Parcours P1 & P2)

#### US-05 — Contrats API V1, diagrammes UML & modèle de données (`docs/`)
* **Statut :** ⚪ À faire
* **Besoin :** En tant que développeur Full Stack, je veux formaliser les contrats JSON et schémas avant de coder la recherche, afin d'aligner back-end, front-end et gestion d'erreurs.
* **Critères d'acceptation :**
  - [ ] Contrat JSON `/api/v1/flights/search` (requête, réponse, codes HTTP & `ApiError`) documenté dans `docs/`
  - [ ] Diagramme de séquence UML (Angular → Controller → Service → Amadeus Adapter → Repository) dans `docs/`
  - [ ] Schéma de données V1 validé (évolution éventuelle de `search_logs`)

#### US-06 — Intégration client Amadeus (OAuth2 + vols dates fixes + Adapter)
* **Statut :** ⚪ À faire
* **Besoin :** En tant que système TripMint, je veux interroger l'API Sandbox Amadeus de façon isolée via un pattern Adapter, afin de ne jamais exposer les DTOs externes dans le domaine métier.
* **Critères d'acceptation :**
  - [ ] Identifiants externalisés (`AMADEUS_CLIENT_ID`, `AMADEUS_CLIENT_SECRET`)
  - [ ] Authentification OAuth2 *Client Credentials* avec cache du token jusqu'à expiration
  - [ ] Client `RestClient` dans `com.tripmint.client.amadeus` + Adapter vers `FlightOffer`
  - [ ] Gestion des timeouts et erreurs fournisseur (traduction en `ApiError` via `GlobalExceptionHandler`)
  - [ ] Tests unitaires / `MockRestServiceServer` (nominal, renouvellement token, erreurs HTTP)

#### US-07 — API REST de recherche à dates fixes, transactions & journalisation
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur (Parcours P1), je veux rechercher des vols sur des dates précises dans un budget optionnel, afin de comparer les meilleures offres triées par prix.
* **Critères d'acceptation :**
  - [ ] Endpoint `/api/v1/flights/search` validé via `@Valid` (codes IATA, dates cohérentes, budget > 0, passagers >= 1)
  - [ ] Filtrage `<= maxBudget` et tri croissant par prix
  - [ ] Journalisation en base dans `search_logs` **en dehors** de l'appel HTTP Amadeus (frontière `@Transactional` courte)
  - [ ] Tests unitaires (service) et d'intégration MockMvc (cas nominaux + erreurs 400/502)

#### US-08 — Moteur de recherche à dates flexibles sous contrainte de budget
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur flexible (Parcours P2), je veux indiquer une ville de départ, un budget maximum, une durée de séjour et une plage de dates, afin de trouver les créneaux les moins chers.
* **Critères d'acceptation :**
  - [ ] Génération bornée des couples `(départ, retour)` dans la fenêtre pour la durée souhaitée
  - [ ] Exécution maîtrisée respectant le rate-limit sandbox Amadeus + déduplication
  - [ ] Filtrage strict `<= maxBudget` et classement par prix croissant
  - [ ] Tests unitaires dédiés sur le calcul des créneaux et le filtrage budgétaire

#### US-09 — Interface Angular : formulaire de recherche & résultats triés par prix
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur (Parcours P1 & P2), je veux un formulaire intuitif (dates fixes / flexibles) et une liste claire des vols triés par prix, afin d'identifier rapidement la meilleure offre.
* **Critères d'acceptation :**
  - [ ] Service Angular `FlightSearchService` consommant `/api/v1/flights/search`
  - [ ] Formulaire **Reactive Forms** typé avec bascule Dates fixes / Flexibles et validations
  - [ ] État réactif piloté par **Angular Signals** (mode Zoneless)
  - [ ] Liste d'offres (compagnie, horaires, durée, escales, prix) triées par prix croissant
  - [ ] Tests unitaires Vitest sur le service et le composant

#### US-10 — Détail d'itinéraire, états UI & release V1 (`develop` → `main`)
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur, je veux consulter le détail d'une offre et comprendre clairement les états de chargement, d'absence de résultat ou d'erreur API, afin de disposer d'une V1 fiable.
* **Critères d'acceptation :**
  - [ ] Fiche détaillée d'itinéraire (segments aller/retour, escales, récapitulatif prix)
  - [ ] Gestion soignée des états UI : chargement, liste vide, affichage des messages `ApiError`
  - [ ] **Pull Request de jalon `develop` → `main`** validée par la CI et tag `v1.0.0`

---

### EPIC-03 — Historisation & Tendances Tarifaires (V2 — Parcours P3)

#### US-11 — Relevé périodique & historisation des prix
* **Statut :** ⚪ À faire
* **Besoin :** En tant que système TripMint, je veux enregistrer périodiquement les prix des trajets suivis, afin de constituer un historique fiable.
* **Critères d'acceptation :**
  - [ ] Migration Flyway (`price_snapshots`) et tâche planifiée (`@Scheduled`) avec transactions courtes

#### US-12 — Analyse de tendances & détection de baisses tarifaires
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur, je veux savoir si le prix actuel est inférieur à la moyenne historique du trajet, afin d'acheter au meilleur moment.
* **Critères d'acceptation :**
  - [ ] Requêtes analytiques PostgreSQL (*Window Functions*), tests d'intégration sur PostgreSQL réel (Testcontainers)
  - [ ] Badge de tendance / baisse affiché sur l'interface Angular

---

### EPIC-04 — Comptes, Alertes, Qualité (Playwright / SonarQube) & Prod (V2)

#### US-13 — Authentification utilisateur (JWT & Spring Security)
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur, je veux créer un compte et me connecter de façon sécurisée, afin de retrouver mes recherches et alertes.
* **Critères d'acceptation :**
  - [ ] Inscription / connexion sécurisées (hachage mot de passe + JWT), intercepteur et route guard Angular

#### US-14 — Recherches favorites & alertes automatiques de baisse de prix
* **Statut :** ⚪ À faire
* **Besoin :** En tant qu'utilisateur connecté, je veux sauvegarder des recherches cibles et être alerté dès qu'un tarif passe sous mon plafond.
* **Critères d'acceptation :**
  - [ ] CRUD des favoris/alertes (API + UI) et déclenchement d'alerte sous seuil

#### US-15 — Tests E2E Playwright sur les parcours critiques
* **Statut :** ⚪ À faire
* **Besoin :** En tant qu'équipe, je veux automatiser les parcours P1 & P2 dans un vrai navigateur avec **Playwright**, afin de prévenir toute régression front/back.
* **Critères d'acceptation :**
  - [ ] Suite Playwright configurée couvrant recherche nominale, détail itinéraire et affichage d'erreur API
  - [ ] Intégration dans la CI GitHub Actions (avec fournisseur Amadeus mocké)

#### US-16 — Analyse qualité continue SonarQube / SonarCloud
* **Statut :** ⚪ À faire
* **Besoin :** En tant qu'équipe, je veux mesurer la dette technique, les bugs potentiels et la couverture de tests via **SonarQube**, afin de maintenir un haut niveau de qualité.
* **Critères d'acceptation :**
  - [ ] Export de couverture JaCoCo (back) + Vitest (front) et Quality Gate SonarQube/SonarCloud branché dans la CI

#### US-17 — Déploiement continu sur PostgreSQL managé (`prod`) & release V2
* **Statut :** ⚪ À faire
* **Besoin :** En tant que mainteneur, je veux déployer TripMint sur un PostgreSQL managé via CD GitHub Actions, afin de rendre la V2 accessible en ligne.
* **Critères d'acceptation :**
  - [ ] Pipeline `.github/workflows/cd.yml` configuré, migrations Flyway validées sur PostgreSQL `prod`, PR `develop` → `main` et tag `v2.0.0`

---

### EPIC-05 — Extension Multi-Modale : Train & Hébergement (V3 — Parcours P4)

#### US-18 — Abstraction multi-fournisseurs (Strategy) & connecteur ferroviaire
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur, je veux comparer train et avion sur une même recherche, afin de choisir l'option la plus pertinente.
* **Critères d'acceptation :**
  - [ ] Interface `TransportProvider` (Strategy), connecteur train + affichage unifié Angular

#### US-19 — Module hébergement & budget global transport + logement
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur, je veux inclure l'hébergement dans mon budget plafond, afin d'estimer le coût réel complet du séjour.
* **Critères d'acceptation :**
  - [ ] Connecteur `StayProvider`, agrégation `transport + hébergement` et filtrage sur budget global

#### US-20 — Recommandations optimisées budget / durée & release V3
* **Statut :** ⚪ À faire
* **Besoin :** En tant que voyageur, je veux un classement combinant prix, durée et escales, afin d'identifier rapidement le meilleur compromis.
* **Critères d'acceptation :**
  - [ ] Moteur de scoring configurable testé unitairement, PR `develop` → `main` et tag `v3.0.0`

---

### EPIC-06 — Scalabilité & Ops (Phase 3–4)

#### US-21 — Parallélisation multi-fournisseurs (Virtual Threads) & benchmark
* **Statut :** ⚪ À faire
* **Besoin :** En tant que système agrégeant plusieurs fournisseurs (avion, train, hébergement), je veux paralléliser les appels I/O avec des Virtual Threads bornés, afin de réduire la latence perçue sans saturer les quotas ni le pool JDBC.
* **Critères d'acceptation :**
  - [ ] Activation mesurée des Virtual Threads avec bornage (`Semaphore`/timeout) et benchmark avant/après

#### US-22 — Conteneurisation complète & déploiement Kubernetes (+ étude NoSQL)
* **Statut :** ⚪ À faire
* **Besoin :** En tant que mainteneur, je veux packager back-end et front-end en conteneurs déployables sur **Kubernetes** (et réévaluer un cache/stockage NoSQL si le volume d'offres brutes le justifie), afin d'assurer la montée en charge en Phase 3–4.
* **Critères d'acceptation :**
  - [ ] `Dockerfile` multi-stage (back + front), manifests Kubernetes / Helm (probes `/api/v1/health`, secrets, ConfigMaps)
  - [ ] Décision documentée dans `docs/ARCHITECTURE.md` sur l'ajout éventuel d'un store NoSQL (MongoDB) ou d'un pipeline analytique selon les métriques réelles
