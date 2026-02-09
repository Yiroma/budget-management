# Conventions de Tests

## Stratégie de test

### Pyramide des tests

```
          /  E2E \          ← Peu (parcours critiques uniquement)
         /--------\
        /Intégration\       ← Modéré (API, composants avec contexte)
       /------------\
      /  Unitaires   \      ← Beaucoup (logique métier, utilitaires)
     /----------------\
    /     statique     \    ← permanent (qualité et structure du code)
   /____________________\
```

| Type            | Quoi tester                                                | Outils                                  |
| --------------- | ---------------------------------------------------------- | --------------------------------------- |
| **statique**    | qualité du code                                            | Eslint, Prettier, Husky                 |
| **Unitaire**    | Fonctions pures, services, utilitaires, hooks              | Jest, JUnit 5                           |
| **Intégration** | API endpoints, composants avec providers, repository + BDD | Spring Boot Test, React Testing Library |
| **E2E**         | Parcours utilisateur complets                              | Playwright ou Cypress (si nécessaire)   |

## Tests statiques

Les tests statiques constituent la **base permanente** de la qualité du code. Ils s'exécutent automatiquement, sans écrire de tests manuels.

### Outils

| Outil                        | Rôle                                                               | Portée        |
| ---------------------------- | ------------------------------------------------------------------ | ------------- |
| **ESLint**                   | Détection de bugs, mauvaises pratiques, a11y                       | Frontend      |
| **Prettier**                 | Formatage cohérent du code                                         | Frontend      |
| **TypeScript** (strict mode) | Vérification des types à la compilation                            | Frontend      |
| **Husky**                    | Exécution automatique des vérifications avant commit/push          | Projet entier |
| **lint-staged**              | Exécute les linters uniquement sur les fichiers modifiés           | Projet entier |
| **commitlint**               | Validation du format des messages de commit (Conventional Commits) | Projet entier |

### Quand s'exécutent-ils ?

| Moment                   | Outil                              | Action                         |
| ------------------------ | ---------------------------------- | ------------------------------ |
| **À l'écriture** (IDE)   | ESLint, Prettier, TypeScript       | Feedback en temps réel         |
| **Au pre-commit** (hook) | lint-staged (ESLint + Prettier)    | Bloque le commit si erreur     |
| **Au commit-msg** (hook) | commitlint                         | Bloque si message non conforme |
| **En CI**                | ESLint, Prettier, TypeScript build | Bloque la PR si erreur         |

### Couverture cible

- **Logique métier** (services) : > 80%
- **Utilitaires** : > 90%
- **Composants UI** : tester le comportement, pas l'implémentation
- **Controllers** : tester via les tests d'intégration

## Frontend (TypeScript / React)

### Outils

- **Jest** : test runner et assertions
- **React Testing Library** : tests de composants (orienté utilisateur)
- **MSW (Mock Service Worker)** : mock des appels API

### Nommage des tests

```typescript
describe("BudgetCard", () => {
  it("should display the budget name and amount", () => {
    // ...
  });

  it("should call onDelete when delete button is clicked", () => {
    // ...
  });

  it("should show a warning when budget exceeds 90%", () => {
    // ...
  });
});
```

**Règles :**

- `describe` : nom du composant, hook ou fonction testée
- `it` / `test` : commence par `should` + description du comportement attendu
- Description en **anglais**
- Un `it` = **un comportement**

### Emplacement des fichiers

Fichiers de test colocalisés avec le code source :

```
features/budget/components/
├── BudgetCard.tsx
└── BudgetCard.test.tsx

hooks/
├── useDebounce.ts
└── useDebounce.test.ts

lib/
├── utils.ts
└── utils.test.ts
```

### Pattern AAA (Arrange / Act / Assert)

```typescript
it("should calculate remaining balance correctly", () => {
  // Arrange
  const budget: Budget = { id: "1", name: "Courses", amount: 30000 };
  const transactions: Transaction[] = [
    { id: "1", amount: 5000 },
    { id: "2", amount: 12000 },
  ];

  // Act
  const result = calculateRemainingBalance(budget, transactions);

  // Assert
  expect(result).toBe(13000);
});
```

### Tests de composants

Tester le **comportement utilisateur**, pas l'implémentation :

```typescript
// Bon : teste ce que l'utilisateur voit et fait
it('should display error message when form is submitted empty', async () => {
  render(<BudgetForm onSubmit={mockSubmit} />);

  await userEvent.click(screen.getByRole('button', { name: /créer/i }));

  expect(screen.getByText(/le nom est obligatoire/i)).toBeInTheDocument();
  expect(mockSubmit).not.toHaveBeenCalled();
});

// Mauvais : teste l'implémentation interne
it('should set error state to true', () => {
  const { result } = renderHook(() => useBudgetForm());
  act(() => result.current.handleSubmit());
  expect(result.current.errors.name).toBe(true);  // Détail d'implémentation
});
```

