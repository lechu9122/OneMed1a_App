"use client";

import Link from "next/link";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useCallback, useEffect, useState } from "react";
import SearchBar from "@/components/SearchBar";

export default function MediaNavigation() {
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();

  // Tabs from both branches (Movies at /movies vs /movie handled below)
  const tabs = [
    { name: "Movies", href: "/movies" }, // prefer plural, support singular in active check
    { name: "TV", href: "/tv" },
    { name: "Books", href: "/books" },
    { name: "Audio", href: "/audio" },
  ];

  // Right-side tab from `main`
  const rightTab = { name: "Recommendations", href: "/recommendations" };

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
      <nav
        className="w-full flex items-center justify-between"
        role="navigation"
        aria-label="Media categories"
      >
        {/* Left spacer to center tabs */}
        <div className="flex-1" />

        {/* Center tabs (keeps main's centered layout) */}
        <div className="flex flex-wrap justify-center gap-3">
          {tabs.map((t) => {
            // Active when on its href OR for Movies also support /movie (from main)
            const isMovies = t.name === "Movies";
            const active =
              pathname?.startsWith(t.href) ||
              (isMovies && pathname?.startsWith("/movie"));
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
        </div>

        {/* Right tab (from main) */}
        <div className="flex-1 flex justify-end">
          <Link
            href={rightTab.href + (q ? `?q=${encodeURIComponent(q)}` : "")}
            aria-current={pathname?.startsWith(rightTab.href) ? "page" : undefined}
            className={`px-4 py-1.5 text-sm font-semibold uppercase tracking-wide rounded-full transition-colors
              ${pathname?.startsWith(rightTab.href) ? "bg-blue-600 text-white" : "bg-red-600 text-white hover:bg-red-700"}`}
          >
            {rightTab.name}
          </Link>
        </div>
      </nav>

      {/* Inline search form (from frontend/user-ui) */}
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

      {/* Keep SearchBar component from main as well (no functionality removed) */}
      <SearchBar />
    </div>
  );
}
