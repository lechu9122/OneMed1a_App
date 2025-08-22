"use client";

import { useEffect, useMemo, useRef, useState } from "react";
import Image from "next/image";
import Link from "next/link";
import PropTypes from "prop-types";

const DEFAULT_PAGE_SIZE = 40;
const OBS_ROOT_MARGIN = "800px 0px";

/** Compute cols from width (no matchMedia -> no deprecated listeners) */
function colsFromWidth(w) {
  if (w >= 1280) return 6;
  if (w >= 1024) return 5;
  if (w >= 768) return 4;
  if (w >= 640) return 3;
  return 2;
}

export default function MediaGrid({ items, pageSize = DEFAULT_PAGE_SIZE }) {
  const data = Array.isArray(items) ? items : [];
  const [visibleCount, setVisibleCount] = useState(Math.min(pageSize, data.length));
  const [cols, setCols] = useState(2);
  const sentinelRef = useRef(null);

  // Infinite scroll
  useEffect(() => {
    const el = sentinelRef.current;
    if (!el || typeof window === "undefined" || !("IntersectionObserver" in window)) return;
    const io = new IntersectionObserver(
      ([entry]) => {
        if (!entry.isIntersecting) return;
        setVisibleCount((prev) =>
          prev >= data.length ? prev : Math.min(prev + pageSize, data.length)
        );
      },
      { rootMargin: OBS_ROOT_MARGIN }
    );
    io.observe(el);
    return () => io.disconnect();
  }, [data.length, pageSize]);

  // Column count from window width (no addListener/removeListener)
  useEffect(() => {
    if (typeof window === "undefined") return;
    const update = () => setCols(colsFromWidth(window.innerWidth));
    update();
    let t = null;
    const onResize = () => {
      clearTimeout(t);
      t = setTimeout(update, 100);
    };
    window.addEventListener("resize", onResize);
    return () => {
      clearTimeout(t);
      window.removeEventListener("resize", onResize);
    };
  }, []);

  // Distribute by row (i % cols), then render as column stacks
  const columns = useMemo(() => {
    const buckets = Array.from({ length: cols }, () => []);
    const count = Math.min(visibleCount, data.length);
    for (let i = 0; i < count; i += 1) {
      const it = data[i];
      if (it) buckets[i % cols].push(it);
    }
    return buckets;
  }, [data, visibleCount, cols]);

  const gridCols =
    "grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4";

  return (
    <>
      <div className={gridCols}>
        {columns.map((col, ci) => {
          const firstId = col[0]?.id ?? "none";
          const lastId = col[col.length - 1]?.id ?? "none";
          const colKey = `col-${ci}-${firstId}-${lastId}-${col.length}`;
          return (
            <div key={colKey} className="flex flex-col gap-4">
              {col.map((item) => (
                <Card key={item.id} item={item} />
              ))}
            </div>
          );
        })}
      </div>
      <div ref={sentinelRef} />
    </>
  );
}

function Card({ item }) {
  const [loaded, setLoaded] = useState(false);
  const [src, setSrc] = useState(item.coverUrl || "/placeholder.png");

  const isHrefValid = typeof item.href === "string" && item.href.trim().length > 0;

  const wrapperClasses =
    "block min-h-[44px] min-w-[44px] overflow-hidden group " +
    "rounded-xl bg-[color:var(--card)] text-[color:var(--card-foreground)] " +
    "shadow-xl shadow-[color:var(--shadow,rgba(0,0,0,.35))] transition " +
    "hover:shadow-2xl hover:shadow-[color:var(--shadow,rgba(0,0,0,.55))] " +
    "focus:outline-none focus-visible:ring-2 " +
    "focus-visible:ring-[color:var(--ring,#2563eb)] " +
    "focus-visible:ring-offset-2 focus-visible:ring-offset-[color:var(--ring-offset,transparent)] " +
    "active:scale-[0.99] motion-safe:transition-transform";

  const content = (
    <>
      <div className="relative w-full">
        <div className="aspect-[2/3] w-full overflow-hidden rounded-t-xl">
          {!loaded && (
            <div className="h-full w-full animate-pulse bg-[color:var(--skeleton,#e5e7eb)]" aria-hidden="true" />
          )}
          <Image
            src={src}
            alt="" /* decorative; accessible label lives on wrapper */
            width={400}
            height={600}
            className={`h-full w-full object-cover ${loaded ? "block" : "hidden"}`}
            sizes="(max-width: 640px) 50vw, (max-width: 1024px) 33vw, 16vw"
            onLoadingComplete={() => setLoaded(true)}
            onError={() => setSrc("/placeholder.png")}
            priority={false}
          />
        </div>

        {/* Hover/focus overlay with white text */}
        <div
          className="
            pointer-events-none absolute inset-x-0 bottom-0
            translate-y-1 opacity-0 transition
            group-hover:opacity-100 group-hover:translate-y-0
            group-focus-within:opacity-100 group-focus-within:translate-y-0
          "
          aria-hidden="true"
        >
          <div className="from-black/70 via-black/40 to-transparent bg-gradient-to-t px-4 pt-6 pb-3">
            <div className="text-white">
              <h3 className="text-sm font-semibold leading-snug line-clamp-2">{item.title}</h3>
              {(item.year || item.type || item.rating) && (
                <p className="mt-1 text-xs leading-5 text-gray-200 truncate">
                  {[item.year, item.type, item.rating].filter(Boolean).join(" • ")}
                </p>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* spacing; content lives in overlay */}
      <div className="p-4 pt-3">
        <span className="sr-only">
          {item.title} {item.year ? `(${item.year})` : ""}
        </span>
      </div>
    </>
  );

  return isHrefValid ? (
    <Link href={item.href} aria-label={`${item.title}${item.year ? ` (${item.year})` : ""}`} className={wrapperClasses}>
      {content}
    </Link>
  ) : (
    <button type="button" aria-label={item.title} className={wrapperClasses}>
      {content}
    </button>
  );
}

Card.propTypes = {
  item: PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    coverUrl: PropTypes.string,
    title: PropTypes.string.isRequired,
    year: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    type: PropTypes.string,
    rating: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    href: PropTypes.string,
  }).isRequired,
};

MediaGrid.propTypes = {
  items: PropTypes.arrayOf(Card.propTypes.item).isRequired,
  pageSize: PropTypes.number,
};

MediaGrid.defaultProps = {
  pageSize: DEFAULT_PAGE_SIZE,
};
