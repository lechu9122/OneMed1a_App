import MediaGrid from "@/components/MediaGrid";
import { getUserMediaByUserId } from "@/api/mediaAPI";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export const dynamic = "force-dynamic";
export const revalidate = 0;

const TMDB_IMG_BASE = "https://image.tmdb.org/t/p/";

// Accept singular and plural paths
const normalizeTypeKey = (t = "") => {
  const key = String(t).toLowerCase();
  if (key === "movies") return "movie";
  if (key === "tvs" || key === "shows") return "tv";
  if (key === "audios") return "audio";
  if (key === "book") return "books";
  return key;
};

// Map route key -> backend media type
const typeMap = {
  movie: "MOVIE",
  movies: "MOVIE",
  tv: "TV",
  shows: "TV",
  music: "MUSIC",
  audio: "MUSIC",
  books: "BOOKS",
};

const toYear = (dateStr) => (dateStr ? Number(String(dateStr).slice(0, 4)) : undefined);

function isFullUrl(value) {
  return typeof value === "string" && /^https?:\/\//i.test(value);
}
function withSize(path, size) {
  if (!path) return null;
  if (isFullUrl(path)) return path;
  const p = String(path).startsWith("/") ? String(path) : `/${path}`;
  return `${TMDB_IMG_BASE}${size}${p}`;
}
function pickCover(posterPath, backdropPath, posterSize = "w342", backdropSize = "w780") {
  return withSize(posterPath, posterSize) || withSize(backdropPath, backdropSize) || "/next.svg";
}

// Fallback mappers for discover endpoints
function mapDiscoverMovies(json) {
  const list = Array.isArray(json?.results) ? json.results : [];
  return list.map((m) => ({
    id: m.id,
    title: m.title ?? "Untitled",
    type: "movie",
    year: (m.release_date || "").slice(0, 4) || undefined,
    rating: typeof m.vote_average === "number" ? m.vote_average.toFixed(1) : undefined,
    coverUrl: pickCover(m.poster_path, m.backdrop_path),
    href: `/collection/movie/${m.id}`,
  }));
}
function mapDiscoverTV(json) {
  const list = Array.isArray(json?.results) ? json.results : [];
  return list.map((m) => ({
    id: m.id,
    title: m.name ?? "Untitled",
    type: "tv",
    year: (m.first_air_date || "").slice(0, 4) || undefined,
    rating: typeof m.vote_average === "number" ? m.vote_average.toFixed(1) : undefined,
    coverUrl: pickCover(m.poster_path, m.backdrop_path),
    href: `/collection/tv/${m.id}`,
  }));
}

/**
 * @param {{ params: { id: string } }} props
 */
export default async function MediaPage({ params }) {
  const { mediaType: rawMediaType } = params;   // no `await` here!
  const mediaTypeKey = normalizeTypeKey(rawMediaType);
  const wantedType = typeMap[mediaTypeKey];

  const cookieStore = await cookies();
  const userId = cookieStore.get("userId")?.value;
  if (!userId) redirect("/");

  let raw = [];
  try {
    raw = (await getUserMediaByUserId(userId)) ?? [];
  } catch (e) {
    console.error("Failed to load user media:", e);
  }

  // 1) Try the user's library first
  let items = raw
    .filter((ums) => ums?.media?.type === wantedType)
    .map((ums) => {
      const m = ums.media ?? {};
      return {
        id: m.mediaId ?? ums.id,
        title: m.title ?? "",
        coverUrl: pickCover(m.posterUrl, m.backdropUrl),
        year: toYear(m.releaseDate),
        type: (m.type || "").toLowerCase(),
        status: ums.status,
        href: `/collection/${(m.type || "").toLowerCase()}/${m.mediaId ?? ums.id}`,
      };
    });

  // 2) If empty, fallback to discover so the page always shows posters
  if (items.length === 0 && (mediaTypeKey === "movie" || mediaTypeKey === "movies" || mediaTypeKey === "tv")) {
    try {
      const base = process.env.NEXT_PUBLIC_API_BASE || "http://localhost:8080";
      const url =
        mediaTypeKey === "tv"
          ? `${base}/api/discover/tv`
          : `${base}/api/discover/movies`;

      const res = await fetch(url, { cache: "no-store" });
      if (res.ok) {
        const data = await res.json();
        items = mediaTypeKey === "tv" ? mapDiscoverTV(data) : mapDiscoverMovies(data);
      } else {
        console.error("Discover fetch failed:", res.status, await res.text());
      }
    } catch (e) {
      console.error("Discover fetch error:", e);
    }
  }

  // Optional: log a few URLs to verify
  // console.log("cover samples:", items.slice(0, 3).map(i => i.coverUrl));

  return (
    <div className="p-4">
      <MediaGrid items={items} />
    </div>
  );
}
