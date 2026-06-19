import { Navbar } from "@/components/landing/Navbar"
import { Hero } from "@/components/landing/Hero"
import { StatsBand } from "@/components/landing/StatsBand"
import { Features } from "@/components/landing/Features"
import { HowItWorks } from "@/components/landing/HowItWorks"
import { Pricing } from "@/components/landing/Pricing"
import { CtaSection } from "@/components/landing/CtaSection"
import { Footer } from "@/components/landing/Footer"

export default function LandingPage() {
  return (
    <main className="min-h-screen bg-[#F5F0E8] font-sans text-[#1A1A1A]">
      <Navbar />
      <Hero />
      <StatsBand />
      <Features />
      <HowItWorks />
      <Pricing />
      <CtaSection />
      <Footer />
    </main>
  )
}
