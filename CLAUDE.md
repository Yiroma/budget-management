# Budget Management

Application full-stack de gestion de budget personnel et partagé.

## Stack technique

| Couche   | Technologie                                                               |
| -------- | ------------------------------------------------------------------------- |
| Frontend | Next.js 16 (App Router), React 19, TypeScript 5, Tailwind CSS 4           |
| Backend  | Spring Boot 4.0.2, Java 21, Maven, Spring Data JPA, Spring Security (JWT) |
| BDD      | PostgreSQL 16                                                             |
| DevOps   | Docker, Docker Compose, Makefile                                          |

## Commandes

```bash
make dev       # Lance l'environnement de développement (Docker)
make test      # Lance tous les tests (front + back)
make lint      # ESLint frontend
make logs      # Logs de tous les containers
make down      # Stoppe et supprime les containers
make prune     # Nettoyage complet (images, volumes, containers)
```

## Architecture

### Backend (couches strictes)

```
Controller → Service → Repository → Database
     ↕           ↕
    DTO        Entity
```

- **Controller** : endpoints REST uniquement, pas de logique métier
- **Service** : toute la logique métier, `@Transactional` sur les mutations
- **Repository** : Spring Data JPA, JPQL préféré au SQL natif
- **Entity** : Lombok (`@Getter`, `@Setter`, `@Builder`), `FetchType.LAZY` par défaut
- **DTO** : Java Records avec Jakarta Validation
- **Mapper** : conversion manuelle Entity <-> DTO

Package : `yiroma.budgetmanagement.{config,controller,service,repository,model,dto,mapper,exception,enums}`

### Frontend (hybride feature-based)

```
frontend/src/
├── app/            # Routing Next.js uniquement (page, layout, loading, error)
├── components/     # UI partagés (ui/, layout/, forms/)
├── features/       # Modules métier (budget/, transaction/, auth/) avec components/, hooks/, services/, types/
├── hooks/          # Hooks partagés
├── lib/            # Client API, utilitaires
├── types/          # Types globaux
└── constants/      # Constantes globales
```

- Server Components par défaut, `'use client'` le plus bas possible
- Named exports pour les composants, `export default` pour les pages Next.js
- TypeScript strict, `any` interdit, union types > enums

## Conventions de code

### Git

- **Branches** : `<issue-number>-<type>-<description>` en kebab-case (ex: `12-feat-add-budget-form`)
- **Commits** : Conventional Commits en anglais — `<type>(<scope>): <description>`
- **Types** : `feat`, `fix`, `chore`, `refactor`, `docs`, `test`, `style`, `perf`, `ci`, `build`
- **Scopes** : `frontend`, `backend`, `api`, `bdd`, `docker`, `config`, ou spécifique (`auth`, `budget`, etc.)
- **PR** : feature → `dev`, puis `dev` → `main`. Titre au format Conventional Commits, squash merge recommandé

> `style` = formatage du code source (lint, indentation). Le CSS/Tailwind est `feat` (ajout) ou `fix` (correction visuelle).

### Nommage

| Élément            | Frontend (TS/React)                                    | Backend (Java)                        |
| ------------------ | ------------------------------------------------------ | ------------------------------------- |
| Composants/Classes | PascalCase                                             | PascalCase                            |
| Fichiers composant | `BudgetCard.tsx`                                       | `BudgetService.java`                  |
| Variables/Méthodes | camelCase                                              | camelCase                             |
| Constantes         | SCREAMING_SNAKE_CASE                                   | SCREAMING_SNAKE_CASE                  |
| Types              | PascalCase + suffixe (Props, State, Response, Request) | PascalCase + suffixe (Dto, Exception) |
| Booléens           | `is/has/can/should` prefix                             | —                                     |
| Handlers           | `handle` prefix                                        | —                                     |
| Callbacks (props)  | `on` prefix                                            | —                                     |

### Formatage

- **Frontend** : 2 espaces, single quotes, semicolons, trailing commas, 80 chars
- **Backend** : 4 espaces, K&R braces, 120 chars

### Imports (Frontend)

Ordre : Node.js natifs → Externes → Internes (`@/`) → Types → Styles (séparés par ligne vide)

## API REST

- Préfixe : `/api/v1/`
- Auth : Bearer Token (JWT)
- Noms pluriels, kebab-case, max 2 niveaux d'imbrication
- Montants en centimes (integer), dates ISO 8601, propriétés JSON en camelCase
- Pagination : `page`, `size`, `sort` (Spring Data)
- Contrat OpenAPI : `backend/src/main/resources/api-contract.yaml`

### Codes HTTP

- `200` GET/PUT/PATCH | `201` POST | `204` DELETE
- `400` validation | `401` non authentifié | `403` non autorisé | `404` introuvable | `409` conflit | `422` incohérence métier

## Base de données

- Tables : snake_case, singulier, en anglais
- Colonnes : snake_case, FK préfixées du nom de la table référencée
- **UUID** pour entités métier (user, operation, account, group, monthly_budget)
- **SERIAL** pour tables de référence (subscription, category, recurrence_rule)
- Montants : `DECIMAL(12, 2)` en PostgreSQL, `BigDecimal` en Java
- Timestamps : `created_at` et `updated_at` sur chaque table
- Relations JPA : toujours `FetchType.LAZY`
- Migrations : Flyway, format `V<n>__<description_snake_case>.sql`

> `user` et `group` sont des mots réservés PostgreSQL : utiliser des guillemets doubles dans le SQL.

## Documentation détaillée

Les conventions complètes se trouvent dans `docs/conventions/` :

1. `01-git.md` — Git, branches, commits, PR
2. `02-code-style.md` — Formatage, nommage, commentaires
3. `03-frontend.md` — React, Next.js, TypeScript, Tailwind
4. `04-backend.md` — Spring Boot, architecture en couches
5. `05-api.md` — REST, HTTP, pagination, CORS
6. `06-database.md` — Schéma, migrations, relations
7. `07-testing.md` — Stratégie de tests
8. `08-quality.md` — Accessibilité, sécurité, SOLID/DRY/KISS/YAGNI

Diagrammes UML dans `docs/diagrams/` (MCD, MLD, MPD, use-case).

## Maintenance de la documentation

Ne pas mettre à jour les README/docs automatiquement à chaque changement. Signaler à l'utilisateur quand une mise à jour de la documentation semble nécessaire (nouvelle feature majeure, changement d'architecture, modification de l'API ou de la structure du projet).
