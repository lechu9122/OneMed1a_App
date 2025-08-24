import MediaGrid from "@/components/MediaGrid";

const TMDB_IMAGE = "https://image.tmdb.org/t/p/w500";

function mapTmdbMovies(json) {
  const list = Array.isArray(json?.results) ? json.results : [];
  return list.map((m) => ({
    id: `movie-${m.id}`,
    title: m.title ?? m.name ?? "Untitled",
    coverUrl: m.poster_path ? `${TMDB_IMAGE}${m.poster_path}` : "/next.svg", // ✅ full URL here
    year: (m.release_date || m.first_air_date || "").slice(0, 4),
    type: m.title ? "movie" : "tv",
    rating: typeof m.vote_average === "number" ? m.vote_average.toFixed(1) : undefined,
    href: `/movies/${m.id}`,
  }));
}

export default async function Page() {
  const res = await fetch("http://localhost:8080/api/discover/movies", { cache: "no-store" });
  const data = await res.json();
  const items = mapTmdbMovies(data);
  return <MediaGrid items={items} />;
}
