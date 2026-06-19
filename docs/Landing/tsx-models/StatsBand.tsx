import { STATS } from "@/lib/constants/landing"

export function StatsBand() {
  return (
    <div className="bg-[#1C3A2F]">
      <div className="max-w-[1440px] mx-auto py-12 px-[5%] grid grid-cols-3 gap-8 text-center">
        {STATS.map((stat) => (
          <div key={stat.number}>
            <p className="font-playfair text-[clamp(1.8rem,4vw,2.5rem)] font-bold text-[#C9A84C] leading-none mb-1.5">
              {stat.number}
            </p>
            <p className="text-xs text-[#F5F0E8]/60 font-light">{stat.label}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
