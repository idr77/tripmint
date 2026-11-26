# Architecture, Décisions & Compromis (ADR) — TripMint

Ce document rassemble les choix structurants de **TripMint**, leur **justification par rapport au besoin métier**, ainsi que leurs **faiblesses/limites selon le contexte** et les critères qui déclencheront une évolution.

---

## 1. Décisions d'Architecture (ADR)

### ADR-01 — Monolithe Spring MVC classique en V1 (sans Spring Modulith ni Microservices)
* **Décision :** Architecture en couches classique (`config`, `controller`, `service`, `client`, `repository`, `model`) sous `com.tripmint`, avec sous-package dédié par fournisseur externe (`client.amadeus`).
* **Pourquoi :** En V1, un seul domaine métier principal existe (recherche de vols). Un monolithe simple maximise la vitesse d'exécution, la lisibilité et le débogage.
* **Faiblesses & Compromis :**
  * Pas de frontières de modules vérifiées à la compilation : un développeur peut coupler deux services par inattention si les revues de PR sont laxistes.
  * Le déploiement est tout-ou-rien (impossible de scaler uniquement la recherche indépendamment des alertes).
* **Quand reconsidérer :** À l'apparition d'un 2e vrai domaine métier (V2 : alertes, authentification) → pivoter vers du **Package by Feature** (`common/`, `search/`, `pricing/`, `alert/`, `user/`). Envisager l'extraction d'un service dédié uniquement si la charge ou les équipes le justifient (Phase 3–4).

---

### ADR-02 — Flyway dès la V1 + H2 (`MODE=PostgreSQL`) en local/tests + PostgreSQL 18 (Docker & Prod)
* **Décision :** Toutes les évolutions de schéma passent par des scripts Flyway (`V<n>__*.sql`) en SQL standard, validés au démarrage par Hibernate (`ddl-auto: validate`). Le profil `local` utilise H2 en mémoire (`MODE=PostgreSQL`), tandis que `docker-compose.yml` fournit un PostgreSQL 18 isolé pour le pré-prod local.
* **Pourquoi :** Zéro friction pour lancer les tests (`./mvnw test`) et la CI en quelques secondes, tout en gardant un vrai PostgreSQL 18 à portée de main.
* **Faiblesses & Compromis :**
  * **H2 en mode PostgreSQL n'est pas 100 % identique à PostgreSQL :** certaines fonctionnalités avancées (`JSONB`, index `GIN`, certaines *Window Functions* ou syntaxes `UPSERT`) divergent entre H2 et PostgreSQL.
  * Risque de « faux vert » en CI si une requête SQL passe sur H2 mais échoue sur PostgreSQL 18.
* **Mitigation & Quand reconsidérer :**
  * Rester strictement sur du SQL standard en V1.
  * Dès la V2 (requêtes analytiques / *Window Functions* PostgreSQL), ajouter **Testcontainers (PostgreSQL 18)** ou un service `postgres:18-alpine` dans GitHub Actions pour exécuter les tests d'intégration sur le vrai moteur PostgreSQL.

---

### ADR-03 — Pas de Virtual Threads ni d'Authentification en V1
* **Décision :**
  * **Virtual Threads** (`spring.threads.virtual.enabled=true`) volontairement non activés en Phase 1.
  * **Authentification / Spring Security / JWT** volontairement reportés en V2.
* **Pourquoi :**
  * En V1, la recherche de vols s'appuie sur un fournisseur unique (Amadeus sandbox, fortement limité en *rate-limit*) : les Virtual Threads n'apportent aucun gain mesurable et risquent au contraire de saturer le quota API ou le pool de connexions JDBC (`HikariCP`).
  * L'authentification n'apporte aucune valeur métier au parcours principal V1 (rechercher un vol pas cher) et ralentit le MVP.
* **Faiblesses & Compromis :**
  * Sans authentification, la V1 ne permet ni favoris persistants ni alertes personnalisées ; l'API publique devra être protégée par du *rate-limiting* en exposition publique.
