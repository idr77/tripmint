# AGENTS.md — Règles & Conventions du projet TripMint

Ce fichier définit les principes d'ingénierie, les choix d'architecture, les règles de collaboration avec l'IA et le workflow à respecter pour toute contribution sur le monorepo **TripMint**.

---

## 1. Principes d'ingénierie & Qualité du code (Priorité absolue)

### Principes fondamentaux
* **KISS (*Keep It Simple, Stupid*) & YAGNI :** privilégier la solution la plus simple, directe et lisible qui répond au besoin métier actuel. Ne jamais introduire d'abstraction spéculative ou d'infrastructure anticipée (pas de Spring Modulith, pas de microservices, pas de Virtual Threads, pas d'authentification, pas de NoSQL/Kubernetes en V1).
* **DRY (*Don't Repeat Yourself*) :** factoriser la logique métier dupliquée et les règles partagées, sans coupler artificiellement deux concepts métier distincts qui se ressemblent par hasard.
* **SOLID :**
  * **S (Single Responsibility) :** une classe / méthode = une responsabilité claire (contrôleur = HTTP, service = métier, client = API externe, repository = BDD).
  * **O (Open/Closed) :** concevoir les points d'extension réels (ex. futurs fournisseurs de transport) par interface plutôt que par chaînes de `if/else`.
  * **L (Liskov Substitution) :** toute implémentation d'une interface doit respecter strictement son contrat.
  * **I (Interface Segregation) :** préférer des interfaces ciblées plutôt que des contrats fourre-tout.
  * **D (Dependency Inversion) :** dépendre d'abstractions pour les briques externes ou variables (fournisseurs, `Clock`), et **toujours injecter les dépendances par constructeur**.

### Design Patterns (uniquement quand le besoin existe)
* **Adapter :** isoler les APIs externes (ex. Amadeus dans `com.tripmint.client.amadeus`) afin qu'aucun DTO brut externe ne fuite dans `service/` ou dans les réponses REST.
* **Strategy :** introduire dès que plusieurs algorithmes ou fournisseurs interchangeables coexistent (ex. modes de recherche ou avion/train en V3).
* **DTO / Record :** découpler systématiquement les contrats JSON exposés (Java `record`) des entités JPA (`@Entity`).

### Lisibilité, Erreurs & Transactions
* **Nommage métier explicite :** noms de classes, méthodes et variables orientés domaine (ex. `findCheapestFlights` plutôt que `processData`).
* **Méthodes courtes & garde-fous :** retours anticipés (*early returns*), éviter les imbrications profondes.
* **Immutabilité par défaut :** Java `record` et champs `final` côté back-end ; `readonly`, `signal()` et `computed()` côté Angular.
* **Gestion d'erreurs centralisée :** ne jamais avaler d'exception silencieusement ; remonter des exceptions métier explicites traduites de manière uniforme en `ApiError` par `GlobalExceptionHandler` (`@RestControllerAdvice`), sans jamais exposer de stacktrace au client.
* **Transactions (`@Transactional`) :**
  * Placer les frontières transactionnelles dans la couche `service` (jamais dans `controller`).
  * Utiliser `@Transactional(readOnly = true)` par défaut pour les lectures.
  * **Règle d'or :** ne **jamais** ouvrir de transaction BDD (`@Transactional`) autour d'un appel HTTP externe (ex. Amadeus), afin de ne pas monopoliser une connexion du pool JDBC pendant la latence réseau.
  * Conserver `spring.jpa.open-in-view=false` et éviter le problème N+1 (`JOIN FETCH`, `@EntityGraph`, projections DTO).
* **Zéro bruit & Sécurité :** pas de code mort ou commenté ; aucune clé API ou mot de passe en dur dans le code ou Git ; toute variable d'environnement documentée dans `.env.example`.

---

## 2. Architecture Cible & Règles Techniques

Référence détaillée des choix et compromis : **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)**.

### Vue Monorepo
* `backend/` : API REST **Java 25 (LTS)** + **Spring Boot 4.x** en **Spring MVC classique** (sans Spring Modulith, sans Virtual Threads ni Auth en V1).
* `frontend/` : SPA **Angular 22+** en **Standalone Components**, détection **Zoneless** (sans `zone.js`), **Angular Signals**, **SCSS** et **TypeScript strict** (`strict: true`, `strictTemplates: true`).
* `docker-compose.yml` : instance **PostgreSQL 18** isolée (`postgres:18-alpine`, volume `/var/lib/postgresql`, healthcheck `pg_isready`).
* `docs/` : [ARCHITECTURE.md](docs/ARCHITECTURE.md), [USER_STORIES.md](docs/USER_STORIES.md) (suivi JIRA), diagrammes UML, schémas MCD et contrats JSON (`/api/v1/*`).

### Règles Back-end (`backend/`)
* **Packages (`com.tripmint`) :**
  * En V1, conserver l'arborescence à plat par couche : `config`, `controller`, `service`, `client`, `repository`, `model`.
  * **Ne pas** créer de sous-packages miroir sous chaque couche (`controller.xxx`, `service.xxx`, `model.xxx`).
  * Sous-package autorisé pour isoler un fournisseur externe : `com.tripmint.client.<fournisseur>` (ex. `com.tripmint.client.amadeus`).
  * Pivoter vers *Package by Feature* (`search/`, `alert/`, `user/`, `common/`) uniquement en V2/V3 lorsqu'un second domaine métier apparaît.
* **Base de données & Flyway :**
  * **Flyway dès la V1** (`backend/src/main/resources/db/migration/V<n>__<description>.sql`). Ne jamais modifier une migration déjà mergée : ajouter une nouvelle version `V<n+1>__...sql`.
  * Écrire du **SQL standard compatible H2 (`MODE=PostgreSQL`) et PostgreSQL 18**.
  * Conserver `spring.jpa.hibernate.ddl-auto=validate` et `spring.jpa.open-in-view=false`.
  * Profil `local` actif par défaut (H2 en mémoire + `/h2-console`) ; profil `prod` pour PostgreSQL (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`), testable en local avec `docker-compose.yml`.
* **Conventions REST & Spring Boot 4.x :**
  * Endpoints métier préfixés par `/api/v1/...`, validation stricte par `@Valid`.
  * Respecter les starters modulaires Spring Boot 4 (`spring-boot-starter-webmvc`, `spring-boot-starter-flyway`, `spring-boot-h2console`, `spring-boot-starter-*-test`).

### Règles Front-end (`frontend/`)
* **Standalone Components** uniquement (aucun `NgModule`).
* **Zoneless + Signals :** utiliser `signal()`, `computed()`, `effect()` pour l'état UI ; réserver **RxJS** aux flux HTTP / asynchrones complexes (`switchMap`, `debounceTime`, annulation).
* Utiliser **Reactive Forms** typés pour les formulaires.
* Appeler l'API via des chemins relatifs `/api/v1/...` (proxy local `proxy.conf.json` → `http://localhost:8080`).
* Composants fins : déléguer les appels HTTP et transformations aux services (`providedIn: 'root'`).

---

## 3. Règles de Collaboration avec l'IA & Workflow

### 1. Suivi JIRA obligatoire (`docs/USER_STORIES.md`)
* Toute tâche correspond à un **EPIC** et une **User Story** dans [docs/USER_STORIES.md](docs/USER_STORIES.md) (modèle d'issue : `.github/ISSUE_TEMPLATE/user_story.md`).
* **À chaque commit :** l'IA **doit mettre à jour [docs/USER_STORIES.md](docs/USER_STORIES.md)** (cases d'acceptation cochées, statut de la US, et entrée dans le journal des commits / colonne `Commits`).

### 2. Challenge systématique avant chaque Pull Request
* Avant d'ouvrir ou de finaliser une Pull Request, l'IA **doit présenter un court bilan critique** au développeur :
  1. **Décisions prises** (architecture, patterns, schéma SQL, contrat API) et *pourquoi*.
  2. **Faiblesses & compromis** de ces choix selon le contexte actuel.
  3. **Points à surveiller / revisiter** dans les jalons suivants.
* L'objectif est que chaque choix soit compris, assumé et défendable (revue de code / mentorat).

### 3. Branches & Pull Requests
* **Branches de travail :** partir de `develop` et nommer `feature/<id>-nom`, `fix/<id>-nom`, ou `chore/<id>-nom`.
* **PR courantes → `develop` :** toute PR de feature, fix ou chore cible **`develop`** (1 PR = 1 User Story ou lot cohérent).
* **PR de jalon → `main` :** les PR vers **`main`** sont réservées aux livraisons de jalons (**V1**, **V2**, **V3**…) depuis `develop`.
* Ne jamais committer de fichiers générés (`target/`, `dist/`, `node_modules/`, `.angular/`) ni de fichier `.env`.

### 4. Definition of Done & Validation
Avant tout commit final ou PR, vérifier systématiquement :
1. **Back-end :** `./mvnw test` (`.\mvnw.cmd test` sous Windows) au vert avec **Java 25**.
2. **Front-end :** `npm test -- --watch=false` et `npm run build` au vert avec **Node 24 LTS** (ou `>= 22.22.3`).
3. **Infrastructure :** `docker compose --env-file .env.example config --quiet` valide si `docker-compose.yml` ou `.env.example` a changé.
4. **Tests :** chaque règle métier, endpoint ou composant significatif est testé (cas nominal + erreur/validation).
5. **CI GitHub Actions (`.github/workflows/ci.yml`) :** tous les jobs (`backend`, `frontend`, `docker`) au vert sur la PR.
6. **Doc & JIRA :** `docs/USER_STORIES.md` (et `docs/ARCHITECTURE.md` / `README.md` si décision structurante) mis à jour.

---

## 4. Périmètre & Roadmap (Éviter le scope creep)

* **V1 (MVP) :** socle Spring MVC + Flyway/H2/PostgreSQL 18 (`docker-compose.yml`) + Angular 22+ Zoneless/Signals + CI, journalisation (`search_logs`), recherche de vols Amadeus (dates fixes puis flexibles sous budget), résultats triés par prix & détail itinéraire.
* **V2 :** historisation et tendances tarifaires, authentification JWT, recherches favorites & alertes, tests E2E **Playwright**, analyse qualité **SonarQube**, déploiement PostgreSQL managé.
* **V3 / Phase 3–4 :** extension multi-modale (train + hébergement), optimisation budget/durée, passage à l'échelle (**Virtual Threads**, **Kubernetes**, étude NoSQL si justifié par le volume).
