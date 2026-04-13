# Conventions Base de données

## Stack

- **SGBD** : PostgreSQL
- **ORM** : Spring Data JPA / Hibernate
- **Migrations** : Flyway (recommandé)

## Nommage

### Tables

- **snake_case** et **singulier**
- Nom descriptif en anglais

```sql
-- Bon
user
operation
budget
account
recurrence_rule
monthly_budget
budget_member

-- Mauvais
Users
operations
budgetCategory
tbl_transaction
group
```

### Colonnes

- **snake_case**
- Préfixer les clés étrangères avec le nom de la table référencée au singulier

```sql
-- Bon
id
user_id
budget_id
created_at
updated_at
first_name
total_amount

-- Mauvais
userId
BudgetID
createdAt
firstName
```

### Colonnes standard

Chaque table doit inclure (sauf tables de jointure) :

| Colonne      | Type               | Description                                |
| ------------ | ------------------ | ------------------------------------------ |
| `id`         | `UUID` ou `SERIAL` | Clé primaire (voir section suivante)       |
| `created_at` | `TIMESTAMP`        | Date de création (NOT NULL, DEFAULT NOW()) |
| `updated_at` | `TIMESTAMP`        | Date de dernière modification              |

### Clés primaires

- Nom : `id` (pas `user_id` dans la table `user`)

Le projet utilise **deux stratégies** de clé primaire selon le type de table :

| Stratégie         | Type PostgreSQL                  | Type Java     | Usage                                                                                  |
| ----------------- | -------------------------------- | ------------- | -------------------------------------------------------------------------------------- |
| **UUID**          | `UUID DEFAULT gen_random_uuid()` | `UUID`        | Entités métier principales (`user`, `operation`, `account`, `budget`, `monthly_budget`) |
| **SERIAL**        | `SERIAL PRIMARY KEY`             | `Integer`     | Tables de référence/lookup (`subscription`, `category`, `recurrence_rule`)             |
| **Clé composite** | Combinaison de FK                | Clé composite | Tables de jointure (`user_group`)                                                      |

#### Pourquoi cette distinction ?

- **UUID** pour les entités métier : non devinables (sécurité), pas de collision, adaptés aux systèmes distribués
- **SERIAL** pour les tables de référence : plus légers, suffisants pour des données rarement créées et peu sensibles

#### JPA

```java
// Entité métier (UUID)
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;

// Table de référence (SERIAL)
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
```

### Clés étrangères

Convention de nommage : `fk_<table_source>_<table_cible>`

```sql
-- Constraint naming
CONSTRAINT fk_operation_user FOREIGN KEY (user_id) REFERENCES "user"(id)
CONSTRAINT fk_operation_monthly_budget FOREIGN KEY (monthly_budget_id) REFERENCES monthly_budget(id)
CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES "user"(id)
CONSTRAINT fk_monthly_budget_budget FOREIGN KEY (budget_id) REFERENCES budget(id)
CONSTRAINT fk_budget_member_user FOREIGN KEY (user_id) REFERENCES "user"(id)
CONSTRAINT fk_budget_member_budget FOREIGN KEY (budget_id) REFERENCES budget(id)
```

> **Note** : `user` est un mot réservé en PostgreSQL. L'entourer de guillemets doubles (`"user"`) dans les requêtes SQL.

### Index

Convention de nommage : `idx_<table>_<colonne(s)>`

```sql
CREATE INDEX idx_operation_user_id ON operation(user_id);
CREATE INDEX idx_operation_monthly_budget_id ON operation(monthly_budget_id);
CREATE INDEX idx_operation_budget_id ON operation(budget_id);
CREATE INDEX idx_account_user_id ON account(user_id);
CREATE INDEX idx_monthly_budget_budget_id ON monthly_budget(budget_id);
CREATE INDEX idx_budget_member_user_id ON budget_member(user_id);
CREATE UNIQUE INDEX idx_user_email ON "user"(email);
CREATE UNIQUE INDEX idx_budget_invite_code ON budget(invite_code) WHERE invite_code IS NOT NULL;
```

### Contraintes

Convention de nommage :

| Type        | Format                     | Exemple                 |
| ----------- | -------------------------- | ----------------------- |
| Primary key | `pk_<table>`               | `pk_operation`          |
| Foreign key | `fk_<source>_<cible>`      | `fk_operation_user`     |
| Unique      | `uq_<table>_<colonne>`     | `uq_user_email`         |
| Check       | `ck_<table>_<description>` | `ck_account_type_valid` |

## Types de données

