# Documentation — TripMint

Ce dossier regroupe l'ensemble de la documentation vivante du projet. Chaque fichier a une **responsabilité unique** pour éviter la duplication et faciliter la navigation (humains et agents IA).

---

## Cartographie & Liens entre Documentations

```text
[README.md]  (Entrée rapide & commandes)
     │
     ├──► [AGENTS.md]                    (Règles IA, principes code, stratégie tests, workflow)
     │
     └──► [docs/README.md]               (Ce hub)
               │
               ├──► [USER_STORIES.md]           (1. EPICs Fonctionnels → US Fonctionnelles → EPICs/US Techniques)
               │         │
               │         ├──► docs/prd/         (2. PRD & Contrats API JSON par EPIC fonctionnel — à venir)
               │         ├──► docs/uml/         (3. Diagrammes UML : séquences, cas d'utilisation, MCD — à venir)
               │         └──► [COMMIT_LOG.md]   (Suivi chronologique des commits liés aux US)
               │
               ├──► [ARCHITECTURE.md]           (4. ADRs déduits du fonctionnel, faiblesses & compromis)
               ├──► [ArchitectureRoadMap.md]    (5. Structure du dépôt & trajectoire d'évolution V1 → Phase 4)
               └──► [TECHNICAL_TARGETS.md]      (6. Cibles techniques & compétences à travailler)
```

---

## Index des Fichiers

| Fichier / Dossier | Contenu | Quand le mettre à jour |
| :--- | :--- | :--- |
| **[USER_STORIES.md](USER_STORIES.md)** | EPICs fonctionnels, EPICs techniques, User Stories, priorités (`P0`–`P2`), charges (`S`/`M`/`L`) et statuts | À chaque création, avancement ou clôture d'une US |
| **[COMMIT_LOG.md](COMMIT_LOG.md)** | Journal chronologique des commits reliés aux EPICs / US | **À chaque commit** |
| **[ARCHITECTURE.md](ARCHITECTURE.md)** | Décisions d'architecture (**ADR**) rattachées aux besoins fonctionnels et EPICs techniques + faiblesses/compromis | À chaque décision structurante ou changement de fournisseur/stack |
| **[ArchitectureRoadMap.md](ArchitectureRoadMap.md)** | Arborescence du monorepo et évolution cible par phase (V1 → V2 → V3 → Phase 4) | Lorsqu'un dossier/module structurant évolue ou qu'une phase est franchie |
| **[TECHNICAL_TARGETS.md](TECHNICAL_TARGETS.md)** | Objectifs d'apprentissage & sujets techniques ciblés (SQL, Patterns, Playwright, SonarQube, K8s…) | À chaque jalon ou compétence validée |
| `docs/prd/` *(à créer dès `EPIC-F1`)* | Product Requirements Documents (`PRD-F1.md`, …) et contrats JSON `/api/v1/*` | Avant de découper/coder une EPIC technique |
| `docs/uml/` *(à créer dès `EPIC-F1`)* | Diagrammes UML (Mermaid / PlantUML) : séquences, cas d'utilisation, MCD/MLD | En parallèle des PRD et des migrations Flyway |
