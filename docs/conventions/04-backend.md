# Conventions Backend

## Stack technique

- **Framework** : Spring Boot 4.0.2
- **Langage** : Java 21
- **Build** : Maven 3.9.12
- **BDD** : PostgreSQL
- **ORM** : Spring Data JPA
- **Sécurité** : Spring Security
- **Utilitaire** : Lombok

## Architecture en couches

Le backend suit une architecture en couches stricte :

```
Controller → Service → Repository → Database
     ↕           ↕
    DTO        Entity
```

### Règles

- Un **Controller** ne contient jamais de logique métier — il délègue au Service
- Un **Service** contient toute la logique métier
- Un **Repository** ne contient que les requêtes de données
- Les **Entities** ne sortent jamais du Service — on retourne toujours des **DTOs**
- Les **Mappers** assurent la conversion Entity ↔ DTO

## Structure des packages

```
yiroma.budgetmanagement/
├── config/                     # Configuration Spring (Security, CORS, etc.)
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   └── AppConfig.java
│
├── controller/                 # Contrôleurs REST
│   ├── BudgetController.java
│   ├── TransactionController.java
│   └── UserController.java
│
├── service/                    # Logique métier
│   ├── BudgetService.java
│   ├── TransactionService.java
│   └── UserService.java
│
├── repository/                 # Accès aux données (Spring Data JPA)
│   ├── BudgetRepository.java
│   ├── TransactionRepository.java
│   └── UserRepository.java
│
├── model/                      # Entités JPA
│   ├── Budget.java
│   ├── Transaction.java
│   └── User.java
│
├── dto/                        # Data Transfer Objects
│   ├── request/                # DTOs entrants
│   │   ├── CreateBudgetDto.java
│   │   └── UpdateBudgetDto.java
│   └── response/               # DTOs sortants
│       ├── BudgetDto.java
│       └── BudgetSummaryDto.java
│
├── mapper/                     # Mappers Entity ↔ DTO
│   ├── BudgetMapper.java
│   └── UserMapper.java
│
├── exception/                  # Exceptions custom et gestion globale
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── BusinessException.java
│
├── enums/                      # Énumérations
│   ├── BudgetStatus.java
│   └── TransactionType.java
│
└── BudgetmanagementApplication.java
```

## Conventions par couche

### Controller

```java
@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetDto>> getAll() {
        return ResponseEntity.ok(budgetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BudgetDto> create(@Valid @RequestBody CreateBudgetDto dto) {
        BudgetDto created = budgetService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBudgetDto dto) {
        return ResponseEntity.ok(budgetService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Règles :**

- Annotation `@RestController` + `@RequestMapping` avec le préfixe API
- Injection par constructeur via `@RequiredArgsConstructor` (Lombok)
- Toujours retourner `ResponseEntity<>` pour contrôler le status code
- Utiliser `@Valid` pour la validation des DTOs entrants
- Pas de logique métier dans le controller

### Service

```java
@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    public List<BudgetDto> findAll() {
        return budgetRepository.findAll().stream()
                .map(budgetMapper::toDto)
                .toList();
    }

    public BudgetDto findById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", id));
        return budgetMapper.toDto(budget);
    }

    @Transactional
    public BudgetDto create(CreateBudgetDto dto) {
        Budget budget = budgetMapper.toEntity(dto);
        Budget saved = budgetRepository.save(budget);
        return budgetMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Budget", id);
        }
        budgetRepository.deleteById(id);
    }
}
```

**Règles :**

- Annotation `@Service`
- `@Transactional` sur les méthodes qui modifient des données
- Toujours vérifier l'existence avant modification/suppression
- Lever des exceptions custom (pas de `null` en retour)

### Repository

```java
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(Long userId);

    @Query("SELECT b FROM Budget b WHERE b.status = :status AND b.user.id = :userId")
    List<Budget> findByStatusAndUserId(
            @Param("status") BudgetStatus status,
            @Param("userId") Long userId);
}
```

**Règles :**

- Étendre `JpaRepository<Entity, IdType>`
- Utiliser les méthodes dérivées de Spring Data quand possible (`findBy...`)
- Utiliser `@Query` pour les requêtes complexes
- Préférer JPQL aux requêtes natives

### Entity

```java
@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

**Règles :**

- Annotations Lombok : `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- Nom de table explicite avec `@Table(name = "...")`
- `@Column` avec les contraintes (`nullable`, `length`)
- Les montants sont stockés en **centimes** (type `Long`) pour éviter les erreurs de virgule flottante
- Relations en `FetchType.LAZY` par défaut
- Timestamps automatiques via `@PrePersist` et `@PreUpdate`

### DTO

```java
public record CreateBudgetDto(
        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String name,

        @NotNull(message = "Le montant est obligatoire")
        @Positive(message = "Le montant doit être positif")
        Long amount
) {}
```

**Règles :**

- Utiliser les **Java Records** pour les DTOs (immutables par nature)
- Annotations Jakarta Validation pour la validation
- DTOs séparés pour la création (`CreateXxxDto`), la mise à jour (`UpdateXxxDto`) et la réponse (`XxxDto`)

### Mapper

```java
@Component
public class BudgetMapper {

    public BudgetDto toDto(Budget entity) {
        return new BudgetDto(
                entity.getId(),
                entity.getName(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public Budget toEntity(CreateBudgetDto dto) {
        return Budget.builder()
                .name(dto.name())
                .amount(dto.amount())
                .status(BudgetStatus.ACTIVE)
                .build();
    }
}
```

**Règles :**

- Annotation `@Component` pour l'injection Spring
- Méthodes `toDto()` et `toEntity()`
- Mapping manuel (ou MapStruct si le mapping devient complexe)

### Exception handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erreurs de validation",
                errors
        );
        return ResponseEntity.badRequest().body(error);
    }
}
```

**Règles :**

- Un seul `@RestControllerAdvice` global
- Exceptions custom qui étendent `RuntimeException`
- Format de réponse d'erreur cohérent (`ErrorResponse`)
- Ne jamais exposer les stack traces en production

## Logging

Utiliser SLF4J via l'annotation Lombok `@Slf4j` :

```java
@Slf4j
@Service
public class BudgetService {

    public BudgetDto create(CreateBudgetDto dto) {
        log.info("Création d'un budget : {}", dto.name());
        // ...
        log.debug("Budget créé avec l'id : {}", saved.getId());
        return budgetMapper.toDto(saved);
    }
}
```

### Niveaux de log

| Niveau  | Usage                                                 |
| ------- | ----------------------------------------------------- |
| `ERROR` | Erreur irrécupérable, intervention nécessaire         |
| `WARN`  | Situation anormale mais gérée                         |
| `INFO`  | Événement métier significatif (création, suppression) |
| `DEBUG` | Détail technique pour le débogage                     |

### Règles

- Ne jamais logger d'informations sensibles (mots de passe, tokens)
- Utiliser les placeholders `{}` (pas la concaténation de strings)
- `INFO` en production, `DEBUG` en développement