| Donnée              | Type PostgreSQL                  | Type Java                                    |
| ------------------- | -------------------------------- | -------------------------------------------- |
| ID métier           | `UUID DEFAULT gen_random_uuid()` | `UUID`                                       |
| ID référence        | `SERIAL`                         | `Integer`                                    |
| Texte court (< 255) | `VARCHAR(n)`                     | `String`                                     |
| Texte long          | `TEXT`                           | `String`                                     |
| Montant             | `DECIMAL(12, 2)`                 | `BigDecimal`                                 |
| Entier              | `INTEGER`                        | `Integer`                                    |
| Booléen             | `BOOLEAN`                        | `Boolean`                                    |
| Date                | `DATE`                           | `LocalDate`                                  |
| Date + heure        | `TIMESTAMP`                      | `LocalDateTime`                              |
| Enum                | `VARCHAR`                        | `Enum` (avec `@Enumerated(EnumType.STRING)`) |

### Montants

Les montants sont stockés en `DECIMAL(12, 2)` conformément au MPD du projet. Ce type garantit une précision exacte à 2 décimales, adapté aux calculs financiers.

```sql
amount DECIMAL(12, 2) NOT NULL
initial_balance DECIMAL(12, 2) NOT NULL DEFAULT 0
```

## Migrations (Flyway)

### Structure des fichiers

```
backend/src/main/resources/db/migration/
├── V1__create_subscription_table.sql
├── V2__create_user_table.sql
├── V3__create_account_table.sql
├── V4__create_budget_table.sql
├── V5__create_budget_member_table.sql
├── V6__create_category_table.sql
├── V7__create_recurrence_rule_table.sql
├── V8__create_monthly_budget_table.sql
└── V9__create_operation_table.sql
```

### Nommage des fichiers

Format : `V<version>__<description_snake_case>.sql`

- Le préfixe `V` suivi du numéro de version (incrémental)
- Deux underscores `__` séparent la version de la description
- Description en **snake_case**
- Extension `.sql`

### Règles

- Une migration = **un changement logique**
- Les migrations sont **immuables** : ne jamais modifier une migration déjà appliquée
- Toujours tester les migrations sur une BDD vierge
- Inclure les rollbacks dans les commentaires si possible

### Exemple de migration

```sql
-- V2__create_user_table.sql
CREATE TABLE "user" (
    id                UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    email             VARCHAR(255)    NOT NULL,
    password          VARCHAR(255)    NOT NULL,
    name              VARCHAR(100)    NOT NULL,
    email_verified    BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP       NOT NULL DEFAULT NOW(),
    subscription_id   INTEGER         NOT NULL REFERENCES subscription(id),

    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE INDEX idx_user_email ON "user"(email);

-- V4__create_budget_table.sql
CREATE TABLE budget (
    id            UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    name          VARCHAR(100)    NOT NULL,
    type          VARCHAR(10)     NOT NULL CHECK (type IN ('solo', 'shared')),
    invite_code   VARCHAR(20)     UNIQUE,
    created_at    TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP       NOT NULL DEFAULT NOW()
);
-- Le propriétaire est identifié via budget_member WHERE role = 'owner'

-- V5__create_budget_member_table.sql
CREATE TABLE budget_member (
    user_id             UUID            NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    budget_id           UUID            NOT NULL REFERENCES budget(id) ON DELETE CASCADE,
    role                VARCHAR(10)     NOT NULL CHECK (role IN ('owner', 'member')),
    contribution_rate   DECIMAL(5, 2)   NOT NULL DEFAULT 50.00
        CHECK (contribution_rate BETWEEN 0 AND 100),
    joined_at           TIMESTAMP       NOT NULL DEFAULT NOW(),

    PRIMARY KEY (user_id, budget_id)
);
```

## Relations

### Convention JPA

| Relation     | Annotation                                             | Fetch par défaut |
| ------------ | ------------------------------------------------------ | ---------------- |
| Many-to-One  | `@ManyToOne(fetch = FetchType.LAZY)`                   | LAZY             |
| One-to-Many  | `@OneToMany(mappedBy = "...", fetch = FetchType.LAZY)` | LAZY             |
| Many-to-Many | `@ManyToMany`                                          | LAZY             |
| One-to-One   | `@OneToOne(fetch = FetchType.LAZY)`                    | LAZY             |

### Règles

- **Toujours LAZY** par défaut pour éviter les problèmes de performance (N+1)
- Utiliser `JOIN FETCH` dans les requêtes JPQL quand on a besoin des relations
- Le côté propriétaire (owning side) contient le `@JoinColumn`
- Cascade : utiliser avec précaution, principalement `CascadeType.ALL` pour les relations parent-enfant fortes

## Bonnes pratiques

- Ne jamais stocker de données dérivées calculables (ex : total d'un budget = somme des transactions)
- Utiliser des **soft deletes** si l'historique est important (colonne `deleted_at`)
- Toujours indexer les colonnes utilisées dans les `WHERE` et `JOIN`
- Limiter la taille des `VARCHAR` de manière réaliste
- Utiliser `TEXT` uniquement quand la taille est vraiment imprévisible
