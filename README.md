# OneMed1a - Unified Cross-Media Discovery App

> Your personalized hub for movies, books, TV shows, music, and podcasts—powered by recommendations and social discovery.

---

## What does this project do?

**OneMed1a** is a social media and entertainment tracking web application that brings together users’ favorite forms of media—movies, TV shows, books, music, and podcasts—into a single, personalized discovery platform. It tracks what you're watching or reading, recommends what to enjoy next, and lets you share your activity with friends in real-time.

---

## Why is this project useful?

Fragmented media platforms make it hard to keep track of everything we enjoy. OneMed1a solves this by:
- Centralizing all media tracking in one place
- Generating intelligent, mood-aware recommendations across different types of media
- Creating a social feed of your and your friends’ media activity
- Recommending content based on friends' top picks and shared interests

Whether you're a casual consumer or a media lover, OneMed1a helps you discover more of what you love—and connect with others who love it too.

---

## How do I get started?

### Prerequisites

Before running the project locally, install the following:

- [Node.js](https://nodejs.org/) (v18+)
- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Supabase CLI](https://supabase.com/docs/guides/cli)
- [Git](https://git-scm.com/)

### Installation Steps

1. **Clone the repository**
git clone https://github.com/SOFTENG-310-OneMed1a/OneMed1a_App.git
cd OneMed1a_App

2. **Set up the frontend (Next.js)**
cd frontend
npm install
npm run dev


3. **Set up the backend (Spring Boot)**
cd ../backend
./mvnw spring-boot:run

## APIs Used

OneMed1a integrates with the following third-party APIs for media data and images:

- TV Shows & Movies Posters → [TMDB API](https://www.themoviedb.org/documentation/api)
- Book Covers → [Google Books API](https://developers.google.com/books)
- Music Covers → [Spotify Web API](https://developer.spotify.com/documentation/web-api/)

API keys should be configured in:
- Frontend (Next.js) → `.env.local`
- Backend (Spring Boot) → `application.properties`

---

## License

This project is licensed under the MIT License.
You're free to use, modify, and distribute the project with proper attribution.

View the full license in [LICENSE](LICENSE).

---

## Versions

We follow Semantic Versioning.

- Current Version: `v1.0.0-alpha`
- Releases are documented in the Releases section.

---

## Where can I get more help?

If you encounter any issues or have questions:
- Check the Issues tab
- Open a new issue
- Start a discussion in GitHub Discussions

---

## Team Members

- Arnav Bhatiani
- Dave Khadka
- Joe Nguyen
- Jake Kim
- Leo Chu
- Harry Ma

---

## Technologies Used

- Frontend: Next.js
- Backend: Spring Boot, Supabase
- Database: PostgreSQL via Supabase
- Version Control: GitHub
- Project Management: Jira, Trello

---

"One platform. All media. Shared together."
— OneMed1a Team