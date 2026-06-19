import Link from "next/link"
import { Check, X } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { PRICING_PLANS } from "@/lib/constants/landing"
import { cn } from "@/lib/utils"

export function Pricing() {
  return (
    <section id="pricing" className="max-w-[1440px] mx-auto px-[5%] py-28 text-center">
      <span className="text-xs font-medium tracking-[0.15em] uppercase text-[#C9A84C] mb-3 block">
        Tarifs
      </span>
      <h2 className="font-playfair text-[clamp(2rem,4vw,3rem)] font-bold leading-[1.2] tracking-[-0.02em] text-[#1A1A1A] mb-14">
        Une formule pour{" "}
        <em className="italic text-[#1C3A2F]">chaque situation.</em>
      </h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-3xl mx-auto">
        {PRICING_PLANS.map((plan) => (
          <div
            key={plan.label}
            className={cn(
              "rounded-3xl p-10 text-left border transition-all duration-300 hover:-translate-y-1",
              plan.highlighted
                ? "bg-[#1C3A2F] border-[#1C3A2F] hover:shadow-[0_20px_50px_rgba(28,58,47,0.25)]"
                : "bg-white border-[#1C3A2F]/8 hover:shadow-[0_20px_50px_rgba(28,58,47,0.1)]"
            )}
          >
            {/* Header */}
            <div className="mb-6">
              {plan.badge ? (
                <Badge className="mb-4 rounded-full bg-[#C9A84C]/20 text-[#C9A84C] border-0 text-xs font-medium">
                  {plan.badge}
                </Badge>
              ) : (
                <p
                  className={cn(
                    "text-xs font-medium tracking-[0.12em] uppercase mb-4",
                    plan.highlighted ? "text-[#C9A84C]" : "text-[#6B7280]"
                  )}
                >
                  {plan.label}
                </p>
              )}

              <p
                className={cn(
                  "font-playfair text-5xl font-bold leading-none mb-1",
                  plan.highlighted ? "text-[#F5F0E8]" : "text-[#1A1A1A]"
                )}
              >
                {plan.price}
              </p>
              <p
                className={cn(
                  "text-sm font-light",
                  plan.highlighted ? "text-[#F5F0E8]/50" : "text-[#6B7280]"
                )}
              >
                {plan.period}
              </p>
            </div>

            {/* Features list */}
            <ul className="space-y-3 mb-8">
              {plan.features.map((feature) => (
                <li key={feature.text} className="flex items-start gap-3">
                  <span
                    className={cn(
                      "w-5 h-5 rounded-full flex items-center justify-center flex-shrink-0 mt-0.5",
                      feature.included
                        ? plan.highlighted
                          ? "bg-[#F5F0E8]/12 text-[#C9A84C]"
                          : "bg-[#1C3A2F]/7 text-[#1C3A2F]"
                        : "bg-[#6B7280]/8 text-[#6B7280]"
                    )}
                  >
                    {feature.included ? <Check size={11} /> : <X size={10} />}
                  </span>
                  <span
                    className={cn(
                      "text-sm font-light",
                      !feature.included && "opacity-50 italic",
                      plan.highlighted
                        ? feature.included ? "text-[#F5F0E8]/70" : "text-[#F5F0E8]/40"
                        : "text-[#6B7280]"
                    )}
                  >
                    {feature.text}
                  </span>
                </li>
              ))}
            </ul>

            {/* CTA */}
            {plan.highlighted ? (
              <Button
                asChild
                className="w-full rounded-full bg-[#C9A84C] hover:bg-[#E2C97E] text-[#1A1A1A] font-medium py-5"
              >
                <Link href={plan.cta.href}>{plan.cta.label}</Link>
              </Button>
            ) : (
              <Button
                asChild
                variant="outline"
                className="w-full rounded-full border-[#1C3A2F]/20 text-[#1A1A1A] hover:border-[#1C3A2F] hover:bg-[#1C3A2F]/4 font-medium py-5"
              >
                <Link href={plan.cta.href}>{plan.cta.label}</Link>
              </Button>
            )}
          </div>
        ))}
      </div>

      <p className="text-xs text-[#6B7280] mt-8 italic font-light">
        * Les tarifs sont des exemples et peuvent évoluer avant le lancement.
      </p>
    </section>
  )
}
