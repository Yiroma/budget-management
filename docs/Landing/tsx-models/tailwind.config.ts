import type { Config } from "tailwindcss"

const config: Config = {
  darkMode: ["class"],
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        playfair: ["var(--font-playfair)", "serif"],
        sans: ["var(--font-dm-sans)", "sans-serif"],
      },
      colors: {
        forest: {
          DEFAULT: "#1C3A2F",
          light: "#2A5242",
        },
        sand: {
          DEFAULT: "#F5F0E8",
          dark: "#EDE6D6",
        },
        gold: {
          DEFAULT: "#C9A84C",
          light: "#E2C97E",
        },
      },
      keyframes: {
        "fade-up": {
          from: { opacity: "0", transform: "translateY(24px)" },
          to: { opacity: "1", transform: "translateY(0)" },
        },
      },
      animation: {
        "fade-up": "fade-up 0.8s ease both",
      },
    },
  },
  plugins: [require("tailwindcss-animate")],
}

export default config
