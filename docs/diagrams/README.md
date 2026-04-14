# Diagrammes UML

Ce dossier contient les diagrammes de modélisation du projet Budget Management.

## Contenu

| Fichier         | Type                               | Description                                           |
| --------------- | ---------------------------------- | ----------------------------------------------------- |
| `use-case.webp` | Diagramme de cas d'utilisation     | Acteurs (Visitor, User) et fonctionnalités du système |
| `mcd.webp`      | Modèle Conceptuel de Données (MCD) | Entités, attributs et relations conceptuelles         |
| `mld.webp`      | Modèle Logique de Données (MLD)    | Transposition en schéma relationnel                   |
| `mpd.webp`      | Modèle Physique de Données (MPD)   | Schéma PostgreSQL avec types et contraintes (référence pour les entités JPA) |

## Entités principales

- **User** : utilisateur de l'application
- **Subscription** : plan d'abonnement Free ou Premium (limites comptes, budgets, membres)
- **Account** : compte bancaire virtuel appartenant à un utilisateur
- **Budget** : enveloppe de dépenses, solo ou partagée (remplace l'ancien "Group")
- **Budget_member** : table de jointure User ↔ Budget (rôle owner/member, taux de contribution) — source de vérité unique pour identifier le propriétaire d'un budget
- **Monthly_budget** : instantiation mensuelle d'un budget (mois + année)
- **Operation** : mouvement financier (revenu ou dépense) attaché à un compte ou un budget
- **Category** : catégorie d'opération (label + icône)
- **Recurrence_rule** : règle de répétition automatique d'une opération

## Concept clé

```
Argent de poche = Revenus - Charges récurrentes (compte) - Part budget partagé
Part budget partagé = Total opérations budget ÷ nombre de membres (pondéré par contribution_rate)
```

> Les diagrammes sont à régénérer dans l'outil de modélisation (draw.io ou équivalent) à chaque évolution majeure du schéma.
