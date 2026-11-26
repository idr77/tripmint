# Backlog & Suivi des User Stories — TripMint

Ce document recense l'ensemble des User Stories du projet **TripMint** par jalon (**V1**, **V2**, **V3**) ainsi que leur état d'avancement réel.

> **Rappel du workflow Git :**
> * Chaque User Story est développée sur une branche dédiée depuis `develop` (`feature/<id>-...`, `chore/<id>-...`) et livrée via une **Pull Request vers `develop`**.
> * Les **Pull Requests vers `main`** sont réservées aux livraisons de jalons (**V1**, **V2**, **V3**…).

---

## Vue d'ensemble de l'avancement

| Jalon | Terminées | En cours / À merger | À faire | Progression |
| :--- | :---: | :---: | :---: | :--- |
| **V1 — Socle & Recherche de Vols (MVP)** | 0 *(2 prêtes à merger)* | 3 (`US-01`, `US-02`, `US-03`) | 6 (`US-04` → `US-09`) | Socle prêt sur `chore/init-skeleton` |
| **V2 — Historisation, Tendances & Comptes** | 0 | 0 | 5 (`US-10` → `US-14`) | Planifié |
| **V3 — Extension Multi-Modale** | 0 | 0 | 3 (`US-15` → `US-17`) | Planifié |

### Tableau synthétique

| ID | Jalon | Titre | Issue GitHub | Statut |
| :--- | :---: | :--- | :---: | :--- |
| **US-01** | V1 | Configuration du dépôt Git, conventions & CI (`develop` / `main`) | `#1` | 🟡 En cours *(code prêt sur `chore/init-skeleton`, protections GitHub à vérifier)* |
| **US-02** | V1 | Initialisation du socle Back-end Spring MVC + Flyway (H2 / PostgreSQL) | `#3` *(adaptée)* | 🟡 Prêt à merger sur `develop` *(validation `./mvnw test` OK)* |
| **US-03** | V1 | Initialisation du socle Front-end Angular 22+ Standalone Zoneless | — | 🟡 Prêt à merger sur `develop` *(validation `npm run build` & tests OK)* |
| **US-04** | V1 | Conception des contrats API V1 & schémas (`docs/`) | — | ⚪ À faire |
| **US-05** | V1 | Intégration client Amadeus (OAuth2 + vols dates fixes + Adapter) | — | ⚪ À faire |
| **US-06** | V1 | API REST de recherche de vols à dates fixes + journalisation (`search_logs`) | — | ⚪ À faire |
| **US-07** | V1 | Moteur de recherche à dates flexibles sous contrainte de budget | — | ⚪ À faire |
| **US-08** | V1 | Interface Angular : formulaire de recherche & liste des vols triés par prix | — | ⚪ À faire |
| **US-09** | V1 | Détail d'itinéraire, gestion des erreurs/états UI & release **V1** (`develop` → `main`) | — | ⚪ À faire |
| **US-10** | V2 | Relevé périodique & historisation des prix | — | ⚪ À faire |
| **US-11** | V2 | Analyse de tendances & détection de baisses tarifaires | — | ⚪ À faire |
| **US-12** | V2 | Authentification utilisateur (JWT & Spring Security) | — | ⚪ À faire |
| **US-13** | V2 | Recherches favorites & alertes automatiques de baisse de prix | — | ⚪ À faire |
| **US-14** | V2 | Déploiement continu sur PostgreSQL managé (`prod`) & release **V2** | — | ⚪ À faire |
| **US-15** | V3 | Abstraction multi-fournisseurs (Strategy) & connecteur ferroviaire (train) | — | ⚪ À faire |
| **US-16** | V3 | Module hébergement & calcul du budget global transport + logement | — | ⚪ À faire |
| **US-17** | V3 | Recommandations d'itinéraires optimisés budget / durée & release **V3** | — | ⚪ À faire |

---

## Alignement avec les Issues GitHub existantes (`#1`, `#2`, `#3`)

Les 3 issues actuellement ouvertes sur GitHub ont été créées avant le recentrage pragmatique V1 (passage à Spring MVC classique sans Spring Modulith et H2 en local/tests). Voici leur statut exact :

