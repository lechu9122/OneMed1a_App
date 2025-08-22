import { Poppins } from "next/font/google";
import "./globals.css";
import Logo from "@/components/Logo";
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
      <body
        className={`${poppins.variable} antialiased bg-white text-neutral-900`}
      >
        <header
          className="py-6 flex justify-center"
          role="banner"
          aria-label="Application Header"
        >
          <Logo />
        </header>

        <main role="main" className="min-h-screen">
          {children}
        </main>
      </body>
    </html>
  );
}

RootLayout.propTypes = {
  children: PropTypes.node.isRequired,
};
