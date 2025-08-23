import { Poppins } from "next/font/google";
import "./globals.css";
import Logo from "@/components/Logo";
import MediaNavigation from "@/components/MediaNavigation";
import PropTypes from "prop-types";

const poppins = Poppins({
  subsets: ["latin"],
  weight: ["400", "500", "600", "700"],
  variable: "--font-poppins",
});

export const metadata = {
  title: "AllMedia",
  description: "A central hub for movies, TV shows, books, and music.",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <head>
        <meta charSet="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
      </head>

      <body className={`${poppins.variable} antialiased bg-white text-neutral-900`}>
        {/* Sticky logo only */}
        <header
          className="sticky top-0 z-50 bg-white/90 backdrop-blur supports-[backdrop-filter]:bg-white/70 border-b border-neutral-200 shadow-sm"
          aria-label="Application Header"
        >
          <div className="mx-auto max-w-7xl px-4 py-4 flex justify-center">
            <Logo />
          </div>
        </header>

        {/* Non-sticky navigation (no extra logo here) */}
        <nav className="bg-white border-b border-neutral-200" aria-label="Media categories">
          <div className="mx-auto max-w-7xl px-4 py-3 flex justify-center">
            <MediaNavigation />
          </div>
        </nav>

        <main id="main" className="min-h-screen">
          {children}
        </main>
      </body>
    </html>
  );
}

RootLayout.propTypes = {
  children: PropTypes.node.isRequired,
};
