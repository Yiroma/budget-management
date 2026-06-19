import Link from "next/link"

const FOOTER_LINKS = [
  { label: "Confidentialité", href: "/privacy" },
  { label: "CGU", href: "/terms" },
  { label: "Contact", href: "/contact" },
]

export function Footer() {
  return (
    <footer className="bg-[#1A1A1A]">
      <div className="max-w-[1440px] mx-auto px-[5%] pt-12 pb-8 flex flex-col sm:flex-row items-center justify-between gap-6 flex-wrap">
        <p className="font-playfair text-lg font-bold text-[#F5F0E8]">
          budget<span className="text-[#C9A84C]">.</span>management
        </p>

        <p className="text-xs text-[#F5F0E8]/30 font-light">
          © {new Date().getFullYear()} Budget Management. Tous droits réservés.
        </p>

        <nav className="flex gap-6">
          {FOOTER_LINKS.map((link) => (
            <Link
              key={link.href}
              href={link.href}
              className="text-xs text-[#F5F0E8]/40 hover:text-[#F5F0E8]/80 transition-colors"
            >
              {link.label}
            </Link>
          ))}
        </nav>
      </div>
    </footer>
  )
}
