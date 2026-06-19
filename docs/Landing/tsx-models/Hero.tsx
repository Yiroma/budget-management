import Link from "next/link"
import { ArrowRight } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"

export function Hero() {
  return (
    <section className="relative min-h-screen flex items-center overflow-hidden">
      {/* Background decorations */}
      <div className="absolute inset-0 pointer-events-none">
        <div className="absolute inset-0 bg-[radial-gradient(ellipse_70%_60%_at_75%_50%,rgba(28,58,47,0.06)_0%,transparent_70%)]" />
        <div className="absolute w-[600px] h-[600px] rounded-full border border-[#1C3A2F]/6 -right-[100px] top-1/2 -translate-y-1/2">
          <div className="absolute inset-[60px] rounded-full border border-[#C9A84C]/10" />
        </div>
      </div>

      <div className="relative z-10 max-w-[1440px] mx-auto w-full px-[5%] pt-32 pb-20">
        <div className="max-w-2xl animate-fade-up">
          {/* Badge */}
          <Badge
            variant="outline"
            className="mb-8 rounded-full border-[#1C3A2F]/12 bg-[#1C3A2F]/7 text-[#1C3A2F] text-xs font-medium px-4 py-1.5 gap-2"
          >
            <span className="w-1.5 h-1.5 rounded-full bg-[#C9A84C] flex-shrink-0" />
            Gérez vos finances en famille ou en solo
          </Badge>

          {/* Heading */}
          <h1 className="font-playfair text-[clamp(2.8rem,6vw,4.5rem)] font-bold leading-[1.1] tracking-[-0.02em] text-[#1A1A1A] mb-6">
            Votre budget,
            <br />
            <em className="text-[#1C3A2F] not-italic font-playfair italic">enfin sous contrôle.</em>
          </h1>

          {/* Subtitle */}
          <p className="text-lg text-[#6B7280] leading-relaxed max-w-lg mb-10 font-light">
            Sachez dès le 1<sup>er</sup> du mois combien il vous reste{" "}
            <em>vraiment</em> après toutes vos charges. Seul, en couple ou en
            colocation — gérez votre argent simplement.
          </p>

          {/* CTAs */}
          <div className="flex flex-wrap items-center gap-5">
            <Button
              asChild
              className="rounded-full bg-[#1C3A2F] hover:bg-[#2A5242] text-[#F5F0E8] px-8 py-6 text-base font-medium shadow-[0_4px_20px_rgba(28,58,47,0.2)] hover:shadow-[0_8px_30px_rgba(28,58,47,0.25)] hover:-translate-y-0.5 transition-all"
            >
              <Link href="/register">Commencer gratuitement</Link>
            </Button>

            <Link
              href="#how"
              className="flex items-center gap-2 text-[#1A1A1A]/70 hover:text-[#1A1A1A] text-base transition-colors group"
            >
              Voir comment ça marche
              <ArrowRight size={16} className="group-hover:translate-x-1 transition-transform" />
            </Link>
          </div>
        </div>
      </div>
    </section>
  )
}
