// app/layout.js
import { Poppins } from "next/font/google";
import "./globals.css";
import Logo from "@/components/Logo";
import MediaNav from "@/components/MediaNav";
import PropTypes from "prop-types";

// Load font with selected weights
const poppins = Poppins({
  subsets: ["latin"],
  weight: ["400", "500", "600", "700"],
  variable: "--font-poppins",
});

// Application metadata for SEO and accessibility
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
        {/* Sticky header: logo + nav */}
        <header
          className="sticky top-0 z-50 bg-white/90 backdrop-blur supports-[backdrop-filter]:bg-white/70 border-b border-neutral-200 shadow-sm"
          role="banner"
          aria-label="Application Header"
        >
          <div className="mx-auto max-w-7xl px-4 py-4 flex justify-center">
            <Logo />
          </div>

          {/* Sticky nav row under the logo */}
          <div className="mx-auto max-w-7xl px-4 pb-3 flex justify-center">
            <MediaNav />
          </div>
        </header>

        <main id="main" role="main" className="min-h-screen">
          {children}
        </main>
      </body>
    </html>
  );
}

RootLayout.propTypes = {
  children: PropTypes.node.isRequired,
};
