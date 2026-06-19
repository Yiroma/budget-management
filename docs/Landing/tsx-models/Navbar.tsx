"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import { Menu, X } from "lucide-react"
import { Button } from "@/components/ui/button"
import { NAV_LINKS } from "@/lib/constants/landing"
import { cn } from "@/lib/utils"

export function Navbar() {
  const [scrolled, setScrolled] = useState(false)
  const [menuOpen, setMenuOpen] = useState(false)

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20)
    window.addEventListener("scroll", onScroll)
    return () => window.removeEventListener("scroll", onScroll)
  }, [])

  return (
    <nav
      className={cn(
        "fixed top-0 left-0 right-0 z-50 flex justify-center transition-[padding] duration-300",
        scrolled ? "pt-4" : "pt-0"
      )}
    >
      <div
        className={cn(
          "relative transition-all duration-300",
          scrolled
            ? "w-[92%] max-w-[1440px] rounded-full bg-[#F5F0E8]/92 backdrop-blur-md border border-[#1C3A2F]/8 shadow-[0_8px_30px_rgba(28,58,47,0.12)]"
            : "w-full max-w-none rounded-none bg-transparent border border-transparent shadow-none"
        )}
      >
        <div className="max-w-[1440px] mx-auto flex items-center justify-between px-[5%] py-5">
          {/* Logo */}
          <Link href="/" className="font-playfair text-xl font-bold text-[#1C3A2F] tracking-tight">
            budget<span className="text-[#C9A84C]">.</span>management
          </Link>

          {/* Desktop links */}
          <ul className="hidden md:flex items-center gap-8 list-none">
            {NAV_LINKS.map((link) => (
              <li key={link.href}>
                <Link
                  href={link.href}
                  className="text-sm text-[#1A1A1A]/70 hover:text-[#1A1A1A] transition-opacity"
                >
                  {link.label}
                </Link>
              </li>
            ))}
            <li>
              <Button
                asChild
                className="rounded-full bg-[#1C3A2F] text-[#F5F0E8] hover:bg-[#2A5242] text-sm font-medium px-5"
              >
                <Link href="/login">Se connecter</Link>
              </Button>
            </li>
          </ul>

          {/* Mobile hamburger */}
          <button
            className="md:hidden text-[#1C3A2F]"
            onClick={() => setMenuOpen(!menuOpen)}
            aria-label="Menu"
          >
            {menuOpen ? <X size={22} /> : <Menu size={22} />}
          </button>
        </div>

        {/* Mobile menu */}
        {menuOpen && (
          <div className="absolute top-full left-0 right-0 bg-[#F5F0E8] rounded-b-2xl border-b border-[#1C3A2F]/10 px-[5%] py-6 flex flex-col gap-4 md:hidden shadow-sm">
            {NAV_LINKS.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className="text-sm text-[#1A1A1A]/70 hover:text-[#1A1A1A] transition-opacity"
                onClick={() => setMenuOpen(false)}
              >
                {link.label}
              </Link>
            ))}
            <Button
              asChild
              className="rounded-full bg-[#1C3A2F] text-[#F5F0E8] hover:bg-[#2A5242] w-fit text-sm font-medium px-5"
            >
              <Link href="/login">Se connecter</Link>
            </Button>
          </div>
        )}
      </div>
    </nav>
  )
}
