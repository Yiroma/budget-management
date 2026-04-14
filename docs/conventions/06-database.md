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

## Gestion du schéma (Hibernate)

Le schéma de base de données est entièrement géré par **Hibernate** via `spring.jpa.hibernate.ddl-auto`.

- **Pas de Flyway**, pas de migrations SQL manuelles
- Le schéma est généré et mis à jour automatiquement à partir des entités JPA
- Configuration dans `application.properties` :

```properties
spring.jpa.hibernate.ddl-auto=update
```

> `update` : Hibernate applique les différences entre les entités et le schéma existant sans supprimer de données. Utiliser `create` uniquement sur une BDD vierge de développement.

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
