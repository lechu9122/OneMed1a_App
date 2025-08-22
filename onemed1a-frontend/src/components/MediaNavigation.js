"use client";
import Link from "next/link";
import { usePathname } from "next/navigation";

export default function MediaNav() {
  const pathname = usePathname();

  const tabs = [
    { name: "Movies", href: "/movies" },
    { name: "TV", href: "/tv" },
    { name: "Books", href: "/books" },
    { name: "Games", href: "/games" },
    { name: "Audio", href: "/audio" },
  ];

  return (
    <nav className="flex gap-3" role="navigation" aria-label="Media categories">
      {tabs.map((t) => {
        const active = pathname?.startsWith(t.href);
        return (
          <Link
            key={t.name}
            href={t.href}
            aria-current={active ? "page" : undefined}
            className={`px-4 py-1.5 text-sm font-semibold uppercase tracking-wide rounded-full transition-colors
              ${active ? "bg-blue-600 text-white" : "bg-red-600 text-white hover:bg-red-700"}`}
          >
            {t.name}
          </Link>
        );
      })}
    </nav>
  );
}
