/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    unoptimized: true,
    domains: [
      "image.tmdb.org",              // TMDB
      "books.googleusercontent.com", // Google Books covers
      "i.scdn.co",                   // Spotify
    ],
  },
};

export default nextConfig;
