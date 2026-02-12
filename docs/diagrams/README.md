# Diagrammes UML

Ce dossier contient les diagrammes de modélisation du projet Budget Management.

## Contenu

| Fichier         | Type                               | Description                                           |
| --------------- | ---------------------------------- | ----------------------------------------------------- |
| `use-case.webp` | Diagramme de cas d'utilisation     | Acteurs (Visitor, User) et fonctionnalités du système |
| `mcd.webp`      | Modèle Conceptuel de Données (MCD) | Entités, attributs et relations conceptuelles         |
| `mld.webp`      | Modèle Logique de Données (MLD)    | Transposition en schéma relationnel                   |
| `mpd.webp`      | Modèle Physique de Données (MPD)   | Implémentation PostgreSQL avec types et contraintes   |

## Entités principales

- **User** : utilisateur de l'application
- **Subscription** : plan d'abonnement (limites de groupes, comptes, membres)
- **Group** : groupe de budget (solo, duo, multi)
- **Account** : compte bancaire rattaché à un groupe
- **Monthly_budget** : budget mensuel
- **Operation** : opération financière (revenu/dépense)
- **Category** : catégorie d'opération
- **Recurrence_rule** : règle de récurrence pour les opérations automatiques
