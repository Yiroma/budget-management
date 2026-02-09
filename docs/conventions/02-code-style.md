# Conventions de Code Style

## EditorConfig

Le fichier `.editorconfig` à la racine garantit la cohérence entre les éditeurs et les contributeurs.

```ini
# .editorconfig
root = true

[*]
charset = utf-8
end_of_line = lf
indent_style = spaces
indent_size = 2
insert_final_newline = true
trim_trailing_whitespace = true

[*.java]
indent_size = 4

[*.md]
trim_trailing_whitespace = false
```

## Prettier (Frontend)

Configuration recommandée dans `frontend/.prettierrc` :

```json
{
  "semi": true,
  "singleQuote": true,
  "trailingComma": "all",
  "printWidth": 80,
  "tabWidth": 2,
  "useTabs": false,
  "bracketSpacing": true,
  "arrowParens": "always",
  "endOfLine": "lf"
}
```

### Règles clés

- **Semicolons** : toujours
- **Quotes** : simples (single quotes) en JS/TS, doubles en JSX (automatique)
- **Trailing comma** : toujours (meilleurs diffs git)
- **Print width** : 80 caractères

## ESLint (Frontend)

La configuration ESLint étend `eslint-config-next` avec les plugins suivants :

### Plugins recommandés

| Plugin                      | Rôle                                          |
| --------------------------- | --------------------------------------------- |
| `eslint-config-next`        | Règles Next.js (core-web-vitals + typescript) |
| `eslint-plugin-jsx-a11y`    | Accessibilité des composants JSX              |
| `eslint-plugin-import`      | Ordre et validation des imports               |
| `eslint-plugin-react-hooks` | Règles des hooks React                        |

### Ordre des imports

Les imports doivent suivre cet ordre, séparés par une ligne vide :

```typescript
// 1. Modules natifs Node.js
import path from "path";

// 2. Modules externes (librairies)
import React from "react";
import { useRouter } from "next/navigation";

// 3. Modules internes (alias @/)
import { Button } from "@/components/ui/Button";
import { useAuth } from "@/hooks/useAuth";

// 4. Types (si import séparé)
import type { User } from "@/types/user";

// 5. Styles
import styles from "./Component.module.css";
```

## Checkstyle (Backend)

Pour le backend Java, suivre les conventions de formatage standard :

- **Indentation** : 4 espaces
- **Accolades** : style K&R (accolade ouvrante sur la même ligne)
- **Longueur de ligne** : 120 caractères max
- **Espaces** : après les mots-clés (`if`, `for`, `while`), autour des opérateurs

```java
// Bon
if (condition) {
    doSomething();
} else {
    doSomethingElse();
}

// Mauvais
if(condition){
    doSomething();
}
else{
    doSomethingElse();
}
```

## Nommage général

### Frontend (TypeScript / React)

| Élément                   | Convention                            | Exemple                               |
| ------------------------- | ------------------------------------- | ------------------------------------- |
| **Composants**            | PascalCase                            | `BudgetCard.tsx`, `UserProfile.tsx`   |
| **Hooks**                 | camelCase préfixé `use`               | `useAuth.ts`, `useBudget.ts`          |
| **Utilitaires / helpers** | camelCase                             | `formatCurrency.ts`, `dateUtils.ts`   |
| **Constantes**            | SCREAMING_SNAKE_CASE                  | `MAX_BUDGET_AMOUNT`, `API_BASE_URL`   |
| **Types / Interfaces**    | PascalCase                            | `User`, `BudgetFormProps`             |
| **Fichiers de config**    | kebab-case                            | `eslint.config.mjs`, `next.config.ts` |
| **Dossiers**              | kebab-case                            | `budget-form/`, `ui/`, `auth/`        |
| **Variables**             | camelCase                             | `userName`, `totalAmount`             |
| **Fonctions**             | camelCase                             | `calculateTotal()`, `formatDate()`    |
| **Booléens**              | camelCase préfixé `is/has/can/should` | `isLoading`, `hasError`, `canEdit`    |
| **Handlers**              | camelCase préfixé `handle`            | `handleSubmit`, `handleDelete`        |
| **Props de callback**     | camelCase préfixé `on`                | `onClick`, `onBudgetCreate`           |

