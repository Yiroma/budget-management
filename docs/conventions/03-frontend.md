# Conventions Frontend

## Stack technique

- **Framework** : Next.js 16 (App Router)
- **UI** : React 19
- **Langage** : TypeScript (strict mode)
- **Styling** : Tailwind CSS 4
- **Linting** : ESLint avec eslint-config-next

## Structure des dossiers

On adopte une approche **hybride** : feature-based pour la logique métier, type-based pour les éléments partagés.

```
frontend/src/
├── app/                        # App Router Next.js (pages et layouts)
│   ├── (auth)/                 # Route group pour les pages authentification
│   │   ├── login/
│   │   │   └── page.tsx
│   │   └── register/
│   │       └── page.tsx
│   ├── dashboard/
│   │   ├── page.tsx
│   │   ├── layout.tsx
│   │   └── loading.tsx
│   ├── budgets/
│   │   ├── page.tsx
│   │   ├── [id]/
│   │   │   └── page.tsx
│   │   └── loading.tsx
│   ├── layout.tsx              # Root layout
│   ├── page.tsx                # Page d'accueil
│   ├── globals.css
│   ├── not-found.tsx
│   └── error.tsx
│
├── components/                 # Composants partagés
│   ├── ui/                     # Composants UI génériques (Button, Input, Modal)
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   └── Modal.tsx
│   ├── layout/                 # Composants de mise en page (Header, Sidebar, Footer)
│   │   ├── Header.tsx
│   │   ├── Sidebar.tsx
│   │   └── Footer.tsx
│   └── forms/                  # Composants de formulaire réutilisables
│       └── FormField.tsx
│
├── features/                   # Modules métier (feature-based)
│   ├── budget/
│   │   ├── components/         # Composants spécifiques au budget
│   │   │   ├── BudgetCard.tsx
│   │   │   └── BudgetForm.tsx
│   │   ├── hooks/              # Hooks spécifiques au budget
│   │   │   └── useBudget.ts
│   │   ├── services/           # Appels API spécifiques au budget
│   │   │   └── budgetService.ts
│   │   ├── types/              # Types spécifiques au budget
│   │   │   └── budget.types.ts
│   │   └── utils/              # Utilitaires spécifiques au budget
│   │       └── budgetCalculations.ts
│   ├── transaction/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── services/
│   │   └── types/
│   └── auth/
│       ├── components/
│       ├── hooks/
│       ├── services/
│       └── types/
│
├── hooks/                      # Hooks partagés
│   ├── useDebounce.ts
│   └── useLocalStorage.ts
│
├── lib/                        # Librairies et configurations
│   ├── api.ts                  # Client API (fetch wrapper)
│   └── utils.ts                # Utilitaires généraux
│
├── types/                      # Types partagés globaux
│   └── global.types.ts
│
└── constants/                  # Constantes partagées
    └── app.constants.ts
```

### Règles de structure

- **`app/`** : uniquement les fichiers de routing Next.js (`page.tsx`, `layout.tsx`, `loading.tsx`, `error.tsx`, `not-found.tsx`). Pas de logique métier.
- **`components/`** : composants réutilisables dans **plusieurs features**. Si un composant n'est utilisé que dans une feature, il va dans `features/<feature>/components/`.
- **`features/`** : chaque feature contient ses propres composants, hooks, services, types et utilitaires.
- **`hooks/`** : hooks réutilisables dans **plusieurs features**.
- **`lib/`** : configurations et wrappers de librairies.
- **`types/`** : types partagés entre plusieurs features.

## TypeScript

### Strict mode

Le `tsconfig.json` est en mode strict. Les règles suivantes s'appliquent :

- **`any` interdit** : utiliser `unknown` si le type est incertain
- **`strictNullChecks` activé** : gérer explicitement `null` et `undefined`

### `type` vs `interface`

| Utiliser `interface`                       | Utiliser `type`                                   |
| ------------------------------------------ | ------------------------------------------------- |
| Pour les objets et les props de composants | Pour les unions, intersections, types utilitaires |
| Quand l'extension (extends) est nécessaire | Pour les alias de types primitifs                 |

```typescript
// Interface pour les props de composants et objets
interface BudgetCardProps {
  budget: Budget;
  onEdit: (id: string) => void;
}

// Type pour les unions et alias
type BudgetStatus = "active" | "archived" | "closed";
type BudgetWithTransactions = Budget & { transactions: Transaction[] };
```

