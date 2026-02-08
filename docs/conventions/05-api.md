# Conventions API

## Architecture générale

- **Style** : RESTful
- **Format** : JSON
- **Préfixe** : `/api/v1/`
- **Authentification** : Bearer Token (JWT)

## Nommage des endpoints

### Règles

- Utiliser des **noms** (pas des verbes) : `/budgets`, pas `/getBudgets`
- Utiliser le **pluriel** : `/budgets`, pas `/budget`
- Utiliser le **kebab-case** pour les noms composés : `/budget-categories`
- Hiérarchie via l'imbrication : `/users/{id}/budgets`
- Maximum **2 niveaux** d'imbrication

### Exemples

```
GET     /api/v1/budgets                    # Liste des budgets
GET     /api/v1/budgets/:id                # Détail d'un budget
POST    /api/v1/budgets                    # Créer un budget
PUT     /api/v1/budgets/:id                # Mettre à jour un budget
DELETE  /api/v1/budgets/:id                # Supprimer un budget

GET     /api/v1/budgets/:id/transactions   # Transactions d'un budget
POST    /api/v1/budgets/:id/transactions   # Ajouter une transaction à un budget

GET     /api/v1/users/me                   # Profil de l'utilisateur connecté
PUT     /api/v1/users/me                   # Modifier son profil
```

## Méthodes HTTP

| Méthode  | Usage                                | Idempotent | Body |
| -------- | ------------------------------------ | ---------- | ---- |
| `GET`    | Récupérer une ressource              | Oui        | Non  |
| `POST`   | Créer une ressource                  | Non        | Oui  |
| `PUT`    | Remplacer entièrement une ressource  | Oui        | Oui  |
| `PATCH`  | Modifier partiellement une ressource | Non        | Oui  |
| `DELETE` | Supprimer une ressource              | Oui        | Non  |

## Codes de statut HTTP

### Succès

| Code             | Usage                                  |
| ---------------- | -------------------------------------- |
| `200 OK`         | Requête réussie (GET, PUT, PATCH)      |
| `201 Created`    | Ressource créée (POST)                 |
| `204 No Content` | Succès sans contenu de retour (DELETE) |

### Erreurs client

| Code                       | Usage                                                   |
| -------------------------- | ------------------------------------------------------- |
| `400 Bad Request`          | Données invalides, erreur de validation                 |
| `401 Unauthorized`         | Non authentifié (token manquant ou expiré)              |
| `403 Forbidden`            | Authentifié mais pas autorisé                           |
| `404 Not Found`            | Ressource inexistante                                   |
| `409 Conflict`             | Conflit (doublon, état incohérent)                      |
| `422 Unprocessable Entity` | Données valides syntaxiquement mais incohérentes métier |

### Erreurs serveur

| Code                        | Usage                          |
| --------------------------- | ------------------------------ |
| `500 Internal Server Error` | Erreur inattendue côté serveur |

## Format de réponse

### Réponse simple (une ressource)

```json
{
  "id": 1,
  "name": "Courses",
  "amount": 30000,
  "status": "active",
  "createdAt": "2026-01-15T10:30:00Z"
}
```

### Réponse liste (avec pagination)

```json
{
  "content": [
    {
      "id": 1,
      "name": "Courses",
      "amount": 30000
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 45,
  "totalPages": 3
}
```

### Réponse d'erreur

```json
{
  "status": 400,
  "message": "Erreurs de validation",
  "errors": ["Le nom est obligatoire", "Le montant doit être positif"],
  "timestamp": "2026-01-15T10:30:00Z"
}
```

### Règles de format

- **Propriétés JSON** : camelCase (`createdAt`, `userId`)
- **Dates** : format ISO 8601 (`2026-01-15T10:30:00Z`)
- **Montants** : en **centimes** (integer), jamais en float (`30000` = 300,00 €)
- **IDs** : numériques (Long)
- **Booléens** : `true` / `false` (pas de `0` / `1`)
- **Nulls** : omettre les champs null plutôt que de retourner `"field": null`

## Pagination

Utiliser les query params Spring Data :

```
GET /api/v1/budgets?page=0&size=20&sort=createdAt,desc
```

| Paramètre | Description                | Défaut           |
| --------- | -------------------------- | ---------------- |
| `page`    | Numéro de page (0-indexed) | `0`              |
| `size`    | Nombre d'éléments par page | `20`             |
| `sort`    | Champ et direction de tri  | `createdAt,desc` |

## Filtrage et recherche

Utiliser les query params pour les filtres :

```
GET /api/v1/budgets?status=active
GET /api/v1/transactions?from=2026-01-01&to=2026-01-31
GET /api/v1/budgets?search=courses
```

## Versioning

- Le versioning se fait dans l'URL : `/api/v1/`, `/api/v2/`
- On ne crée une nouvelle version que pour les **breaking changes**
- On maintient l'ancienne version pendant une période de transition

## CORS

Configuration explicite côté backend :

- Origines autorisées : uniquement le frontend (`http://localhost:3000` en dev)
- Méthodes autorisées : `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`
- Headers autorisés : `Authorization`, `Content-Type`
- Credentials : autorisés (pour les cookies si nécessaire)

## Rate limiting

À implémenter si l'application est exposée publiquement :

- Limiter les requêtes par utilisateur/IP
- Retourner `429 Too Many Requests` avec un header `Retry-After`
