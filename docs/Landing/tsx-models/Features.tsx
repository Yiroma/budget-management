import { FEATURES } from "@/lib/constants/landing"

export function Features() {
  return (
    <section id="features" className="max-w-[1440px] mx-auto px-[5%] py-28">
      <span className="text-xs font-medium tracking-[0.15em] uppercase text-[#C9A84C] mb-3 block">
        Fonctionnalités
      </span>
      <h2 className="font-playfair text-[clamp(2rem,4vw,3rem)] font-bold leading-[1.2] tracking-[-0.02em] text-[#1A1A1A] max-w-xl mb-14">
        Tout ce qu&apos;il vous faut pour{" "}
        <em className="italic text-[#1C3A2F]">gérer sereinement.</em>
      </h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {FEATURES.map((feature) => (
          <div
            key={feature.title}
            className="group relative bg-white rounded-2xl p-10 border border-[#1C3A2F]/6 overflow-hidden
              hover:-translate-y-1 hover:shadow-[0_16px_40px_rgba(28,58,47,0.1)] transition-all duration-300"
          >
            {/* Top accent bar */}
            <div className="absolute top-0 left-0 right-0 h-[3px] bg-gradient-to-r from-[#1C3A2F] to-[#C9A84C] opacity-0 group-hover:opacity-100 transition-opacity duration-300" />

            <div className="w-13 h-13 rounded-2xl bg-[#1C3A2F]/7 flex items-center justify-center text-2xl mb-6">
              {feature.icon}
            </div>
            <h3 className="font-playfair text-xl font-semibold text-[#1A1A1A] mb-3">
              {feature.title}
            </h3>
            <p className="text-sm text-[#6B7280] leading-relaxed font-light">
              {feature.description}
            </p>
          </div>
        ))}
      </div>
    </section>
  )
}
