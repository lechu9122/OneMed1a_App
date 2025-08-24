# OneMed1a - Unified Cross-Media Discovery App

> Your personalized hub for movies, books, TV shows, music, and podcasts—powered by recommendations and social discovery.

---

## What does this project do?

This project is developed by **Team 32** as part of the **University of Auckland SOFTENG 310 (Software Engineering Project)** course.
**OneMed1a** is a social and entertainment tracking web application that unifies users’ favorite media—movies, TV shows, books, and music—into one platform.

It enables users to:
- Track what they are watching, reading, or listening to
- Generate intelligent, cross-media recommendations (e.g., “you read this book, here’s the movie adaptation”)
- Connect with friends through a shared media activity feed
- Explore mood-aware and socially driven suggestions

---

## Why is this project useful?

Fragmented media platforms make it difficult to manage all the content people consume. **OneMed1a** addresses this by:
- Centralizing all media tracking in one place
- Recommending media across multiple domains (books to films, music to related shows, etc.)
- Offering a social feed of media activity among friends
- Suggesting top picks based on shared interests and friends’ ratings

This project also supports the SOFTENG 310 learning outcomes:
- Collaborative software development with GitHub
- Clear documentation and workflows
- Building maintainable and extensible software systems

---

## How do I get started?

### Prerequisites
Install the following before setup:
- [Node.js](https://nodejs.org/) (v18+)
- [Java 21+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/) (for containerized PostgreSQL)
- [Git](https://git-scm.com/)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/SOFTENG-310-OneMed1a/OneMed1a_App.git
   cd OneMed1a_App
   ```

2. **Run the database in Docker**
   ```bash
   docker compose up -d
   ```

3. **Run the backend (Spring Boot)**
   ```bash
   cd onemed1a-backend
   mvn spring-boot:run
   ```

4. **Run the frontend (Next.js)**
   ```bash
   cd ../onemed1a-frontend
   npm install
   npm run dev
   ```

5. **Run backend tests**
   ```bash
   cd onemed1a-backend
   mvn test
   ```


## Technologies Used

Frontend: Next.js

Backend: Java Spring Boot

Database: PostgreSQL (containerized through Docker)

APIs:

TMDB (movies & TV shows, posters/backdrops)

Google Books API (book metadata & covers)

Spotify Web API (music & album covers)

## License

This project is licensed under the MIT License.
You are free to use, modify, and distribute with proper attribution.
See the LICENSE file for details.

## Help & Support

For support or project-related discussions, join our Discord:
OneMed1a Discord

## Team

Developed by Team 32 — SOFTENG 310, University of Auckland

**Members:**

Arnav Bhatiani

Dave Khadka

Joe Nguyen

Jake Kim

Leo Chu

Harry Ma

***"One platform. All media. Shared together." — OneMed1a Team***