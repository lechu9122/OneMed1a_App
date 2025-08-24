import MediaGrid from "@/components/MediaGrid";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import PropTypes from "prop-types";

const TMDB_IMG_BASE = "https://image.tmdb.org/t/p/";
const typeMap = {
  movie: "MOVIE",
  tv: "TV",
  music: "MUSIC",
  books: "BOOKS",
};

function pickCover(posterPath, backdropPath, size = "w342") {
  if (posterPath) return `${TMDB_IMG_BASE}${size}${posterPath}`;
  if (backdropPath) return `${TMDB_IMG_BASE}${size}${backdropPath}`;
  return "/next.svg";
}

const toYear = (dateStr) => (dateStr ? Number(String(dateStr).slice(0, 4)) : undefined);

// Local fallback dataset (from frontend/user-ui)
const mediaData = {
  movies: [
    { id: 1, title: "Inception", coverUrl: "/next.svg", year: 2010 },
    { id: 2, title: "Interstellar", coverUrl: "/next.svg", year: 2014 },
  ],
  tv: [
    { id: 1, title: "Breaking Bad", coverUrl: "/next.svg", year: 2008 },
    { id: 2, title: "Dark", coverUrl: "/next.svg", year: 2017 },
  ],
  audio: [
    { id: 1, title: "Abbey Road", coverUrl: "/next.svg", year: 1969 },
    { id: 2, title: "Thriller", coverUrl: "/next.svg", year: 1982 },
  ],
  books: [
    { id: 1, title: "1984", coverUrl: "/next.svg", year: 1949 },
    { id: 2, title: "Brave New World", coverUrl: "/next.svg", year: 1932 },
    { id: 3, title: "Fahrenheit 451", coverUrl: "/next.svg", year: 1953 },
  ],
};

export default async function MediaPage({ params, searchParams }) {
  const { mediaType } = await params; // e.g., "movie" | "tv" | "music" | "books" | "audio"

  // Try to use server API if available
  let apiAvailable = true;
  let getUserMediaByUserId;
  try {
    const mod = await import("@/api/mediaAPI");
    getUserMediaByUserId = mod?.getUserMediaByUserId;
    if (typeof getUserMediaByUserId !== "function") apiAvailable = false;
  } catch {
    apiAvailable = false;
  }

  if (apiAvailable) {
    // Enforce login (from main)
    const cookieStore = await cookies();
    const userId = cookieStore.get("userId")?.value;
    if (!userId) redirect("/");

    let raw = [];
    try {
      raw = (await getUserMediaByUserId(userId)) ?? [];
    } catch (e) {
      console.error("Failed to load user media:", e);
    }

    const items = raw
      .filter((ums) => ums?.media?.type === typeMap[mediaType])
      .map((ums) => ({
        id: ums.media?.mediaId ?? ums.id,
        title: ums.media?.title ?? "",
        coverUrl: pickCover(ums.media?.posterUrl, ums.media?.backdropUrl),
        year: toYear(ums.media?.releaseDate),
        type: ums.media?.type?.toLowerCase(),
        status: ums.status,
      }));

    return (
      <div className="p-4">
        <h1 className="text-2xl font-bold my-4">{String(mediaType).toUpperCase()}</h1>
        <MediaGrid items={items} />
      </div>
    );
  }

  // Fallback: local data + ?q= filter (from frontend/user-ui)
  const sp = searchParams;
  const qRaw =
    sp && typeof sp.get === "function"
      ? sp.get("q")
      : typeof sp === "object"
      ? sp?.q
      : "";
  const q = (qRaw || "").toString().trim().toLowerCase();

  const items = mediaData[mediaType] || [];
  const filtered = q
    ? items.filter((it) => {
        const title = (it.title || "").toLowerCase();
        const year = String(it.year || "");
        return title.includes(q) || year.includes(q);
      })
    : items;

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold my-4">{String(mediaType).toUpperCase()}</h1>
      <MediaGrid items={filtered} />
    </div>
  );
}

MediaPage.propTypes = {
  params: PropTypes.any,
  searchParams: PropTypes.any,
};
