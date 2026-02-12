# Conventions Git

## Nommage des branches

Format : `<issue-number>-<type>-<description-courte>`

> Les branches sont créées depuis une issue GitHub. Le numéro d'issue est automatiquement préfixé par GitHub.

| Préfixe     | Usage                                                                    | Exemple                        |
| ----------- | ------------------------------------------------------------------------ | ------------------------------ |
| `feat-`     | Nouvelle fonctionnalité (inclut le design/styling UI)                    | `12-feat-add-budget-form`      |
| `fix-`      | Correction de bug (inclut les bugs visuels : overflow, alignement cassé) | `23-fix-login-redirect`        |
| `chore-`    | Maintenance (dépendances, config)                                        | `55-chore-update-dependencies` |
| `docs-`     | Documentation uniquement                                                 | `56-docs-api-endpoints`        |
| `test-`     | Ajout ou modification de tests                                           | `34-test-user-service`         |
| `refactor-` | Refactoring sans changement fonctionnel ni visuel                        | `40-refactor-budget-service`   |
| `style-`    | Formatage du code source, lint (aucun changement fonctionnel ni visuel)  | `41-style-eslint-fixes`        |
| `perf-`     | Amélioration de performance                                              | `42-perf-optimize-queries`     |
| `ci-cd-`    | Pipeline d'intégration et déploiement continu                            | `43-ci-cd-add-deploy-pipeline` |
| `hotfix-`   | Correction urgente en production                                         | `99-hotfix-critical-auth-bug`  |

### Règles

- Utiliser le **kebab-case** pour la description
- Garder la description **courte et descriptive** (2 à 4 mots)
- Ne pas inclure de numéro de ticket sauf si un système de tracking est en place
- La branche `main` est protégée : jamais de push direct

## Nommage des commits

On suit la spécification [Conventional Commits](https://www.conventionalcommits.org/).

### Format

```
<type>(<scope>): <description>

[body]

[footer]
```

### Types

| Type       | Description                                                                                                         |
| ---------- | ------------------------------------------------------------------------------------------------------------------- |
| `feat`     | Nouvelle fonctionnalité (inclut le design/styling UI)                                                               |
| `fix`      | Correction de bug (inclut les bugs visuels : overflow, alignement cassé)                                            |
| `chore`    | Maintenance (dépendances, config)                                                                                   |
| `refactor` | Refactoring sans changement fonctionnel ni visuel                                                                   |
| `docs`     | Documentation                                                                                                       |
| `test`     | Ajout ou modification de tests                                                                                      |
| `style`    | Formatage du code source uniquement : indentation, point-virgules, espaces, lint. **Ne concerne PAS le CSS/design** |
| `perf`     | Amélioration de performance                                                                                         |
| `ci`       | Changements CI/CD                                                                                                   |
| `build`    | Changements du système de build                                                                                     |

> **Attention** : `style` désigne le formatage du **code source**, pas le design visuel (CSS/Tailwind).
> Ajouter ou modifier du CSS/Tailwind est un `feat` (ajout de design) ou un `fix` (correction visuelle).
> Réorganiser du CSS sans changer le rendu est un `refactor`.

### Scope

Le scope indique la partie du projet affectée :

- `frontend`, `backend`, `api`, `bdd`, `docker`, `config`
- Ou plus spécifique : `auth`, `budget`, `transaction`, `user`

### Règles

- La description commence par un **verbe à l'impératif en anglais** : `add`, `fix`, `update`, `remove`, `refactor`
- La description ne dépasse pas **72 caractères**
- Pas de point final dans la description
- Le body (optionnel) explique le **pourquoi**, pas le quoi
- Un commit = **un changement logique**

### Exemples

```
feat(frontend): add budget creation form

feat(frontend): add styling to budget creation form

fix(frontend): fix overflow on budget card in mobile view

fix(backend): handle null pointer in transaction service

chore(config): update ESLint rules for accessibility

refactor(frontend): reorganize Tailwind classes in BudgetCard

refactor(api): extract validation logic into middleware

style(frontend): fix indentation in BudgetCard component

docs(conventions): add git naming conventions
```

### Breaking changes

Pour les changements non rétrocompatibles, ajouter `!` après le scope :

```
feat(api)!: change authentication from session to JWT

BREAKING CHANGE: All API calls now require a Bearer token in the Authorization header.
```

## Pull Requests

### Titre

Même format que les commits : `<type>(<scope>): <description>`

### Template de description

```markdown
## Résumé

<!-- Résumé en 1-3 bullet points -->

## Changements

<!-- Liste des changements effectués -->

## Comment tester

<!-- Étapes pour vérifier les changements -->

## Captures d'écran

<!-- Si changements UI -->

## Checklist

- [ ] Code reviewé par moi-même
- [ ] Tests ajoutés / mis à jour
- [ ] Documentation mise à jour si nécessaire
- [ ] Pas de console.log / System.out.println oublié
```

### Règles

- Une PR = **une fonctionnalité ou un fix**
- Taille raisonnable : **< 400 lignes modifiées** idéalement
- Toujours créer la PR vers `main` depuis une branche de feature
- Ne pas merge sa propre PR en contexte d'équipe (review obligatoire)
- Squash merge recommandé pour garder un historique propre

## Versioning

On suit le [Semantic Versioning (SemVer)](https://semver.org/) :

```
MAJOR.MINOR.PATCH
```

| Incrément | Quand                                            |
| --------- | ------------------------------------------------ |
| `MAJOR`   | Changement non rétrocompatible (breaking change) |
| `MINOR`   | Nouvelle fonctionnalité rétrocompatible          |
| `PATCH`   | Correction de bug rétrocompatible                |

## Git Hooks (Husky)

Hooks recommandés à mettre en place :

| Hook         | Action                                                     |
| ------------ | ---------------------------------------------------------- |
| `pre-commit` | Lint + format (via lint-staged)                            |
| `commit-msg` | Validation du format Conventional Commits (via commitlint) |