| Issue GitHub | Titre actuel | Statut / Action recommandée |
| :--- | :--- | :--- |
| **#1** | *Mise en place du dépot Git* | **En cours** — `.gitignore`, `.env.example`, `.github/ISSUE_TEMPLATE/user_story.md`, `AGENTS.md` et `.github/workflows/ci.yml` sont prêts sur `chore/init-skeleton`. Vérifier la protection des branches `main` et `develop` dans les paramètres GitHub, puis fermer à la fusion de la PR vers `develop`. |
| **#2** | *Mise en place de l'environnement local PostgreSQL via Docker Compose* | **Obsolète pour la V1** — Remplacée par H2 en mémoire (`MODE=PostgreSQL`) + Flyway pour le local/tests, et PostgreSQL en profil `prod`. Peut être fermée comme *not planned* (ou reportée en option V2 pré-prod). |
| **#3** | *Initialisation du squelette Back-end Spring Boot 4 & Spring Modulith* | **Remplacée par US-02** — L'architecture retenue est **Spring MVC classique** (sans Spring Modulith). Mettre à jour le titre/description de `#3` (ou la fermer au profit de la PR `chore/init-skeleton` → `develop`). |

---

## Jalon V1 — Socle & Recherche de Vols (MVP)

### US-01 — Configuration du dépôt Git, conventions & CI (`develop` / `main`)
* **Statut :** 🟡 En cours *(branche `chore/init-skeleton`, à merger dans `develop`)*
* **Issue GitHub :** `#1`
* **Besoin utilisateur :** En tant que développeur, je veux disposer d'un dépôt propre avec conventions d'ingénierie, template de User Story et pipeline CI, afin d'assurer la qualité et la traçabilité dès le premier commit.
* **Critères d'acceptation :**
  - [x] `.gitignore` complet (Java/Maven, Node/Angular, `.env`, IDE/OS)
  - [x] `.env.example` documentant `AMADEUS_CLIENT_ID`, `AMADEUS_CLIENT_SECRET`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
  - [x] Template `.github/ISSUE_TEMPLATE/user_story.md` et guide `AGENTS.md` (KISS, DRY, SOLID, workflow)
  - [x] Workflow `.github/workflows/ci.yml` déclenché sur PR vers `develop` et `main`
  - [ ] Règles de protection configurées sur GitHub pour `main` et `develop` (PR obligatoire + CI verte)
  - [ ] PR `chore/init-skeleton` → `develop` mergée avec CI verte

---

