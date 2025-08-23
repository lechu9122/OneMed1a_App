"use client";

import Link from "next/link";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useCallback, useState, useEffect } from "react";

export default function MediaNavigation() {
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();

  const tabs = [
    { name: "Movies", href: "/movies" },
    { name: "TV", href: "/tv" },
    { name: "Books", href: "/books" },
    { name: "Audio", href: "/audio" },
  ];

  // Restore q from URL so the input reflects current filter
  const initialQ = searchParams?.get("q") ?? "";
  const [q, setQ] = useState(initialQ);

  useEffect(() => {
    // keep input in sync if URL changes externally
    setQ(searchParams?.get("q") ?? "");
  }, [searchParams]);

  const onSubmit = useCallback(
    (e) => {
      e.preventDefault();
      const params = new URLSearchParams(searchParams?.toString() || "");
      if (q?.trim()) params.set("q", q.trim());
      else params.delete("q");
      router.push(`${pathname}?${params.toString()}`);
    },
    [q, pathname, router, searchParams]
  );

  return (
    <div className="w-full flex flex-col items-center gap-3">
      <nav className="flex flex-wrap justify-center gap-3" role="navigation" aria-label="Media categories">
        {tabs.map((t) => {
          const active = pathname?.startsWith(t.href);
          return (
            <Link
              key={t.name}
              href={t.href + (q ? `?q=${encodeURIComponent(q)}` : "")}
              aria-current={active ? "page" : undefined}
              className={`px-4 py-1.5 text-sm font-semibold uppercase tracking-wide rounded-full transition-colors
                ${active ? "bg-blue-600 text-white" : "bg-red-600 text-white hover:bg-red-700"}`}
            >
              {t.name}
            </Link>
          );
        })}
      </nav>

      {/* Search bar */}
      <form onSubmit={onSubmit} className="w-full max-w-xl flex items-center gap-2">
        <input
          type="search"
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Search titles…"
          className="flex-1 rounded-full border border-neutral-300 px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-blue-600 focus:border-blue-600"
          aria-label="Search titles"
        />
        <button
          type="submit"
          className="px-4 py-2 rounded-full bg-blue-600 text-white text-sm font-semibold hover:bg-blue-700 transition-colors"
        >
          Search
        </button>
      </form>
    </div>
  );
}
