import Link from "next/link"
import { Button } from "@/components/ui/button"

export function CtaSection() {
  return (
    <section className="relative bg-[#1C3A2F] text-center overflow-hidden">
      {/* Decorative circles */}
      <div className="absolute w-[500px] h-[500px] rounded-full border border-[#F5F0E8]/5 top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 pointer-events-none" />
      <div className="absolute w-[300px] h-[300px] rounded-full border border-[#C9A84C]/10 top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 pointer-events-none" />

      <div className="relative z-10 max-w-[1440px] mx-auto px-[5%] py-28">
        <h2 className="font-playfair text-[clamp(2rem,4vw,3rem)] font-bold leading-[1.2] tracking-[-0.02em] text-[#F5F0E8] mb-4">
          Prêt à reprendre le
          <br />
          <em className="italic text-[#E2C97E]">contrôle de votre argent ?</em>
        </h2>
        <p className="text-[#F5F0E8]/60 font-light mb-10">
          Rejoignez des milliers d&apos;utilisateurs qui gèrent leurs finances avec sérénité.
        </p>

        <div className="flex items-center justify-center gap-4 flex-wrap">
          <Button
            asChild
            className="rounded-full bg-[#C9A84C] hover:bg-[#E2C97E] text-[#1A1A1A] font-medium px-8 py-6 text-base shadow-[0_4px_20px_rgba(201,168,76,0.3)] hover:-translate-y-0.5 transition-all"
          >
            <Link href="/register">Créer mon compte gratuit</Link>
          </Button>
          <Button
            asChild
            variant="ghost"
            className="rounded-full border border-[#F5F0E8]/15 text-[#F5F0E8]/70 hover:border-[#F5F0E8]/40 hover:text-[#F5F0E8] hover:bg-transparent px-8 py-6 text-base"
          >
            <Link href="/login">J&apos;ai déjà un compte</Link>
          </Button>
        </div>
      </div>
    </section>
  )
}