### Tests de hooks

```typescript
import { renderHook, act } from "@testing-library/react";

describe("useDebounce", () => {
  it("should return debounced value after delay", () => {
    jest.useFakeTimers();

    const { result, rerender } = renderHook(({ value }) => useDebounce(value, 500), {
      initialProps: { value: "initial" },
    });

    rerender({ value: "updated" });
    expect(result.current).toBe("initial");

    act(() => jest.advanceTimersByTime(500));
    expect(result.current).toBe("updated");
  });
});
```

## Backend (Java / Spring Boot)

### Outils

- **JUnit 5** : test runner et assertions
- **Mockito** : mocking
- **Spring Boot Test** : tests d'intégration
- **AssertJ** : assertions fluides (optionnel mais recommandé)

### Nommage des tests

```java
@DisplayName("BudgetService")
class BudgetServiceTest {

    @Test
    @DisplayName("should return budget when valid id is provided")
    void shouldReturnBudget_whenValidId() {
        // ...
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when budget does not exist")
    void shouldThrowException_whenBudgetNotFound() {
        // ...
    }
}
```

**Règles :**

- Classe : `<ClasseTestée>Test.java`
- Méthode : `should<Résultat>_when<Condition>()`
- Utiliser `@DisplayName` pour des descriptions lisibles
- Un test = **un comportement**

### Emplacement des fichiers

Structure miroir dans `src/test/java/` :

```
src/test/java/yiroma/budgetmanagement/
├── controller/
│   └── BudgetControllerTest.java     # Tests d'intégration (@WebMvcTest)
├── service/
│   └── BudgetServiceTest.java        # Tests unitaires
├── repository/
│   └── BudgetRepositoryTest.java     # Tests d'intégration (@DataJpaTest)
└── mapper/
    └── BudgetMapperTest.java         # Tests unitaires
```

### Tests unitaires (Service)

```java
@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    @DisplayName("should return budget DTO when budget exists")
    void shouldReturnBudgetDto_whenBudgetExists() {
        // Arrange
        Long budgetId = 1L;
        Budget budget = Budget.builder().id(budgetId).name("Courses").build();
        BudgetDto expectedDto = new BudgetDto(budgetId, "Courses", 30000L);

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        when(budgetMapper.toDto(budget)).thenReturn(expectedDto);

        // Act
        BudgetDto result = budgetService.findById(budgetId);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
        verify(budgetRepository).findById(budgetId);
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException when budget does not exist")
    void shouldThrowException_whenBudgetNotFound() {
        // Arrange
        Long budgetId = 999L;
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> budgetService.findById(budgetId));
    }
}
```

### Tests d'intégration (Controller)

```java
@WebMvcTest(BudgetController.class)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetService budgetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/budgets/{id} should return 200 with budget")
    void getById_shouldReturn200() throws Exception {
        // Arrange
        BudgetDto dto = new BudgetDto(1L, "Courses", 30000L);
        when(budgetService.findById(1L)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/budgets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Courses"))
                .andExpect(jsonPath("$.amount").value(30000));
    }

    @Test
    @DisplayName("POST /api/v1/budgets should return 400 when name is blank")
    void create_shouldReturn400_whenInvalidData() throws Exception {
        // Arrange
        CreateBudgetDto dto = new CreateBudgetDto("", null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
```

### Tests d'intégration (Repository)

```java
@DataJpaTest
class BudgetRepositoryTest {

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    @DisplayName("should find budgets by user id")
    void shouldFindBudgetsByUserId() {
        // Arrange - données insérées via @Sql ou TestEntityManager

        // Act
        List<Budget> budgets = budgetRepository.findByUserId(1L);

        // Assert
        assertThat(budgets).hasSize(2);
    }
}
```

## Règles générales

### Quand mocker

| Mocker                                     | Ne pas mocker          |
| ------------------------------------------ | ---------------------- |
| Appels API externes                        | Logique métier pure    |
| Base de données (dans les tests unitaires) | Utilitaires et helpers |
| Services tiers (email, paiement)           | Mappers                |
| Timer / Date (si nécessaire)               |                        |

### Ce qu'on ne teste PAS

- Les getters / setters (générés par Lombok)
- Les configurations Spring triviales
- Le code de librairies tierces
- L'implémentation interne (tester le comportement, pas le comment)

### Données de test

- Utiliser des **builders** ou **factories** pour créer les données de test
- Éviter les données magiques : utiliser des constantes nommées
- Chaque test doit être **indépendant** et **isolé**

```java
// Bon : intention claire
Budget budget = Budget.builder()
        .name("Courses mensuelles")
        .amount(30000L)
        .status(BudgetStatus.ACTIVE)
        .build();

// Mauvais : données magiques
Budget budget = new Budget(null, "abc", 123L, null, null, null);
```
