# Décisions d'Architecture (ADR) & Compromis — TripMint

Chaque décision d'architecture ci-dessous **découle explicitement d'un besoin fonctionnel** ([USER_STORIES.md](USER_STORIES.md)) et documente ses **faiblesses selon le contexte** ainsi que l'**EPIC technique** chargée de la faire évoluer lorsque le contexte changera.

Pour l'arborescence du dépôt et la trajectoire d'évolution par phase, voir [ArchitectureRoadMap.md](ArchitectureRoadMap.md).

---

## ADR-01 — Monolithe Spring MVC classique en V1 (sans Spring Modulith ni Microservices)
* **Besoin fonctionnel / EPIC lié :** `EPIC-F1` & `EPIC-F2` (livrer vite un MVP de recherche de vols) → `EPIC-T1` / `EPIC-T2`.
* **Décision :** Architecture en couches simple sous `com.tripmint` (`config`, `controller`, `exception`, `service`, `repository`, `model`, et `client.<fournisseur>` dès `EPIC-T2`).
* **Pourquoi :** Un seul domaine métier actif en V1 (la recherche de vols). Maximise la lisibilité, le débogage et la vélocité.
* **Faiblesses & Compromis :**
  * Aucun contrôle de modularité à la compilation : un couplage involontaire entre domaines reste possible si la revue de PR est légère.
  * Scalabilité globale (tout le back-end scale ensemble).
* **Condition de révision / EPIC technique :** À l'introduction des comptes et alertes (**`EPIC-F3` / `EPIC-T5`**), basculer vers du **Package by Feature** (`common/`, `search/`, `pricing/`, `alert/`, `user/`).

---

## ADR-02 — Flyway dès la V1 + H2 (`MODE=PostgreSQL`) en local/tests + `backend/docker-compose.yml` (PostgreSQL 18)
* **Besoin fonctionnel / EPIC lié :** Fiabilité du schéma dès `EPIC-F1` (`search_logs`), puis requêtes analytiques en `EPIC-F3` → `EPIC-T1`, `EPIC-T4`.
* **Décision :**
  * Migrations SQL versionnées Flyway dès la V1 (`ddl-auto: validate`).
  * Profil `local` par défaut sur H2 en mémoire (`MODE=PostgreSQL`) pour un démarrage et une CI ultra-rapides.
  * Instance PostgreSQL 18 isolée dans **`backend/docker-compose.yml`** (séparée du front-end pour éviter un compose monolithique complexe).
* **Faiblesses & Compromis :**
  * **H2 ≠ PostgreSQL à 100 % :** les fonctionnalités spécifiques PostgreSQL (`JSONB`, index `GIN`, certaines *Window Functions* ou `UPSERT`) peuvent passer sur H2 et échouer en prod — ou inversement.
  * Un compose séparé dans `backend/` demande une commande dédiée pour lancer la BDD, puis un orchestrateur séparé si le front est conteneurisé plus tard.
* **Condition de révision / EPIC technique :** **`EPIC-T4` (`US-T13`)** introduira **Testcontainers (PostgreSQL 18)** dès les premières requêtes SQL avancées de `EPIC-F3`.

---

## ADR-03 — Isolation du fournisseur de vols par Adapter (pivot post-fermeture Amadeus Self-Service)
* **Besoin fonctionnel / EPIC lié :** `EPIC-F1` & `EPIC-F2` (données de vols réelles ou sandbox fiables) → `EPIC-T2`.
* **Contexte clé :** Le portail **Amadeus for Developers Self-Service** (tier gratuit) a été **définitivement fermé le 17 juillet 2026** (accès désormais uniquement Enterprise sur contrat + accréditation IATA).
* **Décision :** Définir une interface métier `FlightProvider` dans le back-end et isoler tout fournisseur externe derrière un **Adapter** (`com.tripmint.client.<fournisseur>`) + un stub/mock pour les tests/CI. En V1, le choix se porte sur **Duffel API (Test Mode 100 % gratuit, illimité)** ou **SerpApi (Google Flights, 250 recherches/mois réelles)** — décision actée dans `PRD-F1.md` (`US-T05`).
* **Faiblesses & Compromis :**
  * Duffel Test Mode fournit des tarifs simulés (non réels) ; SerpApi fournit de vrais prix mais avec un quota mensuel très bas (250 appels/mois, vite consommé par les dates flexibles de `EPIC-F2`).
  * Ajouter une couche Adapter ajoute un mapping supplémentaire (léger, mais justifié ici par un fait concret : un fournisseur externe peut disparaître !).
* **Condition de révision / EPIC technique :** **`EPIC-T6` (`US-T18`)** étendra ce contrat en pattern **Strategy** (`TransportProvider` / `StayProvider`) pour supporter train et hébergement (`EPIC-F4`).

---

## ADR-04 — Absence volontaire d'Authentification et de Virtual Threads en V1
* **Besoin fonctionnel / EPIC lié :** `EPIC-F1` & `EPIC-F2` ne nécessitent pas de compte utilisateur et n'interrogent qu'un seul fournisseur.
* **Décision :** Pas de Spring Security / JWT ni de `spring.threads.virtual.enabled=true` en V1.
* **Faiblesses & Compromis :**
  * Sans auth, impossible de persister des favoris ou alertes par utilisateur en V1, et l'API devra être protégée par du rate-limiting si exposée publiquement.
  * Sans Virtual Threads, les appels synchrones multiples (`EPIC-F2`, dates flexibles) restent séquentiels ou sur pool borné classique — volontaire en V1 pour ne pas exploser le rate-limit fournisseur ni le pool JDBC.
