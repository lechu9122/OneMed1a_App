// app/music/[id]/page.js
import {notFound} from "next/navigation";
import BackgroundImage from "@/app/media-details-components/BackgroundImage";
import PosterImage from "@/app/media-details-components/PosterImage";
import StarRating from "@/app/media-details-components/StarRating";

import Divider from "@/app/media-details-components/Divider";
import {getMediaById} from "@/api/mediaClient";
import {cookies} from "next/headers";
import CollectionDropdown from "@/app/media-details-components/CollectionDropdown";
import {getStatus} from "@/api/mediaAPI";

async function getAlbum(id) {
    try {
        return await getMediaById(id);
    } catch (error) {
        console.error("Error fetching song:", error);
        return null;
    }
}

async function getMediaStatus(userId, mediaId) {
    try {
        return await getStatus(userId, mediaId);
    } catch (error) {
        console.log("Not in collection");
        return null;
    }
}

export default async function MusicPage({ params }) {
    const { id } = await params;
    const userId = (await cookies()).get('userId')?.value;

    const album = await getAlbum(id);
    if (!album) notFound();
    const result = await getMediaStatus(userId, id);

    return (
        <main className="min-h-screen bg-gray-100 text-gray-900">
            <BackgroundImage src={album.backdropUrl} alt={`${album.title} backdrop`} />

            <div className="mx-auto w-full max-w-6xl px-4 pb-20">
                {/* Back button */}
                <div className="pt-8 mb-8">
                    <a
                        href="/music"
                        className="inline-flex items-center gap-2 text-gray-800 hover:text-gray-600"
                    >
                        <span className="text-2xl">←</span>
                    </a>
                </div>

                {/* Main content */}
                <div className="flex flex-col lg:flex-row gap-8">
                    {/* Artwork */}
                    <div className="flex-shrink-0 lg:w-80">
                        <PosterImage
                            src={album.posterUrl}
                            alt={`${album.title} cover`}
                            className="w-full lg:w-80 rounded-lg"
                        />
                    </div>

                    {/* Content */}
                    <div className="flex-1">
                        {/* Title and basic info */}
                        <div className="mb-6">
                            <h1 className="text-4xl font-bold mb-2 text-gray-900">{album.title}</h1>

                            <div className="text-gray-600 mb-3">
                                {/* Artists */}
                                {album.artists?.length ? (
                                    <div className="text-lg">{album.artists.join(", ")}</div>
                                ) : null}

                                {/* Meta row */}
                                <div className="flex flex-wrap items-center gap-4 text-sm">
                                    {album.releaseDate && <span>{album.releaseDate}</span>}
                                    {album.duration && <span>• {album.duration}</span>}
                                    {album.label && <span>• Label: {album.label}</span>}
                                </div>
                            </div>

                            {/* Genre pills */}
                            <div className="flex flex-wrap gap-2 mb-6">
                                {(album.genres || []).map((genre) => (
                                    <span
                                        key={genre}
                                        className="bg-blue-600 text-white px-3 py-1 rounded text-sm font-medium"
                                    >
                    {genre}
                  </span>
                                ))}
                            </div>
                        </div>

                        {/* Description - hidden on mobile, shown on desktop */}
                        <div className="hidden lg:block">
                            <p className="text-gray-700 leading-relaxed">
                                {album.description || "No description available."}
                            </p>
                        </div>
                    </div>
                </div>

                {/* Description - mobile */}
                <div className="lg:hidden mt-8">
                    <p className="text-gray-700 leading-relaxed">
                        {album.description || "No description available."}
                    </p>
                </div>

                {/* Track list */}
                <div className="mt-8">
                    <h3 className="mb-3 text-sm font-semibold uppercase tracking-wide text-gray-600">
                        Track List
                    </h3>
                    <ol className="divide-y divide-gray-200 rounded-lg bg-white ring-1 ring-gray-200">
                        {(album.tracks || []).map((t) => (
                            <li
                                key={t.no}
                                className="flex items-center justify-between px-4 py-3 text-sm"
                            >
                <span className="text-gray-900">
                  {t.no}. {t.title}
                </span>
                                <span className="text-gray-500">{t.length}</span>
                            </li>
                        ))}
                    </ol>
                </div>

                <Divider />

                <StarRating value={4} />

                <Divider />

                <div className="mt-4">
                    <CollectionDropdown
                        currentStatus={result === null ? "UNSPECIFIED" : result.status}
                        userId={userId}
                        mediaId={album.mediaId}
                        mediaType={album.type}
                        />
                        </div>
                      </div>
        </main>
    );
}