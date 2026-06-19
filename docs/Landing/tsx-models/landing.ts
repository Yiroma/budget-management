export const NAV_LINKS = [
  { label: "Fonctionnalités", href: "#features" },
  { label: "Comment ça marche", href: "#how" },
  { label: "Tarifs", href: "#pricing" },
]

export const STATS = [
  { number: "3×", label: "Plus de visibilité sur vos finances" },
  { number: "100%", label: "Vos données, privées et sécurisées" },
  { number: "∞", label: "Opérations récurrentes automatisées" },
]

export const FEATURES = [
  {
    icon: "📊",
    title: "Solde réel dès le 1er du mois",
    description:
      "Enregistrez revenus et charges récurrentes en début de mois. L'app calcule immédiatement ce qu'il vous reste réellement pour vivre.",
  },
  {
    icon: "🔁",
    title: "Récurrence automatique",
    description:
      "Loyer, abonnements, crédits... Vos dépenses récurrentes se reportent automatiquement chaque mois. Zéro ressaisie.",
  },
  {
    icon: "💳",
    title: "Carte à débit différé",
    description:
      "Anticipez vos prélèvements de fin de mois en enregistrant vos dépenses différées. Plus de mauvaises surprises.",
  },
  {
    icon: "👫",
    title: "Compte commun partagé",
    description:
      "En duo ou en groupe, gérez ensemble un compte commun. L'app calcule exactement ce que chacun doit verser selon votre répartition.",
  },
  {
    icon: "⚖️",
    title: "Répartition personnalisable",
    description:
      "50/50 par défaut, mais ajustable selon les revenus de chacun. Chaque membre voit sa part équitable des charges communes.",
  },
  {
    icon: "📱",
    title: "Mobile-first & responsive",
    description:
      "Conçue pour être fluide sur mobile, tablette et desktop. Vos finances disponibles où que vous soyez.",
  },
]

export const STEPS = [
  {
    number: "01",
    title: "Créez votre compte",
    description:
      "Inscrivez-vous et choisissez votre formule : Free ou Premium. Accédez à votre espace en quelques secondes.",
  },
  {
    number: "02",
    title: "Ajoutez vos revenus",
    description:
      "En début de mois, renseignez votre salaire, CAF, aides... et déduisez votre éventuel découvert du mois précédent.",
  },
  {
    number: "03",
    title: "Enregistrez vos charges",
    description:
      "Loyer, abonnements, crédits, dépenses différées... L'app calcule immédiatement votre solde disponible réel.",
  },
  {
    number: "04",
    title: "Suivez au fil du mois",
    description:
      "Ajoutez vos dépenses au fur et à mesure. Votre solde s'actualise en temps réel. Le mois suivant, tout repart automatiquement.",
  },
]

export type PricingPlan = {
  label: string
  badge?: string
  price: string
  period: string
  features: { text: string; included: boolean; note?: string }[]
  cta: { label: string; href: string }
  highlighted?: boolean
}

export const PRICING_PLANS: PricingPlan[] = [
  {
    label: "Free",
    price: "0€",
    period: "pour toujours",
    features: [
      { text: "1 compte bancaire", included: true },
      { text: "1 budget, jusqu'à 2 membres", included: true },
      { text: "Revenus & dépenses récurrentes", included: true },
      { text: "Solde restant en temps réel", included: true },
      { text: "Avec publicités", included: false, note: "pub" },
    ],
    cta: { label: "Commencer gratuitement", href: "/register?plan=free" },
  },
  {
    label: "Premium",
    badge: "⭐ Populaire",
    price: "4,99€",
    period: "par mois",
    highlighted: true,
    features: [
      { text: "Comptes bancaires illimités", included: true },
      { text: "Budgets illimités, membres illimités", included: true },
      { text: "Idéal couple, colocation, famille", included: true },
      { text: "Répartition personnalisable (pondérée)", included: true },
      { text: "Sans publicité", included: true },
    ],
    cta: { label: "Essayer Premium", href: "/register?plan=premium" },
  },
]
