import { STEPS } from "@/lib/constants/landing"

export function HowItWorks() {
  return (
    <section id="how" className="bg-[#EDE6D6]">
      <div className="max-w-[1440px] mx-auto px-[5%] py-28">
        <span className="text-xs font-medium tracking-[0.15em] uppercase text-[#C9A84C] mb-3 block">
          Comment ça marche
        </span>
        <h2 className="font-playfair text-[clamp(2rem,4vw,3rem)] font-bold leading-[1.2] tracking-[-0.02em] text-[#1A1A1A] max-w-xl mb-14">
          Démarrez en quelques <em className="italic text-[#1C3A2F]">minutes.</em>
        </h2>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          {STEPS.map((step) => (
            <div key={step.number} className="pt-2">
              <p className="font-playfair text-[4rem] font-bold text-[#1C3A2F]/8 leading-none mb-4">
                {step.number}
              </p>
              <h3 className="font-playfair text-lg font-semibold text-[#1A1A1A] mb-2">
                {step.title}
              </h3>
              <p className="text-sm text-[#6B7280] leading-relaxed font-light">
                {step.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
