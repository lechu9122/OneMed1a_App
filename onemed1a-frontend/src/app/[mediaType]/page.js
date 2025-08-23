import MediaGrid from "@/components/MediaGrid";
import PropTypes from "prop-types";

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
  const { mediaType } = await params;

  // In Next 15+, searchParams is a Promise<URLSearchParams>
  const sp = await searchParams;
  const qRaw = typeof sp?.get === "function" ? sp.get("q") : sp?.q;
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

      <MediaGrid items={filtered} />
    </div>
  );
}

MediaPage.propTypes = {
  params: PropTypes.any,
  searchParams: PropTypes.any,
};
