# Journal des Commits — TripMint

Ce fichier trace chronologiquement les commits significatifs du dépôt et leur rattachement aux **EPICs** et **User Stories** définis dans [USER_STORIES.md](USER_STORIES.md).

Conformément à [AGENTS.md](../AGENTS.md), ce journal est mis à jour **à chaque commit**.

---

| Date | Commit | Branche / PR | EPIC / US | Résumé |
| :--- | :--- | :--- | :---: | :--- |
| 2026-11-19 | `dc00ded` | `chore/init-skeleton` (PR `#4`) | `EPIC-T1` / `US-T01`, `US-T03`, `US-T04` | Initialisation du squelette monorepo V1 (Spring MVC Java 25, Flyway/H2/PostgreSQL, Angular 22.3 Zoneless, `AGENTS.md`, CI) |
| 2026-11-25 | `1d79c98` | `chore/init-skeleton` (PR `#4`) | `EPIC-T1` / `US-T01` | Alignement workflow PR (`develop` pour features, `main` pour jalons), CI sur `develop`/`main` et création du backlog |
| 2026-11-26 | `329574f` | `chore/init-skeleton` (PR `#4`) | `EPIC-T1` / `US-T02` | Ajout de `docker-compose.yml` (`postgres:18-alpine`, volume `/var/lib/postgresql`, healthcheck `pg_isready`) et variables `DB_NAME`/`DB_PORT` |
| 2026-11-26 | `2d9339a` | `chore/init-skeleton` (PR `#4`) | `EPIC-T1` / `US-T01` | Enrichissement de la CI (tests Vitest, validation Docker Compose, `push` sur `develop`/`main`, `concurrency`) |
| 2026-11-26 | `25a6d37` | `chore/init-skeleton` (PR `#4`) | `EPIC-T1` / `US-T01` | Ajout `docs/ARCHITECTURE.md` (ADR, faiblesses, Signals, NoSQL, transactions) et règles IA dans `AGENTS.md` |
| 2026-11-26 | `8a93679` | `chore/init-skeleton` (PR `#4`) | `EPIC-T1` / `US-T02` | Validation runtime Docker Compose PostgreSQL 18.7 (`healthy`, persistance volume après restart, tests Spring Boot/Flyway `prod` OK) |
| 2026-11-26 | `5b65f47` | `develop` (Merge PR `#4`) | `EPIC-T1` | Merge du socle complet V1 dans `develop` |
| 2026-11-26 | `6a549a0` | `chore/refactor-docs-and-conventions` | `EPIC-T1` (`US-T02`, `US-T03`) | Déplacement `GlobalExceptionHandler` & `ApiError` dans `com.tripmint.exception`, suppression `client/package-info.java`, isolation `backend/docker-compose.yml` (+ CI), ajout `Notes.md` au `.gitignore` |
| 2026-11-26 | *(commit courant)* | `chore/refactor-docs-and-conventions` | `EPIC-T1` (`US-T01`) | Refonte documentaire post-point 26/11 : séparation EPICs Fonctionnels/Techniques (`USER_STORIES.md`), `COMMIT_LOG.md`, `ArchitectureRoadMap.md`, `TECHNICAL_TARGETS.md`, `ARCHITECTURE.md` relié au fonctionnel, `README.md` orienté dev/IA et stratégie de tests détaillée dans `AGENTS.md` |
