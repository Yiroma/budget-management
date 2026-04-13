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
# Auth
POST    /api/v1/auth/register              # Inscription (+ choix abonnement)
POST    /api/v1/auth/login                 # Connexion
POST    /api/v1/auth/logout                # Déconnexion
GET     /api/v1/auth/verify-email          # Confirmation email

# Profil utilisateur
GET     /api/v1/users/me                   # Profil de l'utilisateur connecté
PUT     /api/v1/users/me                   # Modifier son profil
DELETE  /api/v1/users/me                   # Supprimer son compte
PATCH   /api/v1/users/me/subscription      # Changer d'abonnement

# Comptes bancaires
GET     /api/v1/accounts                   # Liste des comptes de l'utilisateur
POST    /api/v1/accounts                   # Créer un compte
GET     /api/v1/accounts/:id               # Détail d'un compte
PUT     /api/v1/accounts/:id               # Modifier un compte
DELETE  /api/v1/accounts/:id               # Supprimer un compte
GET     /api/v1/accounts/:id/balance       # Solde restant (argent de poche)

# Budgets
GET     /api/v1/budgets                    # Liste des budgets de l'utilisateur
POST    /api/v1/budgets                    # Créer un budget (solo ou partagé)
GET     /api/v1/budgets/:id                # Détail d'un budget
PUT     /api/v1/budgets/:id                # Modifier un budget
DELETE  /api/v1/budgets/:id                # Supprimer un budget
POST    /api/v1/budgets/join               # Rejoindre un budget partagé (via invite_code)

# Membres d'un budget partagé
GET     /api/v1/budgets/:id/members        # Liste des membres
POST    /api/v1/budgets/:id/members        # Inviter un membre (par email)
PATCH   /api/v1/budgets/:id/members/:uid   # Modifier le taux de contribution
DELETE  /api/v1/budgets/:id/members/:uid   # Retirer un membre
GET     /api/v1/budgets/:id/contribution   # Calcul contribution par membre

# Opérations
GET     /api/v1/accounts/:id/operations    # Opérations d'un compte
POST    /api/v1/accounts/:id/operations    # Ajouter une opération à un compte
GET     /api/v1/budgets/:id/operations     # Opérations d'un budget
POST    /api/v1/budgets/:id/operations     # Ajouter une opération à un budget
PUT     /api/v1/operations/:id             # Modifier une opération
DELETE  /api/v1/operations/:id             # Supprimer une opération
POST    /api/v1/operations/:id/recurrence  # Attacher une règle de récurrence

# Budgets mensuels
GET     /api/v1/budgets/:id/monthly        # Liste des budgets mensuels
GET     /api/v1/budgets/:id/monthly/:year/:month  # Budget mensuel d'un mois
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
