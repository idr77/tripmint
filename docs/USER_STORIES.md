# Backlog & Suivi des EPICs / User Stories — TripMint

Ce document pilote le backlog produit et technique de **TripMint**. Le journal chronologique des commits est suivi séparément dans [COMMIT_LOG.md](COMMIT_LOG.md).

---

## 1. Processus Produit → Technique (Règle de travail)

Aucune fonctionnalité n'est codée « par la technique ». Chaque livraison suit cet enchaînement strict :

```text
1. EPIC Fonctionnelle       (Valeur métier & parcours utilisateur, sans jargon technique)
        │
        ▼
2. User Stories Fonctionnelles  (Découpage équilibré, priorisé P0/P1/P2, estimé S/M/L)
        │
        ▼
3. PRD (docs/prd/PRD-Fx.md)     (Règles métier précises, cas limites, contrats JSON, UML)
        │
        ▼
4. EPIC Technique           (Traduction en travaux back/front/BDD/infra/tests)
        │
        ▼
5. User Stories Techniques  (Implémentation atomique : 1 US ≈ 1 PR vers develop)
        │
        ▼
6. Architecture déduite     (ADRs & compromis tracés dans docs/ARCHITECTURE.md)
```

> **Légende :**
> * **Priorité :** `P0` Critique MVP · `P1` Important · `P2` Évolution ultérieure
> * **Charge (T-shirt sizing) :** `S` (< 0,5 j) · `M` (0,5–1,5 j) · `L` (2–3 j — à redécouper si > 3 j)
> * **Évolution prévue (non prioritaire) :** éclater ce fichier en **1 fichier par EPIC** (`docs/epics/EPIC-F1.md`, …) dès que le backlog actif grandira.

---

## 2. Personas & Parcours Utilisateurs (Source du Besoin)

* **Voyageur opportuniste (V1) :** dispose d'un budget plafond et cherche un vol au meilleur prix sur **dates fixes (Parcours P1)** ou sur une **fenêtre de dates flexibles (Parcours P2)**.
* **Planificateur attentif aux prix (V2) :** suit un trajet dans le temps, évalue si un prix est une bonne affaire et reçoit une alerte (**Parcours P3**).
* **Organisateur de séjour complet (V3) :** compare avion vs train et intègre l'hébergement dans un budget global (**Parcours P4**).

---

## 3. EPICs Fonctionnels (Valeur Métier Pure)

### Vue synthétique des EPICs Fonctionnels

| EPIC | Jalon | Parcours | Objectif métier | Priorité | Charge | PRD | Statut |
| :--- | :---: | :---: | :--- | :---: | :---: | :---: | :--- |
| **EPIC-F1** | **V1** | P1 | Trouver le vol le moins cher sur des dates fixes | `P0` | `L` | `docs/prd/PRD-F1.md` *(à rédiger)* | ⚪ À cadrer (Prochaine étape) |
| **EPIC-F2** | **V1** | P2 | Explorer les meilleurs créneaux sur dates flexibles selon un budget | `P1` | `L` | `docs/prd/PRD-F2.md` *(à rédiger)* | ⚪ À faire |
| **EPIC-F3** | **V2** | P3 | Suivre l'évolution des prix, gérer son compte et recevoir des alertes | `P1` | `L` | `docs/prd/PRD-F3.md` *(V2)* | ⚪ Planifié |
| **EPIC-F4** | **V3** | P4 | Comparer plusieurs modes de transport (train/avion) et l'hébergement | `P2` | `L` | `docs/prd/PRD-F4.md` *(V3)* | ⚪ Planifié |

---

### EPIC-F1 — Trouver le vol le moins cher sur des dates fixes *(V1 · P0 · Parcours P1)*

> **Objectif :** permettre à un voyageur sans compte de comparer rapidement des vols sur un trajet et des dates précises, en mettant en avant le prix et la clarté de l'itinéraire.