* **Quand reconsidérer :**
  * **Authentification :** dès **EPIC-04 (V2)** pour les recherches favorites et alertes.
  * **Virtual Threads :** dès que plusieurs fournisseurs (avion + train + hébergement) ou de nombreux appels I/O indépendants doivent être parallélisés (V3 / Phase 3–4), avec mesure de charge et bornage par `Semaphore` / `Bulkhead`.

---

### ADR-04 — Retours API, Gestion d'Erreurs & Transactions (`@Transactional`)
* **Décision :**
  * Toutes les erreurs REST passent par `GlobalExceptionHandler` (`@RestControllerAdvice`) et renvoient un contrat unique `ApiError` (`timestamp`, `status`, `error`, `message`, `path`, `fieldErrors`).
  * `spring.jpa.open-in-view: false` pour interdire les requêtes SQL cachées pendant la sérialisation JSON.
  * Règles strictes de démarcation transactionnelle :
    * `@Transactional(readOnly = true)` par défaut sur les méthodes de lecture en service.
    * `@Transactional` uniquement sur les opérations d'écriture courtes en base de données.
    * **Règle d'or :** ne **jamais** englober un appel HTTP externe (ex. Amadeus) dans une transaction `@Transactional` — cela bloque une connexion JDBC du pool `HikariCP` pendant toute la latence réseau (et provoque un épuisement du pool sous charge).
* **Faiblesses & Compromis :**
  * Séparer appel externe et persistance implique qu'un échec d'écriture après un appel Amadeus ne « rollback » pas l'appel externe (acceptable ici car la recherche est en lecture côté fournisseur ; critique si un jour on ajoute la réservation).
  * Avec `open-in-view: false`, charger une relation `LAZY` hors transaction lève une `LazyInitializationException` — cela force à écrire explicitement des `JOIN FETCH` ou des projections DTO (ce qui est volontaire pour éviter le problème **N+1**).

---

## 2. Front-end Angular : Signals & Zoneless vs ce qui existait avant

TripMint démarre directement en **Angular 22+ Standalone + Signals + Zoneless**. Voici le comparatif pour bien situer ce choix face aux générations précédentes d'Angular :

| Axe | Avant (Angular 2 → 16) | Maintenant (Angular 17 → 22+, choix TripMint) | Ce qu'il faut retenir / Faiblesses |
| :--- | :--- | :--- | :--- |
| **Structure** | `NgModule` obligatoires (boilerplate, imports indirects) | **Standalone Components** (imports explicites par composant) | Plus simple et meilleur *tree-shaking*. Attention à ne pas importer trop de dépendances dans un seul gros composant. |
| **Détection de changement** | **`Zone.js`** interceptait tous les événements JS (`setTimeout`, `click`, `Promise`) et vérifiait toute l'arborescence de composants (d'où le besoin de `ChangeDetectionStrategy.OnPush` pour limiter les dégâts) | **Zoneless** : Angular ne met à jour le DOM que lorsque un **Signal** change (ou `async` pipe / événement template / `markForCheck`) | Bundle plus léger, performances prévisibles, stacktraces propres. **Faiblesse :** une variable JS mutée « à la main » hors Signal ne rafraîchit plus l'écran (piège classique quand on vient d'Angular historique ou qu'on intègre une lib tierce non réactive). |
| **Gestion d'état** | Propriétés mutables + **`RxJS` (`BehaviorSubject`, `combineLatest`, `async` pipe, gestion manuelle des `unsubscribe`)** | **Angular Signals** (`signal`, `computed`, `effect`) pour l'état synchrone/UI ; **RxJS** réservé aux flux asynchrones complexes (`HttpClient`, `debounceTime`, `switchMap`, annulation de requêtes) | Signals = zéro fuite mémoire, lecture synchrone simple, dépendances automatiques. **Faiblesse :** Signals ne remplacent pas RxJS pour les flux événementiels complexes (annulation HTTP, retry, websockets) → les deux sont complémentaires (`toSignal` / `rxResource`). |