* **Condition de révision / EPIC technique :**
  * Auth → **`EPIC-T5` (`US-T15`)** pour servir `EPIC-F3`.
  * Virtual Threads bornés → **`EPIC-T6` (`US-T19`)** pour paralléliser les appels multi-fournisseurs de `EPIC-F4`.

---

## ADR-05 — Stratégie Erreurs API (`exception/`) & Frontières Transactionnelles (`@Transactional`)
* **Besoin fonctionnel / EPIC lié :** `US-F04` (retours clairs en cas d'erreur de saisie ou de panne fournisseur) + intégrité BDD → `EPIC-T1`, `EPIC-T2`.
* **Décision :**
  * `GlobalExceptionHandler` (`@RestControllerAdvice`) et `ApiError` isolés dans un package dédié **`com.tripmint.exception`** (hors `controller/`).
  * `spring.jpa.open-in-view: false` pour interdire le chargement paresseux caché dans les contrôleurs.
  * Règles `@Transactional` : `@Transactional(readOnly = true)` par défaut en lecture, `@Transactional` uniquement sur des écritures courtes, et **jamais autour d'un appel HTTP externe** (afin de ne pas bloquer une connexion JDBC `HikariCP` pendant la latence réseau).
* **Faiblesses & Compromis :**
  * Découpler appel HTTP et transaction implique l'absence de rollback distribué (acceptable pour la recherche en lecture ; demanderait un pattern Outbox / Saga si une réservation transactionnelle était ajoutée).
  * `open-in-view: false` peut provoquer des `LazyInitializationException` si une relation `LAZY` est accédée hors service — c'est un garde-fou volontaire contre le problème **N+1**.

---

## ADR-06 — Front-end Angular 22+ Standalone, Zoneless & Signals (vs l'existant historique)
* **Besoin fonctionnel / EPIC lié :** `EPIC-F1` & `EPIC-F2` (formulaire réactif, filtres rapides, états chargement/erreur fluides) → `EPIC-T1`, `EPIC-T2`.
* **Décision :** Angular 22+ en **Standalone Components**, mode **Zoneless** (sans `zone.js`) et **Signals** (`signal`, `computed`, `effect`), complété par **RxJS** uniquement pour les flux asynchrones complexes (`HttpClient`, `switchMap`, `debounceTime`, annulation).

| Axe | Avant (Angular 2 → 16) | Choix TripMint (Angular 22+) | Faiblesse / Vigilance |
| :--- | :--- | :--- | :--- |
| **Modules** | `NgModule` obligatoires | **Standalone Components** | Attention à garder des composants petits avec peu d'imports. |
| **Change Detection** | `Zone.js` vérifiait tout l'arbre (d'où `OnPush`) | **Zoneless** (mise à jour ciblée sur changement de Signal) | Une propriété mutée hors Signal ne rafraîchit plus la vue (piège avec libs tierces non réactives). |
| **État** | `BehaviorSubject` + `async` pipe + `unsubscribe` | **Signals** (synchrone, sans fuite mémoire) + **RxJS** pour HTTP/flux | Deux paradigmes à articuler proprement (`toSignal`) sans mélanger les rôles. |

* **Action de suivi :** Auditer les conventions front (formulaire, gestion d'état, composants partagés) lors du passage à `EPIC-T2` (`US-T08`) et compléter la couverture par **Playwright** en **`EPIC-T4` (`US-T11`)**.

---

## ADR-07 — Challenge NoSQL (MongoDB / Spark) & Infrastructure (Playwright, SonarQube, Kubernetes)
* **Besoin fonctionnel / EPIC lié :** Préparer l'évolution long terme (`EPIC-F3`, `EPIC-F4`) sans sur-ingénierie en V1 → `EPIC-T4`, `EPIC-T6`.
* **Analyse critique :**
  * **MongoDB :** ❌ Non pertinent en V1/V2 (données relationnelles simples + PostgreSQL 18 gère `JSONB` + index `GIN`). 🟡 À réévaluer en **`EPIC-T6` (`US-T20`)** uniquement si des catalogues d'offres multi-fournisseurs très hétérogènes à TTL court justifient une persistance polyglotte.
  * **Apache Spark :** ❌ Surdimensionné tant que le volume reste sous plusieurs millions d'événements/jour ; les **Window Functions PostgreSQL** (`EPIC-T5` / `US-T14`) couvrent largement l'analyse tarifaire.
  * **Playwright & SonarQube :** Planifiés en **`EPIC-T4` (`US-T11`, `US-T12`)** dès que les premiers parcours utilisateur (`EPIC-F1`) existent.
  * **Kubernetes :** Reporté en **Phase 3–4 (`EPIC-T6` / `US-T20`)** en raison de son coût opérationnel (manifests/Helm, ingress, observabilité, coordination Flyway multi-réplicas).
