import { Poppins } from "next/font/google";
import Link from "next/link";

const poppins = Poppins({
  subsets: ["latin"],
  weight: ["700"],
});

export default function Logo() {
  return (
    <Link
      href="/"
      className={`${poppins.className} text-5xl font-bold tracking-tight text-red-600 hover:text-blue-600 transition-colors`}
    >
      allmedia
    </Link>
  );
}