| ID | User Story fonctionnelle | Priorité | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-F01** | **En tant que** voyageur, **je veux** saisir une ville/aéroport de départ, une destination, une date aller (et retour optionnelle) et le nombre de passagers, **afin de** consulter les vols disponibles triés du moins cher au plus cher. | `P0` | `M` | ⚪ À faire |
| **US-F02** | **En tant que** voyageur soucieux de mon budget, **je veux** indiquer un plafond budgétaire optionnel, **afin de** ne voir que les offres compatibles avec mon budget (ou être informé clairement si aucune ne convient). | `P0` | `S` | ⚪ À faire |
| **US-F03** | **En tant que** voyageur, **je veux** ouvrir le détail d'une offre (compagnie, horaires, durée totale, nombre et lieux d'escales, prix total), **afin de** décider si l'itinéraire me convient. | `P0` | `S` | ⚪ À faire |
| **US-F04** | **En tant que** voyageur, **je veux** avoir un retour clair pendant le chargement, en cas de critères invalides, d'absence de vols ou d'indisponibilité temporaire d'un partenaire, **afin de** savoir quoi corriger ou réessayer. | `P0` | `S` | ⚪ À faire |

---

### EPIC-F2 — Explorer les meilleurs créneaux sur dates flexibles selon un budget *(V1 · P1 · Parcours P2)*

> **Objectif :** permettre au voyageur qui a des dates souples de trouver automatiquement les meilleures combinaisons de dates sous son budget plafond.

| ID | User Story fonctionnelle | Priorité | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-F05** | **En tant que** voyageur flexible, **je veux** saisir un départ, une destination, une fenêtre de dates, une durée de séjour (en jours) et un budget maximum, **afin que** TripMint explore les créneaux possibles pour moi. | `P1` | `M` | ⚪ À faire |
| **US-F06** | **En tant que** voyageur flexible, **je veux** comparer les meilleurs créneaux trouvés (dates aller/retour, prix, écart par rapport au budget), **afin de** choisir le départ le plus avantageux. | `P1` | `M` | ⚪ À faire |

---

### EPIC-F3 — Suivre les prix, sauvegarder ses recherches & être alerté *(V2 · P1 · Parcours P3)*

| ID | User Story fonctionnelle | Priorité | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-F07** | **En tant que** voyageur, **je veux** voir si le tarif affiché est inférieur ou supérieur au prix moyen observé récemment sur ce trajet, **afin de** savoir si c'est le bon moment pour acheter. | `P1` | `M` | ⚪ Planifié |
| **US-F08** | **En tant que** voyageur régulier, **je veux** créer un compte personnel et sauvegarder mes recherches favorites, **afin de** les retrouver facilement sans les ressaisir. | `P1` | `M` | ⚪ Planifié |
| **US-F09** | **En tant qu'** utilisateur connecté, **je veux** définir un prix cible sur un trajet suivi et être alerté dès qu'une offre passe dessous, **afin de** ne rater aucune baisse tarifaire. | `P1` | `M` | ⚪ Planifié |

---

### EPIC-F4 — Comparer avion, train et hébergement dans un budget global *(V3 · P2 · Parcours P4)*

| ID | User Story fonctionnelle | Priorité | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-F10** | **En tant que** voyageur, **je veux** comparer des trajets en train et en avion dans une même recherche, **afin de** choisir le meilleur compromis prix / temps / confort. | `P2` | `L` | ⚪ Planifié |
| **US-F11** | **En tant que** voyageur, **je veux** inclure une estimation d'hébergement dans mon budget plafond, **afin de** piloter le coût total de mon séjour. | `P2` | `L` | ⚪ Planifié |
| **US-F12** | **En tant que** voyageur, **je veux** trier les propositions selon un score combinant prix, durée et escales, **afin de** repérer immédiatement l'option la plus équilibrée. | `P2` | `M` | ⚪ Planifié |

---

## 4. EPICs Techniques (Déduits des EPICs Fonctionnels & Audits)

### Vue synthétique des EPICs Techniques