### Backend (Java / Spring Boot)

| Élément        | Convention                                          | Exemple                                |
| -------------- | --------------------------------------------------- | -------------------------------------- |
| **Classes**    | PascalCase                                          | `BudgetService`, `UserController`      |
| **Interfaces** | PascalCase                                          | `BudgetRepository`, `AuthService`      |
| **Méthodes**   | camelCase                                           | `findById()`, `createBudget()`         |
| **Variables**  | camelCase                                           | `userName`, `budgetAmount`             |
| **Constantes** | SCREAMING_SNAKE_CASE                                | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE` |
| **Packages**   | tout en minuscules                                  | `yiroma.budgetmanagement.service`      |
| **Enums**      | PascalCase (classe), SCREAMING_SNAKE_CASE (valeurs) | `BudgetStatus.ACTIVE`                  |
| **DTOs**       | PascalCase suffixé `Dto`                            | `BudgetDto`, `CreateUserDto`           |
| **Exceptions** | PascalCase suffixé `Exception`                      | `BudgetNotFoundException`              |

### Suffixes par type de fichier

#### Frontend

| Type             | Suffixe                  | Exemple                 |
| ---------------- | ------------------------ | ----------------------- |
| Composant        | `.tsx`                   | `BudgetCard.tsx`        |
| Hook             | `.ts`                    | `useAuth.ts`            |
| Type / Interface | `.types.ts`              | `budget.types.ts`       |
| Utilitaire       | `.ts`                    | `formatCurrency.ts`     |
| Constantes       | `.constants.ts`          | `budget.constants.ts`   |
| Test             | `.test.ts` / `.test.tsx` | `BudgetCard.test.tsx`   |
| Style module     | `.module.css`            | `BudgetCard.module.css` |

#### Backend

| Type           | Suffixe                  | Exemple                        |
| -------------- | ------------------------ | ------------------------------ |
| Controller     | `Controller.java`        | `BudgetController.java`        |
| Service        | `Service.java`           | `BudgetService.java`           |
| Repository     | `Repository.java`        | `BudgetRepository.java`        |
| Entity / Model | `.java` (pas de suffixe) | `Budget.java`, `User.java`     |
| DTO            | `Dto.java`               | `BudgetDto.java`               |
| Mapper         | `Mapper.java`            | `BudgetMapper.java`            |
| Exception      | `Exception.java`         | `BudgetNotFoundException.java` |
| Config         | `Config.java`            | `SecurityConfig.java`          |
| Test           | `Test.java`              | `BudgetServiceTest.java`       |

## Commentaires

### Quand commenter

- **Oui** : le "pourquoi" — décisions non évidentes, workarounds, contexte métier
- **Non** : le "quoi" — le code doit être suffisamment lisible par lui-même

```typescript
// Mauvais : décrit le quoi
// Incrémente le compteur de 1
counter++;

// Bon : explique le pourquoi
// On arrondit au centime supérieur pour éviter les erreurs d'arrondi bancaire
const amount = Math.ceil(rawAmount * 100) / 100;
```

```java
// Mauvais
// Getter pour le nom
public String getName() { return name; }

// Bon
// On exclut les transactions annulées du calcul car elles sont
// conservées en BDD pour l'audit mais ne doivent pas impacter le solde
List<Transaction> activeTransactions = transactions.stream()
    .filter(t -> t.getStatus() != TransactionStatus.CANCELLED)
    .toList();
```

### JSDoc / Javadoc

Documenter les **fonctions publiques et complexes** uniquement :

```typescript
/**
 * Calcule le solde restant d'un budget en soustrayant
 * toutes les transactions associées.
 *
 * @param budget - Le budget à évaluer
 * @param transactions - Les transactions associées au budget
 * @returns Le montant restant en centimes
 */
function calculateRemainingBalance(budget: Budget, transactions: Transaction[]): number {
  // ...
}
```

```java
/**
 * Calcule le solde restant d'un budget.
 * Seules les transactions avec le statut COMPLETED sont prises en compte.
 *
 * @param budgetId l'identifiant du budget
 * @return le solde restant en centimes
 * @throws BudgetNotFoundException si le budget n'existe pas
 */
public long calculateRemainingBalance(Long budgetId) {
    // ...
}
```
