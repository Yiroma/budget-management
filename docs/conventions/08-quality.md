# Conventions de Qualité

## Accessibilité (a11y)

L'application doit respecter les critères **WCAG 2.1 niveau AA** minimum.

### HTML sémantique

Utiliser les éléments HTML appropriés plutôt que des `<div>` génériques :

| Élément     | Usage                                           |
| ----------- | ----------------------------------------------- |
| `<main>`    | Contenu principal de la page (un seul par page) |
| `<nav>`     | Navigation                                      |
| `<header>`  | En-tête de page ou de section                   |
| `<footer>`  | Pied de page ou de section                      |
| `<section>` | Section thématique avec un titre                |
| `<article>` | Contenu autonome (carte de budget, transaction) |
| `<aside>`   | Contenu complémentaire (sidebar)                |
| `<button>`  | Action interactive (jamais un `<div onClick>`)  |
| `<a>`       | Navigation vers une autre page/URL              |

```tsx
// Bon
<button onClick={handleDelete} type="button">Supprimer</button>
<a href="/budgets">Voir les budgets</a>

// Mauvais
<div onClick={handleDelete} role="button">Supprimer</div>
<span onClick={() => navigate('/budgets')}>Voir les budgets</span>
```

### Images et médias

- **Toujours** un attribut `alt` sur les `<img>`
- `alt=""` pour les images purement décoratives
- Descriptions concises et informatives

```tsx
// Informatif
<Image src={chart} alt="Graphique des dépenses mensuelles de janvier 2026" />

// Décoratif
<Image src={decorativeLine} alt="" />

// Mauvais
<Image src={chart} />
<Image src={chart} alt="image" />
```

### Formulaires

- Chaque input a un `<label>` associé (via `htmlFor` / `id`)
- Messages d'erreur liés à l'input via `aria-describedby`
- Indication visuelle et textuelle des champs requis

```tsx
<div>
  <label htmlFor="budget-name">
    Nom du budget <span aria-hidden="true">*</span>
  </label>
  <input
    id="budget-name"
    type="text"
    required
    aria-required="true"
    aria-invalid={!!errors.name}
    aria-describedby={errors.name ? "budget-name-error" : undefined}
  />
  {errors.name && (
    <p id="budget-name-error" role="alert">
      {errors.name}
    </p>
  )}
</div>
```

### Navigation clavier

- Tous les éléments interactifs sont accessibles au clavier (Tab, Enter, Escape)
- Ordre de tabulation logique (ne pas manipuler `tabIndex` sauf `tabIndex={-1}` pour le focus programmatique)
- Indicateur de focus visible (ne jamais supprimer `outline` sans alternative)
- Modales : piéger le focus à l'intérieur, fermer avec Escape

### Contraste

- **Texte normal** : ratio minimum 4.5:1
- **Grand texte** (> 18px bold ou > 24px) : ratio minimum 3:1
- **Éléments UI** (bordures, icônes) : ratio minimum 3:1

### ARIA

- **Règle d'or** : ne pas utiliser ARIA si un élément HTML natif fait le travail
- `aria-label` : pour les éléments sans texte visible (boutons icônes)
- `aria-live="polite"` : pour les mises à jour dynamiques (notifications, totaux recalculés)
- `role="alert"` : pour les messages d'erreur

```tsx
// Bouton avec icône uniquement
<button aria-label="Supprimer le budget Courses" onClick={handleDelete}>
  <TrashIcon aria-hidden="true" />
</button>

// Zone mise à jour dynamiquement
<p aria-live="polite">Solde restant : {formattedBalance}</p>
```

### ESLint a11y

Installer et configurer `eslint-plugin-jsx-a11y` dans la configuration ESLint pour détecter automatiquement les problèmes d'accessibilité courants.

## Sécurité

### Variables d'environnement

- **`.env`** : contient les secrets, **jamais versionné**
- **`.env.sample`** : template avec les clés sans les valeurs, **versionné**
- Accès côté serveur uniquement (pas de `NEXT_PUBLIC_` pour les secrets)

```bash
# .env.example (versionné)
DB_NAME=
DB_USER=
DB_PASSWORD=
DB_PORT=5432
JWT_SECRET=
```

### Validation des inputs

La validation se fait à **deux niveaux** :

| Niveau       | Rôle                        | Outil                             |
| ------------ | --------------------------- | --------------------------------- |
| **Frontend** | UX (feedback immédiat)      | Validation HTML5 + logique custom |
| **Backend**  | Sécurité (source de vérité) | Jakarta Validation (`@Valid`)     |

Le frontend peut être contourné — le backend est la **seule garantie**.

### Authentification

- **JWT** (JSON Web Tokens) via Spring Security
- Tokens stockés de manière sécurisée (httpOnly cookies préféré, pas de localStorage)
- Access token : courte durée (15-30 min)
- Refresh token : longue durée (7 jours)
- Rotation des refresh tokens à chaque utilisation

### Protection contre les attaques courantes

| Attaque               | Protection                                                             |
| --------------------- | ---------------------------------------------------------------------- |
| **XSS**               | React échappe par défaut, ne jamais utiliser `dangerouslySetInnerHTML` |
| **CSRF**              | Tokens CSRF ou architecture stateless (JWT)                            |
| **SQL Injection**     | JPA/Hibernate (requêtes paramétrées), jamais de concaténation SQL      |
| **Données sensibles** | Ne jamais retourner le mot de passe dans les DTOs                      |