### US-02 — Initialisation du socle Back-end Spring MVC + Flyway
* **Statut :** 🟡 Prêt à merger sur `develop` *(branche `chore/init-skeleton`)*
* **Issue GitHub :** `#3` *(adaptée en Spring MVC classique)*
* **Besoin utilisateur :** En tant que développeur back-end, je veux un squelette Spring Boot 4 / Java 25 en architecture Spring MVC avec Flyway et H2/PostgreSQL, afin de bâtir les fonctionnalités V1 sur des fondations validées.
* **Critères d'acceptation :**
  - [x] Projet Maven Java 25 + Spring Boot 4.1.2 (`webmvc`, `data-jpa`, `validation`, `flyway`, `h2`, `postgresql`, `test`)
  - [x] Packages `com.tripmint` (`config`, `controller`, `service`, `client`, `repository`, `model`)
  - [x] Endpoint `GET /api/v1/health` et `@RestControllerAdvice` global (`GlobalExceptionHandler`)
  - [x] Profils `local` (H2 `MODE=PostgreSQL`, console `/h2-console`, `ddl-auto=validate`) et `prod` (PostgreSQL env vars, `ddl-auto=validate`)
  - [x] Migration `V1__init.sql` (`search_logs`) compatible H2 et PostgreSQL + entité `SearchLog`
  - [x] `./mvnw test` passant au vert (3/3 tests d'intégration)
  - [ ] Mergé dans `develop`

---

### US-03 — Initialisation du socle Front-end Angular 22+
* **Statut :** 🟡 Prêt à merger sur `develop` *(branche `chore/init-skeleton`)*
* **Besoin utilisateur :** En tant que développeur front-end, je veux une application Angular 22+ Standalone Zoneless avec proxy vers l'API locale, afin de développer rapidement les écrans V1.
* **Critères d'acceptation :**
  - [x] Application Angular 22.3 Standalone Components, détection Zoneless (sans `zone.js`), SCSS, TypeScript strict (`strict: true`, `strictTemplates: true`)
  - [x] `proxy.conf.json` redirigeant `/api` vers `http://localhost:8080` configuré dans `angular.json`
  - [x] Composant d'accueil minimal TripMint + tests Vitest passants
  - [x] `npm run build` et `npm test -- --watch=false` passants au vert
  - [ ] Mergé dans `develop`

---

### US-04 — Conception des contrats API V1 & schémas (`docs/`)
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que développeur Full Stack, je veux formaliser les contrats JSON de recherche de vols et le modèle de données V1 avant de coder le métier, afin d'aligner le back-end et le front-end.
* **Critères d'acceptation :**
  - [ ] Contrat JSON des endpoints de recherche (`/api/v1/flights/search`, requêtes, réponses, erreurs `ApiError`) documenté dans `docs/`
  - [ ] Schéma UML de séquence (Angular → Controller → Service → Amadeus Adapter → Repository) ajouté dans `docs/`
  - [ ] Modèle de données V1 (évolution éventuelle de `search_logs`) validé

---

### US-05 — Intégration du client Amadeus (OAuth2 + vols dates fixes)
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que système TripMint, je veux interroger l'API Sandbox Amadeus de manière isolée et sécurisée, afin de récupérer des offres de vols réelles sans coupler le domaine métier au format externe.
* **Critères d'acceptation :**
  - [ ] Configuration Amadeus externalisée via `AMADEUS_CLIENT_ID` et `AMADEUS_CLIENT_SECRET`
  - [ ] Authentification OAuth2 *Client Credentials* avec mise en cache du token jusqu'à expiration
  - [ ] Client HTTP dédié dans `com.tripmint.client.amadeus` utilisant `RestClient` Spring
  - [ ] Pattern **Adapter** traduisant les DTOs bruts Amadeus en modèles métier TripMint (`FlightOffer`)
  - [ ] Gestion propre des timeouts / erreurs HTTP Amadeus (traduite via `GlobalExceptionHandler`)
  - [ ] Tests unitaires / MockRestServiceServer couvrant succès, renouvellement de token et erreurs fournisseur

---

### US-06 — API REST de recherche de vols à dates fixes & journalisation
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux rechercher des vols sur des dates précises (aller simple ou aller-retour) dans la limite d'un budget optionnel, afin de comparer les meilleures offres triées par prix.
* **Critères d'acceptation :**
  - [ ] Endpoint REST `/api/v1/flights/search` validant les paramètres (`@Valid` : codes IATA, dates cohérentes, budget > 0, passagers >= 1)
  - [ ] Filtrage sur le budget maximum (si renseigné) et tri croissant par prix
  - [ ] Journalisation de chaque recherche dans `search_logs` (avec migration Flyway `V2__...sql` si colonnes supplémentaires)
  - [ ] Tests unitaires (service) et tests d'intégration MockMvc (cas nominaux + erreurs 400 de validation)

---

### US-07 — Moteur de recherche à dates flexibles sous contrainte de budget
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur flexible, je veux saisir une ville de départ, un budget plafond, une durée de séjour et une fenêtre de dates, afin de découvrir automatiquement les combinaisons de dates les moins chères.
* **Critères d'acceptation :**
  - [ ] Génération bornée des couples de dates `(départ, retour)` dans la fenêtre demandée pour une durée donnée
  - [ ] Exécution maîtrisée des requêtes fournisseur (gestion du rate-limit sandbox Amadeus et déduplication)
  - [ ] Filtrage strict des offres `<= maxBudget` et classement par meilleur prix
  - [ ] Tests unitaires dédiés sur le calcul des fenêtres de dates et le filtrage budgétaire

---

### US-08 — Interface Angular : recherche de vols & résultats triés par prix
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux un formulaire clair (mode dates fixes / dates flexibles) et une liste lisible d'offres triées par prix, afin de trouver rapidement un vol adapté à mon budget.
* **Critères d'acceptation :**
  - [ ] Service Angular `FlightSearchService` appelant `/api/v1/flights/search` via `HttpClient`
  - [ ] Formulaire **Reactive Forms** typé avec bascule Dates fixes / Dates flexibles et validation côté client
  - [ ] Gestion d'état réactive via **Angular Signals** (mode Zoneless)
  - [ ] Affichage des cartes de vols (compagnie, horaires, durée, escales, prix) triées par prix croissant
  - [ ] Tests unitaires Vitest sur le service et le composant de recherche

---

### US-09 — Détail d'itinéraire, états UI & livraison du jalon V1
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux consulter le détail d'un itinéraire et recevoir des retours clairs en cas de chargement, absence de résultat ou erreur API, afin de disposer d'une V1 fiable de bout en bout.
* **Critères d'acceptation :**
  - [ ] Vue détaillée d'une offre sélectionnée (segments aller/retour, escales, récapitulatif tarifaire)
  - [ ] États UI soignés : chargement (*skeleton/spinner*), liste vide, affichage des messages `ApiError`
  - [ ] Tests back-end et front-end complets au vert
  - [ ] **Pull Request de jalon `develop` → `main`** validée par la CI et tag `v1.0.0`

---

## Jalon V2 — Historisation, Tendances & Comptes Utilisateurs

### US-10 — Relevé périodique & historisation des prix
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que système TripMint, je veux enregistrer périodiquement les prix des trajets suivis, afin de constituer un historique fiable.
* **Critères d'acceptation :**
  - [ ] Migration Flyway pour la table d'historique des prix (`price_snapshots`)
  - [ ] Tâche planifiée (`@Scheduled`) enregistrant les relevés sans bloquer l'API

### US-11 — Analyse de tendances & détection de baisses tarifaires
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux voir si le prix actuel d'un trajet est inférieur à sa moyenne historique, afin de savoir si c'est le bon moment pour réserver.
* **Critères d'acceptation :**
  - [ ] Calcul de la moyenne et de l'écart tarifaire par trajet
  - [ ] Indicateur visuel de tendance / baisse sur l'interface Angular

### US-12 — Authentification utilisateur (JWT & Spring Security)
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux créer un compte et me connecter de façon sécurisée, afin de retrouver mes recherches et préférences.
* **Critères d'acceptation :**
  - [ ] Endpoints d'inscription et de connexion sécurisés (mots de passe hachés, JWT)
  - [ ] Intercepteur HTTP Angular et garde de route pour les pages protégées

### US-13 — Recherches favorites & alertes automatiques de baisse de prix
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant qu'utilisateur connecté, je veux sauvegarder des recherches cibles et être alerté dès qu'un prix passe sous mon budget, afin de ne rater aucune bonne affaire.
* **Critères d'acceptation :**
  - [ ] CRUD des recherches suivies / alertes (back-end + front-end)
  - [ ] Déclenchement d'une alerte lorsqu'un relevé passe sous le seuil configuré

### US-14 — Déploiement continu sur PostgreSQL managé (`prod`) & release V2
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que mainteneur, je veux déployer TripMint sur une instance PostgreSQL réelle avec le profil `prod`, afin de rendre l'application accessible en production.
* **Critères d'acceptation :**
  - [ ] Validation complète des migrations Flyway sur PostgreSQL réel (profil `prod`)
  - [ ] **Pull Request de jalon `develop` → `main`** validée et tag `v2.0.0`

---

## Jalon V3 — Extension Multi-Modale (Train & Hébergement)

### US-15 — Abstraction multi-fournisseurs (Strategy) & connecteur ferroviaire
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux comparer des trajets en train en plus de l'avion dans une même recherche, afin de choisir l'option la plus avantageuse.
* **Critères d'acceptation :**
  - [ ] Interface `TransportProvider` (pattern Strategy) réunissant avion et train
  - [ ] Intégration d'un fournisseur ferroviaire et affichage unifié côté Angular

### US-16 — Module hébergement & budget global transport + logement
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux inclure l'hébergement dans mon plafond budgétaire, afin d'estimer le coût réel complet de mon séjour.
* **Critères d'acceptation :**
  - [ ] Connecteur d'hébergement (`StayProvider`) et agrégation du coût total `transport + hébergement`
  - [ ] Filtrage sur le budget global côté API et affichage détaillé côté front-end

### US-17 — Recommandations d'itinéraires optimisés budget / durée & release V3
* **Statut :** ⚪ À faire
* **Besoin utilisateur :** En tant que voyageur, je veux obtenir un classement intelligent combinant prix et temps de trajet, afin de sélectionner rapidement le meilleur compromis.
* **Critères d'acceptation :**
  - [ ] Algorithme de scoring configurable (prix / durée / escales) testé unitairement
  - [ ] **Pull Request de jalon `develop` → `main`** validée et tag `v3.0.0`
