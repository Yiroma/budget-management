# Charte Graphique — budget.management

---

## 1. Présentation du projet

**budget.management** est une application web SaaS de gestion de budget personnel et partagé. Elle permet à ses utilisateurs de connaître, dès le début du mois, leur solde réellement disponible après déduction de toutes leurs charges récurrentes.

L'application cible trois profils d'utilisateurs : les personnes seules (**Solo**), les couples (**Duo**) et les groupes (colocation, famille, association — **Multi**). Elle est conçue **mobile-first** et responsive (mobile, tablette, desktop), avec une future déclinaison en application mobile native (iOS et Android).

**Valeurs de la marque :** clarté, sérénité, confiance, sobriété.

---

## 2. Typographies

### Police principale — Corps de texte

**DM Sans**

- Source : Google Fonts
- Utilisation : tous les textes courants, labels, boutons, paragraphes, navigation
- Graisses utilisées : 300 (light), 400 (regular), 500 (medium)
- Caractère : géométrique, moderne, excellent rendu sur écran, très lisible en petite taille

### Police secondaire — Titres & display

**Playfair Display**

- Source : Google Fonts
- Utilisation : titres de section (h1, h2, h3), accroches, logotype
- Graisses utilisées : 400 (regular), 600 (semibold), 700 (bold) + italique
- Caractère : sérif élégant, apporte chaleur et premium, contraste fort avec DM Sans

### Échelle typographique recommandée

| Rôle            | Police           | Taille                     | Graisse |
| --------------- | ---------------- | -------------------------- | ------- |
| H1 (hero)       | Playfair Display | clamp(2.8rem, 6vw, 4.5rem) | 700     |
| H2 (section)    | Playfair Display | clamp(2rem, 4vw, 3rem)     | 700     |
| H3 (card)       | Playfair Display | 1.2rem                     | 600     |
| Body            | DM Sans          | 1rem                       | 400     |
| Body small      | DM Sans          | 0.875rem                   | 300     |
| Label / Caption | DM Sans          | 0.75rem                    | 500     |
| Bouton          | DM Sans          | 0.9–0.95rem                | 500     |

### Import Google Fonts (Next.js)

```ts
import { Playfair_Display, DM_Sans } from "next/font/google";

const playfair = Playfair_Display({
  subsets: ["latin"],
  variable: "--font-playfair",
  weight: ["400", "600", "700"],
  style: ["normal", "italic"],
});

const dmSans = DM_Sans({
  subsets: ["latin"],
  variable: "--font-dm-sans",
  weight: ["300", "400", "500"],
});
```

---

## 3. Palette de couleurs

### Couleurs principales

| Nom        | Rôle                        | HEX       | HSL           | OKLCH          |
| ---------- | --------------------------- | --------- | ------------- | -------------- |
| Vert forêt | Primary — CTA, nav, sidebar | `#1C3A2F` | `155 35% 18%` | `32% 0.07 163` |
| Vert clair | Primary hover / variante    | `#2A5242` | `155 32% 24%` | `38% 0.08 163` |
| Or         | Accent — highlights, actif  | `#C9A84C` | `40 53% 55%`  | `73% 0.12 80`  |
| Or clair   | Accent hover / variante     | `#E2C97E` | `40 60% 69%`  | `83% 0.10 82`  |

### Couleurs de base

| Nom         | Rôle                            | HEX       | HSL          | OKLCH          |
| ----------- | ------------------------------- | --------- | ------------ | -------------- |
| Sable       | Background principal            | `#F5F0E8` | `40 33% 93%` | `95% 0.01 80`  |
| Sable foncé | Background secondaire/section   | `#EDE6D6` | `40 27% 87%` | `91% 0.02 80`  |
| Charcoal    | Foreground — texte principal    | `#1A1A1A` | `0 0% 10%`   | `15% 0.00 0`   |
| Gris muted  | Texte secondaire / descriptions | `#6B7280` | `220 9% 46%` | `52% 0.02 264` |
| Blanc       | Card, popover, input            | `#FFFFFF` | `0 0% 100%`  | `100% 0.00 0`  |

### Couleurs sémantiques

| Nom     | Rôle               | HEX       |
| ------- | ------------------ | --------- |
| Success | Validation, income | `#16A34A` |
| Warning | Alerte budget      | `#D97706` |
| Danger  | Erreur, dépense    | `#DC2626` |
| Info    | Information neutre | `#2563EB` |

---

## 4. Accessibilité — Contrastes WCAG 2.1

Seuils WCAG : **AA texte normal ≥ 4.5:1** | **AA grand texte ≥ 3.0:1** | **AAA ≥ 7.0:1**