---

## 3. Challenge NoSQL : MongoDB & Apache Spark — Utile ou Sur-ingénierie ?

Question posée en réunion : *« Comment ajouter MongoDB / Spark (NoSQL) ? »*

### Analyse honnête par phase

| Technologie | Ce qu'elle résout vraiment | Pertinence en **V1** | Pertinence potentielle en **V2 / V3 / Phase 4** |
| :--- | :--- | :--- | :--- |
| **MongoDB (Document NoSQL)** | Stockage de documents JSON très hétérogènes à schéma variable, fort volume d'écriture non relationnel | ❌ **Non pertinent en V1** : on a un schéma simple et structuré (`search_logs`), et **PostgreSQL 18 gère nativement `JSONB`** (indexable avec `GIN`) si on veut archiver des payloads bruts Amadeus. Ajouter MongoDB en V1 doublerait la complexité ops (2 BDD, 2 modèles de sauvegarde, pas de transactions ACID cross-DB) sans gain. | 🟡 **Défendable en V3 uniquement** si on agrège de nombreux fournisseurs aux schémas très hétérogènes (avion + train + hôtels + métadonnées brutes) en tant que **cache/archive documentaire** (modèle *Polyglot Persistence* : PostgreSQL pour les comptes/alertes/transactions, MongoDB pour les catalogues d'offres brutes à TTL court). |
| **Apache Spark (Traitement distribué Big Data)** | Calcul distribué sur des téraoctets de données / millions d'événements par heure | ❌ **Sur-ingénierie totale en V1/V2** : sur quelques milliers ou millions de relevés tarifaires, des requêtes SQL analytiques PostgreSQL (**Window Functions**, vues matérialisées) répondent en quelques millisecondes. | 🟡 **Pertinent uniquement en Phase 4 (très grande échelle)** si TripMint ingère des millions de relevés de prix/jour pour entraîner des modèles de prédiction tarifaire batch (ML / Spark SQL). |

**Conclusion pragmatique :**
* **V1–V2 :** 100 % **PostgreSQL 18** (en exploitant `JSONB` si besoin d'archiver des réponses brutes fournisseurs, et les *Window Functions* SQL pour l'analyse de tendances).
* **V3–V4 :** réévaluer MongoDB (stockage d'offres multi-fournisseurs avec TTL) uniquement sur constat de douleur réelle, jamais par anticipation.

---

## 4. Qualité, Tests E2E & Infrastructure Cible (Playwright, SonarQube, Kubernetes)

* **Tests E2E Playwright (fin V1 / V2) :**
  * Complète JUnit (back) et Vitest (front) en validant les **parcours utilisateurs complets** (P1 et P2) dans un vrai navigateur contre le front Angular + back Spring Boot.
  * **Faiblesse :** plus lents et plus fragiles que les tests unitaires → limiter les E2E aux parcours critiques (« happy paths » + 1 cas d'erreur API) et mocker Amadeus en CI pour éviter la dépendance réseau.
* **SonarQube / SonarCloud (V2, ou dès que le code métier arrive en V1) :**
  * Analyse statique continue dans GitHub Actions (code smells, dette technique, bugs potentiels, couverture JaCoCo/Vitest, règles de sécurité).
  * **Faiblesse :** ne remplace pas la revue de conception ni la qualité des assertions de test (une couverture à 80 % sans bonnes assertions reste trompeuse).
* **Kubernetes (Phase 3–4) :**
  * Volontairement **hors V1 et V2** (un PaaS managé ou Docker Compose suffit largement).
  * Pertinent en Phase 3–4 si TripMint passe en déploiement multi-instances (autoscaling horizontal, *rolling updates* sans coupure, gestion déclarative des secrets/CronJobs de relevé de prix).
  * **Faiblesse :** fort coût opérationnel et cognitif (manifests/Helm, ingress, observabilité, gestion des migrations Flyway en multi-réplicas).