### CORS

Configuration explicite, jamais `*` en production :

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // Frontend uniquement
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }
}
```

## Performance

### Frontend

#### Images

- Utiliser le composant `<Image>` de Next.js (optimisation automatique)
- Formats modernes : WebP, AVIF
- Lazy loading par défaut (comportement de Next.js `<Image>`)
- Tailles responsives via `sizes` et `srcSet`

#### Bundle

- **Dynamic imports** pour le code-splitting des composants lourds
- Vérifier la taille du bundle régulièrement (`next build` affiche les tailles)
- Éviter d'importer des librairies entières quand un sous-module suffit

```typescript
// Bon : import ciblé
import { format } from "date-fns/format";

// Mauvais : import de toute la librairie
import { format } from "date-fns";
```

#### Mémoisation

Utiliser `useMemo` et `useCallback` **uniquement quand c'est justifié** :

- Calculs coûteux
- Props de référence passées à des composants mémoïsés (`React.memo`)
- Dépendances de `useEffect`

Ne pas mémoïser par défaut — React est performant sans.

#### Rendering

- Préférer les **Server Components** pour le contenu statique ou peu interactif
- Utiliser `loading.tsx` et `<Suspense>` pour le streaming
- Éviter les re-renders inutiles en structurant bien l'état

### Backend

#### Requêtes BDD

- Relations JPA en `FetchType.LAZY` par défaut
- `JOIN FETCH` dans les requêtes JPQL quand les données liées sont nécessaires
- Pagination systématique pour les listes (`Pageable`)
- Index sur les colonnes filtrées et triées

#### Caching

- Cache HTTP via les headers (`Cache-Control`, `ETag`)
- Cache applicatif avec `@Cacheable` de Spring pour les données peu volatiles

## Principes généraux

### DRY — Don't Repeat Yourself

- Extraire la logique dupliquée dans des fonctions/méthodes utilitaires
- Composants UI réutilisables plutôt que du copier-coller
- **Attention** : ne pas créer d'abstraction prématurée. Dupliquer 2 fois est acceptable, extraire à partir de 3

### SOLID (principalement backend)

| Principe                      | Application                                                                     |
| ----------------------------- | ------------------------------------------------------------------------------- |
| **S** — Single Responsibility | Un service = une responsabilité (BudgetService gère les budgets, pas les users) |
| **O** — Open/Closed           | Étendre via des nouvelles classes plutôt que modifier l'existant                |
| **L** — Liskov Substitution   | Les sous-classes doivent être substituables à leur classe parente               |
| **I** — Interface Segregation | Interfaces spécifiques plutôt qu'une interface générale                         |
| **D** — Dependency Inversion  | Dépendre des abstractions (interfaces), pas des implémentations                 |

### KISS — Keep It Simple, Stupid

- La solution la plus simple qui fonctionne est la meilleure
- Éviter l'over-engineering
- Code lisible > code "clever"
- Si un commentaire est nécessaire pour expliquer le "quoi", le code est trop complexe

### YAGNI — You Ain't Gonna Need It

- Ne pas implémenter de fonctionnalité "au cas où"
- Ne pas créer d'abstraction pour un seul cas d'usage
- Construire pour le besoin actuel, refactorer quand le besoin évolue

### Separation of Concerns

- **Frontend** : UI / logique / data bien séparés (composants / hooks / services)
- **Backend** : couches clairement séparées (controller / service / repository)
- **API** : contrat clair entre frontend et backend
- Un composant/classe ne fait qu'une chose et la fait bien

## CI/CD

### Pipeline recommandé

```
Push → Lint → Test → Build → Deploy
```

| Étape      | Frontend             | Backend                |
| ---------- | -------------------- | ---------------------- |
| **Lint**   | `eslint .`           | Checkstyle (optionnel) |
| **Format** | `prettier --check .` | —                      |
| **Test**   | `jest --coverage`    | `mvn test`             |
| **Build**  | `next build`         | `mvn package`          |
| **Deploy** | Vercel / Docker      | Docker                 |

### Stratégie de branches

```
feature branch → dev → main
```

1. **Feature branch** : développement d'une fonctionnalité (`feat/add-budget-form`)
2. **`dev`** : branche d'intégration — la feature branch est merge sur `dev` après les tests
3. **`main`** : branche de production — `dev` est merge sur `main` pour le déploiement

| Branche                 | Rôle                                  | Déploiement         |
| ----------------------- | ------------------------------------- | ------------------- |
| `feat/*`, `fix/*`, etc. | Développement isolé d'une feature/fix | Aucun               |
| `dev`                   | Intégration et tests                  | Staging (optionnel) |
| `main`                  | Production stable                     | Production          |

### Règles

- Jamais de push direct sur `main` ni sur `dev`
- Toute modification passe par une **PR** (feature branch → `dev`, puis `dev` → `main`)
- Les tests doivent passer **avant** le merge sur `dev`
- Le merge sur `main` déclenche le déploiement en production

### Docker

- Un `Dockerfile` par service (frontend, backend)
- `docker-compose.yml` pour l'environnement de développement local (app + PostgreSQL)
- Images multi-stage pour minimiser la taille