| Combinaison                        | Ratio   | Niveau                         |
| ---------------------------------- | ------- | ------------------------------ |
| Charcoal `#1A1A1A` sur Blanc       | 17.40:1 | ✅ AAA                         |
| Charcoal `#1A1A1A` sur Sable       | 15.34:1 | ✅ AAA                         |
| Vert forêt `#1C3A2F` sur Blanc     | 12.38:1 | ✅ AAA                         |
| Sable `#F5F0E8` sur Vert forêt     | 10.92:1 | ✅ AAA                         |
| Charcoal `#1A1A1A` sur Sable foncé | 14.00:1 | ✅ AAA                         |
| Or `#C9A84C` sur Charcoal          | 7.62:1  | ✅ AAA                         |
| Or `#C9A84C` sur Vert forêt        | 5.42:1  | ✅ AA                          |
| Gris muted `#6B7280` sur Blanc     | 4.83:1  | ✅ AA                          |
| Gris muted `#6B7280` sur Sable     | 4.26:1  | ⚠️ AA grands textes uniquement |
| Or `#C9A84C` sur Sable             | 2.01:1  | ❌ FAIL                        |

### ⚠️ Points d'attention

- **Or sur Sable** (2.01:1) : ne jamais utiliser l'or comme couleur de texte sur fond sable. L'or est réservé aux **éléments décoratifs, icônes, bordures et accents visuels** uniquement.
- **Gris muted sur Sable** (4.26:1) : acceptable uniquement pour du texte de taille ≥ 18px (regular) ou ≥ 14px (bold). Pour les petits textes, préférer `#5A6270` (légèrement plus foncé).
- Sur fond sable, toujours privilegier **Charcoal** `#1A1A1A` pour les textes importants.

---

## 5. Iconographie

- **Librairie recommandée :** Lucide React (déjà intégrée avec shadcn/ui)
- **Style :** trait fin (stroke), cohérent avec l'esthétique minimaliste
- **Tailles standards :** 16px (inline), 20px (bouton), 24px (navigation), 32px (feature card)
- **Couleur :** hérite de la couleur du texte parent par défaut (`currentColor`)

---

## 6. Espacements & Layout

- **Système de grille :** 4px base unit (Tailwind par défaut)
- **Marges de page :** 5% sur mobile, 5% sur tablette, 5–8% sur desktop
- **Border radius :**
  - Petits éléments (badge, tag) : `9999px` (pill)
  - Boutons : `9999px` (pill)
  - Cards : `16px` (`rounded-2xl`)
  - Modales : `24px` (`rounded-3xl`)
  - Inputs : `8px` (`rounded-lg`)

---

## 7. Élévations & Ombres

| Niveau | Usage             | Valeur CSS                        |
| ------ | ----------------- | --------------------------------- |
| 1      | Card au repos     | `0 1px 3px rgba(28,58,47,0.06)`   |
| 2      | Card hover        | `0 16px 40px rgba(28,58,47,0.10)` |
| 3      | Dropdown, popover | `0 8px 24px rgba(28,58,47,0.12)`  |
| 4      | Modale            | `0 24px 60px rgba(28,58,47,0.16)` |
| CTA    | Bouton primary    | `0 4px 20px rgba(28,58,47,0.20)`  |

---

## 8. Composants clés — Règles d'usage

### Boutons

| Variante    | Fond          | Texte          | Usage                         |
| ----------- | ------------- | -------------- | ----------------------------- |
| Primary     | Vert forêt    | Sable          | Action principale             |
| Gold        | Or            | Charcoal       | CTA secondaire fort (landing) |
| Outline     | Transparent   | Charcoal       | Action secondaire             |
| Ghost       | Transparent   | Charcoal/Sable | Action tertiaire / annulation |
| Destructive | Rouge #DC2626 | Blanc          | Suppression, danger           |

### États interactifs

- **Hover :** légère montée (`translateY(-2px)`) + ombre renforcée
- **Focus :** ring vert forêt `2px solid #1C3A2F` avec offset de 2px
- **Disabled :** opacité 40%, cursor not-allowed
- **Loading :** spinner DM Sans, même couleur que le texte du bouton

---

## 9. Ton & voix (UX Writing)

- Tutoiement dans toute l'interface
- Phrases courtes, directes, rassurantes
- Messages d'erreur : explicatifs, jamais techniques (`"Email déjà utilisé"` plutôt que `"Conflict 409"`)
- Messages de succès : chaleureux mais sobres (`"Budget créé !"`)
- CTA : verbes d'action à l'infinitif (`"Créer mon budget"`, `"Voir mon solde"`)
