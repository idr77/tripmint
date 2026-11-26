# AGENTS.md — Règles & Conventions du projet TripMint

Ce fichier définit les principes d'ingénierie, les conventions techniques, la stratégie de tests et les règles de collaboration avec l'IA sur le monorepo **TripMint**.

---

## 1. Principes d'ingénierie & Qualité du code (Priorité absolue)

### Principes fondamentaux
* **KISS (*Keep It Simple, Stupid*) & YAGNI :** privilégier la solution la plus simple et lisible répondant au besoin fonctionnel actuel. Ne jamais introduire d'abstraction spéculative ou de brique anticipée (pas de Spring Modulith, pas de microservices, pas de Virtual Threads, pas d'authentification, pas de NoSQL/Kubernetes en V1).
* **DRY (*Don't Repeat Yourself*) :** factoriser la logique métier réellement partagée sans coupler deux concepts métier distincts.
* **SOLID :**
  * **S (Single Responsibility) :** une classe / méthode = une responsabilité unique (`controller` = HTTP, `exception` = erreurs transverses, `service` = métier, `client` = API externe, `repository` = BDD).
  * **O (Open/Closed) :** exposer des interfaces sur les vrais points d'extension (fournisseurs externes).
  * **L (Liskov Substitution) :** toute implémentation respecte strictement le contrat de son interface.
  * **I (Interface Segregation) :** préférer des interfaces ciblées et courtes.
  * **D (Dependency Inversion) :** dépendre d'abstractions pour les dépendances externes ou variables (`FlightProvider`, `Clock`) et **toujours injecter par constructeur**.

### Design Patterns (uniquement quand le problème existe)
* **Adapter :** isoler tout fournisseur externe (ex. `com.tripmint.client.<fournisseur>`) afin qu'aucun DTO brut externe ne fuite dans `service/` ou dans l'API REST TripMint.
* **Strategy :** à introduire dès que plusieurs algorithmes ou fournisseurs interchangeables coexistent (V3 : avion / train / hébergement).
* **DTO / Record :** découpler systématiquement les contrats JSON exposés (Java `record`) des entités JPA (`@Entity`).

### Lisibilité, Erreurs & Transactions
* **Nommage métier explicite :** noms orientés domaine (ex. `findCheapestFlights` plutôt que `processData`).
* **Méthodes courtes & garde-fous :** retours anticipés (*early returns*), faible imbrication, zéro code mort ou commenté.
* **Immutabilité par défaut :** Java `record` et champs `final` côté back-end ; `readonly`, `signal()` et `computed()` côté Angular.
* **Gestion d'erreurs centralisée :** ne jamais avaler d'exception silencieusement ; remonter des exceptions explicites traduites en `ApiError` par `com.tripmint.exception.GlobalExceptionHandler` (`@RestControllerAdvice`), sans jamais exposer de stacktrace au client.
* **Transactions (`@Transactional`) :**
  * Déclarer les frontières transactionnelles dans `service/` (jamais dans `controller/`).
  * Utiliser `@Transactional(readOnly = true)` par défaut sur les lectures.
  * **Règle d'or :** ne **jamais** ouvrir une transaction `@Transactional` autour d'un appel HTTP externe (fournisseur de vols), afin de ne pas bloquer une connexion du pool JDBC (`HikariCP`) pendant la latence réseau.
  * Maintenir `spring.jpa.open-in-view=false` et éviter le problème N+1 (`JOIN FETCH`, `@EntityGraph`, projections DTO).
* **Sécurité & Secrets :** aucune clé API ou mot de passe en dur dans le code ou Git ; toute variable d'environnement documentée dans `.env.example`.

---

## 2. Architecture Cible & Conventions Techniques

Sources de vérité détaillées : [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) (ADRs & compromis) et [docs/ArchitectureRoadMap.md](docs/ArchitectureRoadMap.md) (structure du dépôt & trajectoire).

### Règles Back-end (`backend/`)
* **Stack V1 :** Java 25 (LTS), Spring Boot 4.x (Spring MVC classique), Flyway, H2 (`MODE=PostgreSQL` en local/tests), PostgreSQL 18 (`backend/docker-compose.yml` & profil `prod`).
* **Packages (`com.tripmint`) :**
  * En V1 : `config`, `controller`, `exception`, `service`, `repository`, `model`, et `client.<fournisseur>` dès l'intégration du premier connecteur externe.
  * Ne **pas** créer de dossiers ou fichiers placeholders vides (`package-info.java` sans annotation réelle).
  * Ne **pas** dupliquer de sous-packages miroir sous chaque couche (`controller.xxx`, `service.xxx`, `model.xxx`).
  * Pivoter vers *Package by Feature* (`common/`, `search/`, `pricing/`, `alert/`, `user/`) en V2 (`EPIC-T5`).
* **Base de données & Flyway :**
  * Ne jamais modifier une migration Flyway déjà mergée : toujours ajouter `V<n+1>__<description>.sql`.
  * Écrire du **SQL standard compatible H2 (`MODE=PostgreSQL`) et PostgreSQL 18**, validé par `spring.jpa.hibernate.ddl-auto=validate`.
* **Infrastructure Docker :** garder les `docker-compose.yml` séparés par périmètre (`backend/docker-compose.yml` pour PostgreSQL 18).

### Règles Front-end (`frontend/`)
* **Angular 22+ Standalone** uniquement (aucun `NgModule`), mode **Zoneless** (sans `zone.js`), **SCSS**, TypeScript strict (`strict: true`, `strictTemplates: true`).
* **Signals & RxJS :** `signal()`, `computed()`, `effect()` pour l'état UI ; réserver **RxJS** aux flux HTTP / asynchrones complexes (`switchMap`, `debounceTime`, annulation).
* **Reactive Forms** typés pour tous les formulaires ; appels API relatifs `/api/v1/...` (via `proxy.conf.json`).
* Composants fins : déléguer les appels HTTP et transformations aux services (`providedIn: 'root'`).

---

## 3. Stratégie de Tests (Exigences par Couche)

Tout nouveau code métier ou technique doit être accompagné des tests adaptés à son niveau de risque :

### 1. Tests Unitaires (Rapides, isolés, sans contexte Spring)
* **Logique métier complexe (prioritaire) :** moteur de dates flexibles (`EPIC-T3`), filtrage budgétaire, tri multi-critères, scoring, calculs de moyennes/tendances, génération de fenêtres de dates et cas limites (bornes de dates, budget nul/égal, listes vides, devises).
* **Adapters & mapping :** traduction des réponses brutes fournisseur → modèles métier TripMint (y compris champs manquants, escales multiples, réponses partielles).
* **Front-end (Vitest) :** logique des services, validateurs personnalisés de Reactive Forms et états dérivés (`computed` Signals).

### 2. Tests d'Intégration Techniques (Back-end)
* **Couche HTTP & Erreurs (`@WebMvcTest` / MockMvc) :** vérifier les contrats JSON, les règles `@Valid` (erreurs `400` champ par champ dans `ApiError.fieldErrors`), les `404`, et la traduction des timeouts/erreurs fournisseur (`502`/`503`/`504`).
* **Clients HTTP externes (`MockRestServiceServer`) :** tester succès, erreurs HTTP `4xx`/`5xx`, timeouts et renouvellement de token si applicable, sans appel réseau réel.
* **Persistence, Flyway & Transactions (`@SpringBootTest` / `@DataJpaTest`) :**
  * Vérifier l'exécution des migrations Flyway et la cohérence `ddl-auto=validate`.
  * Tester les comportements transactionnels critiques (rollback sur exception, persistance `search_logs` indépendante d'un échec annexe si attendu).
  * À partir de `EPIC-T4` (`US-T13`) : exécuter les tests SQL avancés (Window Functions / requêtes PostgreSQL) sur **Testcontainers (PostgreSQL 18)**.

### 3. Tests Fonctionnels & End-to-End (E2E)
* **Back-end :** tests de bout en bout d'un scénario API complet (requête HTTP → service → provider mocké → persistance BDD → réponse JSON).
* **Full Stack (Playwright — `EPIC-T4` / `US-T11`) :** automatiser les parcours utilisateurs critiques (**P1** recherche dates fixes + détail, **P2** dates flexibles, + au moins 1 scénario d'erreur API affichée à l'écran) avec fournisseur externe mocké en CI.

---

## 4. Règles de Collaboration avec l'IA & Workflow

### 1. Flux Produit → Technique
Respecter systématiquement l'ordre défini dans [docs/USER_STORIES.md](docs/USER_STORIES.md) :
**`EPIC Fonctionnelle` → `User Stories Fonctionnelles` → `PRD (docs/prd/)` → `EPIC Technique` → `User Stories Techniques` → `Code`**.
Toute décision d'architecture introduite doit être reliée au besoin fonctionnel dans [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

### 2. Transparence pédagogique (Annotations & Mécanismes « niche »)
* Chaque fois que l'IA introduit une annotation, option ou mécanique non triviale (ex. `@PrePersist`, `@RestControllerAdvice`, `@Transactional(readOnly = true)`, `open-in-view: false`, `toSignal`, `MockRestServiceServer`…), elle **doit expliquer brièvement son rôle, pourquoi elle est choisie et quelles alternatives existent**, afin que le développeur maîtrise 100 % du code livré.

### 3. Suivi JIRA & Journal des Commits obligatoires
* **À chaque commit :**
  1. Mettre à jour **[docs/COMMIT_LOG.md](docs/COMMIT_LOG.md)** (date, hash/référence, branche, EPIC/US, résumé).
  2. Mettre à jour le statut / cases d'acceptation dans **[docs/USER_STORIES.md](docs/USER_STORIES.md)** si une US avance ou se termine.

### 4. Challenge critique avant chaque Pull Request
* Avant l'ouverture ou la finalisation d'une PR, l'IA présente un court bilan :
  1. **Décisions prises** et le besoin fonctionnel dont elles découlent.
  2. **Faiblesses & compromis** selon le contexte actuel.
  3. **Tests ajoutés** (unitaires / techniques / fonctionnels) et limites de couverture restantes.

### 5. Branches, PR & Definition of Done
* **Branches de travail** depuis `develop` : `feature/<id>-nom`, `fix/<id>-nom`, `chore/<id>-nom`.
* **PR courantes → `develop`** ; **PR de jalons (`V1`, `V2`, `V3`…) → `main`**.
* **Validation avant commit / PR :**
  1. `backend/` : `./mvnw test` au vert (**Java 25**).
  2. `frontend/` : `npm test -- --watch=false` et `npm run build` au vert (**Node 24 LTS** ou `>= 22.22.3`).
  3. Infra : `docker compose -f backend/docker-compose.yml --env-file .env.example config --quiet` valide.
  4. CI GitHub Actions (`.github/workflows/ci.yml`) au vert sur tous les jobs (`backend`, `frontend`, `docker`).
  5. Aucun fichier généré (`target/`, `dist/`, `node_modules/`), `.env` ni note personnelle (`Notes.md`) commité.