### Nommage des types

- **Props** : suffixe `Props` → `BudgetCardProps`
- **État** : suffixe `State` → `AuthState`
- **Réponse API** : suffixe `Response` → `BudgetListResponse`
- **Requête API** : suffixe `Request` → `CreateBudgetRequest`
- Pas de préfixe `I` pour les interfaces

### Enums vs Union types

Préférer les **union types** aux enums :

```typescript
// Préféré
type BudgetStatus = "active" | "archived" | "closed";

// Éviter
enum BudgetStatus {
  ACTIVE = "active",
  ARCHIVED = "archived",
  CLOSED = "closed",
}
```

Les union types sont mieux tree-shakés et plus idiomatiques en TypeScript.

## React / Next.js

### Composants

- **Fonctionnels uniquement** : pas de class components
- **Nommage** : PascalCase, nom descriptif
- **Export** : named export (pas de `export default` sauf pour les pages Next.js)

```typescript
// Bon
export function BudgetCard({ budget, onEdit }: BudgetCardProps) {
  return (/* ... */);
}

// Pages Next.js : export default obligatoire
export default function BudgetPage() {
  return (/* ... */);
}
```

### Server Components vs Client Components

- **Par défaut** : tout est Server Component (pas de `'use client'`)
- **Ajouter `'use client'`** uniquement quand nécessaire :
  - Utilisation de hooks React (`useState`, `useEffect`, etc.)
  - Event handlers (`onClick`, `onChange`, etc.)
  - APIs navigateur (`window`, `localStorage`, etc.)
- **Règle** : pousser le `'use client'` le plus bas possible dans l'arbre de composants

```typescript
// Server Component (défaut) - pas de directive
export function BudgetList({ budgets }: BudgetListProps) {
  return (
    <div>
      {budgets.map((budget) => (
        <BudgetCard key={budget.id} budget={budget} />
      ))}
    </div>
  );
}

// Client Component - directive nécessaire
'use client';

import { useState } from 'react';

export function BudgetForm({ onSubmit }: BudgetFormProps) {
  const [name, setName] = useState('');
  // ...
}
```

### Props

- Toujours **destructurer** dans la signature de la fonction
- Définir les props dans une **interface dédiée**

```typescript
// Bon
interface BudgetCardProps {
  budget: Budget;
  isEditable?: boolean;
  onDelete: (id: string) => void;
}

export function BudgetCard({ budget, isEditable = false, onDelete }: BudgetCardProps) {
  // ...
}

// Mauvais
export function BudgetCard(props: any) {
  // ...
}
```

### Hooks

- Respecter les [règles des hooks](https://react.dev/reference/rules/rules-of-hooks)
- Les hooks custom commencent toujours par `use`
- Un hook = **une responsabilité**

```typescript
// Bon : une responsabilité claire
function useBudgetForm(initialBudget?: Budget) {
  const [formData, setFormData] = useState(initialBudget ?? defaultBudget);
  const [errors, setErrors] = useState<FormErrors>({});

  const validate = () => {
    /* ... */
  };
  const handleSubmit = () => {
    /* ... */
  };

  return { formData, errors, setFormData, handleSubmit };
}
```

### Data fetching

- **Server Components** : fetch directement dans le composant (recommandé par Next.js)
- **Client Components** : utiliser un hook dédié ou une librairie (React Query, SWR)
- **Mutations** : Server Actions ou appels API via le service de la feature

### Error handling

- Utiliser les fichiers `error.tsx` de Next.js pour les error boundaries par route
- `not-found.tsx` pour les pages 404
- `loading.tsx` ou `<Suspense>` pour les états de chargement

## Tailwind CSS

### Règles d'utilisation

- Utiliser les classes utilitaires de Tailwind en priorité
- Extraire un composant React plutôt qu'une classe CSS quand un pattern se répète
- Utiliser `globals.css` uniquement pour les variables CSS custom et les styles de base
- Ordre des classes : layout → spacing → sizing → typography → visual → interactivity

```tsx
// Bon : classes ordonnées logiquement
<div className="flex items-center gap-4 p-4 w-full text-sm bg-white rounded-lg hover:shadow-md">

// Éviter : créer des classes CSS custom pour ce que Tailwind peut faire
```