| EPIC Tech | Jalon | Sert l'EPIC Fonctionnelle | Objectif technique | Priorité | Charge | Statut |
| :--- | :---: | :---: | :--- | :---: | :---: | :--- |
| **EPIC-T1** | **V1** | Socle commun | Monorepo Spring MVC, Flyway, H2/PostgreSQL Docker, Angular 22+, Conventions & CI | `P0` | `L` | ✅ **Mergé (`develop`, PR `#4`)** *(ajustements en cours sur `chore/refactor-docs-and-conventions`)* |
| **EPIC-T2** | **V1** | `EPIC-F1` | PRD, contrats API, connecteur vols (Adapter), API dates fixes, transactions & UI Angular | `P0` | `L` | ⚪ À faire *(après rédaction `PRD-F1.md`)* |
| **EPIC-T3** | **V1** | `EPIC-F2` | Moteur combinatoire dates flexibles, rate-limit/cache, UI flexibles & release V1 | `P1` | `M` | ⚪ À faire *(après `EPIC-T2`)* |
| **EPIC-T4** | **V2** | Audit Qualité & CI | Tests E2E **Playwright**, analyse **SonarQube**, **Testcontainers** PostgreSQL 18 | `P1` | `M` | ⚪ Planifié *(issu de l'audit `ARCHITECTURE.md`)* |
| **EPIC-T5** | **V2** | `EPIC-F3` | Historisation SQL (Window Functions), Auth JWT, alertes & CD PostgreSQL `prod` | `P1` | `L` | ⚪ Planifié |
| **EPIC-T6** | **V3 / Ph. 3–4** | `EPIC-F4` & Ops | Strategy multi-fournisseurs, Virtual Threads bornés, **Kubernetes** & étude NoSQL | `P2` | `L` | ⚪ Planifié |

---

### EPIC-T1 — Socle Monorepo, Conventions, Docker & CI *(V1 · P0 — ✅ Mergé dans `develop`)*

| ID | User Story technique | Issue GitHub | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-T01** | Configuration Git (`develop`/`main`), `.gitignore`, `.env.example`, `AGENTS.md`, doc structurée et CI GitHub Actions | `#1` | `S` | ✅ Fait *(protections branches GitHub à confirmer)* |
| **US-T02** | Environnement local PostgreSQL 18 isolé dans `backend/docker-compose.yml` (volume `/var/lib/postgresql`, `pg_isready`, validation `prod`) | `#2` | `S` | ✅ Fait *(déplacement dans `backend/` sur branche courante)* |
| **US-T03** | Socle Back-end Spring MVC (Java 25, Spring Boot 4.1.2, Flyway `V1__init.sql`, H2/PostgreSQL, `/api/v1/health`, `exception/GlobalExceptionHandler`) | `#3` | `M` | ✅ Fait *(cleanup `exception/` & `package-info` sur branche courante)* |
| **US-T04** | Socle Front-end Angular 22.3 Standalone Zoneless / Signals, SCSS, strict, proxy `/api`, Vitest | — | `S` | ✅ Fait |

---

### EPIC-T2 — Implémentation Technique de `EPIC-F1` (Vols dates fixes) *(V1 · P0)*

> **Prérequis strict :** rédiger et valider `docs/prd/PRD-F1.md` + diagrammes `docs/uml/` avant de démarrer `US-T06`.

| ID | User Story technique | Sert | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-T05** | Rédiger `docs/prd/PRD-F1.md` (règles métier, cas d'erreur, contrats JSON `/api/v1/flights/search`), diagramme de séquence UML (`docs/uml/`) et acter le fournisseur de vols (**Duffel Test Mode** ou **SerpApi**, en remplacement d'Amadeus Self-Service fermé en juillet 2026) | `US-F01`→`F04` | `S` | ⚪ À faire |
| **US-T06** | Implémenter l'interface `FlightProvider` et son **Adapter** HTTP (`RestClient`, `com.tripmint.client.<fournisseur>`) + stub/mock pour tests/CI + gestion timeouts/erreurs fournisseur | `US-F01`, `US-F04` | `M` | ⚪ À faire |
| **US-T07** | Implémenter l'endpoint REST `/api/v1/flights/search` (`@Valid`, filtrage budget, tri prix, journalisation `search_logs` en transaction courte hors appel HTTP) + tests unitaires & MockMvc | `US-F01`, `US-F02`, `US-F04` | `M` | ⚪ À faire |
| **US-T08** | Implémenter l'interface Angular de `EPIC-F1` (service HTTP, formulaire Reactive Forms typé, état Signals, liste triée, vue détail itinéraire, états chargement/vide/erreur) + tests Vitest | `US-F01`→`F04` | `M` | ⚪ À faire |

---

### EPIC-T3 — Implémentation Technique de `EPIC-F2` (Dates flexibles & Release V1) *(V1 · P1)*

> **Prérequis strict :** `EPIC-T2` terminée + `docs/prd/PRD-F2.md` validé.

| ID | User Story technique | Sert | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-T09** | Implémenter le moteur combinatoire borné de dates flexibles (génération de créneaux, garde-fous rate-limit / déduplication, filtrage budget) + tests unitaires approfondis sur les cas limites | `US-F05`, `US-F06` | `M` | ⚪ À faire |
| **US-T10** | Ajouter le mode « Dates flexibles » dans le formulaire et les résultats Angular + tests + **PR de jalon `develop` → `main` (`v1.0.0`)** | `US-F05`, `US-F06` | `M` | ⚪ À faire |

---

### EPIC-T4 — Audit Qualité, Tests E2E & Fiabilité CI *(V2 · P1 — issu de l'audit d'architecture)*

| ID | User Story technique | Objectif | Charge | Statut |
| :--- | :--- | :--- | :---: | :--- |
| **US-T11** | Mettre en place **Playwright** et écrire les tests E2E fonctionnels des parcours P1 & P2 (avec fournisseur mocké en CI) | Détecter les régressions front/back | `M` | ⚪ Planifié |
| **US-T12** | Intégrer **SonarQube / SonarCloud** dans la CI (export couverture JaCoCo + Vitest, Quality Gate) | Suivre dette technique, bugs et couverture | `S` | ⚪ Planifié |
| **US-T13** | Ajouter **Testcontainers (PostgreSQL 18)** sur les tests d'intégration SQL critiques | Éliminer le risque de divergence H2 vs PostgreSQL 18 | `S` | ⚪ Planifié |

---

### EPIC-T5 — Implémentation Technique de `EPIC-F3` (Historique, Auth, Alertes & Prod) *(V2 · P1)*

| ID | User Story technique | Sert | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-T14** | Migration Flyway `price_snapshots`, tâche planifiée `@Scheduled` (transactions courtes) et requêtes analytiques PostgreSQL (*Window Functions*) | `US-F07` | `M` | ⚪ Planifié |
| **US-T15** | Authentification Spring Security + JWT (hachage BCrypt, rôles) + intercepteur & route guard Angular | `US-F08` | `M` | ⚪ Planifié |
| **US-T16** | CRUD recherches favorites & moteur de détection d'alertes sous budget (pivot *Package by Feature*) | `US-F08`, `US-F09` | `M` | ⚪ Planifié |
| **US-T17** | Pipeline CD `.github/workflows/cd.yml` sur PostgreSQL managé (`prod`) + **PR `develop` → `main` (`v2.0.0`)** | Déploiement V2 | `M` | ⚪ Planifié |

---

### EPIC-T6 — Implémentation Technique de `EPIC-F4` & Scalabilité *(V3 / Phase 3–4 · P2)*

| ID | User Story technique | Sert | Charge | Statut |
| :--- | :--- | :---: | :---: | :--- |
| **US-T18** | Refactoring en pattern **Strategy** (`TransportProvider`, `StayProvider`), connecteurs train & hébergement, algorithme de scoring prix/durée | `US-F10`→`F12` | `L` | ⚪ Planifié |
| **US-T19** | Parallélisation multi-fournisseurs par **Virtual Threads** bornés (`Semaphore`, timeouts) + benchmark de charge | Perf. multi-fournisseurs | `M` | ⚪ Planifié |
| **US-T20** | Conteneurisation complète (`Dockerfile` multi-stage), déploiement **Kubernetes** (manifests/Helm, probes `/api/v1/health`) + réévaluation NoSQL/Cache (Redis/MongoDB) sur métriques réelles | Scalabilité Ph. 3–4 | `L` | ⚪ Planifié |
