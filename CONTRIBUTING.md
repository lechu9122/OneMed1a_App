# Contributing to `OneMed1a`

Thank you for your interest in contributing to `OneMed1a`.
This guide will help you understand how to get involved, submit your work, and collaborate effectively.

> Please also read our [README.md](README.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) before contributing.

## Filing a Bug Report
If you find a bug:
1. **Search** existing [issues](https://github.com/SOFTENG-310-OneMed1a/OneMed1a_App/issues) to avoid duplicates.
2. If it’s new, **open a Bug issue** following the [bug report template](.github/ISSUE_TEMPLATE/bug_report.md).

## Suggesting a New Feature
1. **Check** existing [feature requests](https://github.com/SOFTENG-310-OneMed1a/OneMed1a_App/issues?q=is%3Aissue%20state%3Aopen%20type%3AFeature).
2. If not found, open a **Feature Request issue** following the [feature request template](.github/ISSUE_TEMPLATE/feature_request.md).

## Submitting a Pull Request
1. **Check** existing [pull requests](https://github.com/SOFTENG-310-OneMed1a/OneMed1a_App/pulls) to avoid duplicates.
2. If not found:
    - First, make a fork of the main repository
  
    - After you have made a fork, clone it to your local machine then set upstream to the main repository
      ```shell
      git remote add upstream https://github.com/ORIGINAL_OWNER/REPO_NAME.git
      ```
    - Make your changes in a new git branch:
      ```shell
      git checkout -b my-fix-branch main
      ```
    - Make sure to test your code and ensure it works and nothing else breaks.
  
    - **Before you commit any changes, make sure not to include the API keys in application.properties**
   
    - Commit your changes with a descriptive commit message:
  
      ```shell
      git commit -am "commit message"
      ```

    - Push your changes to GitHub:

      ```shell
      git push origin my-fix-branch
      ```

    - In GitHub, send a pull request to `OneMed1a:main`.
  
    - When creating a pull request, make sure to include a short descriptive title and reference the issue it is addressing in the body.

## Environment set up and running tests
   ### Backend setup
   - Install Java version 21
   - Install Maven
   - Install Docker (container for postgres)

   ### Frontend setup
   - Install Node.js version 18 +

   ### IDE Extensions
   - Install SonarQube extension

   ### API Keys
   - Make sure to put all the API keys in onemed1a-backend/src/main/resources/application.properties

   Once you have installed all the required software, first run the database through docker in the root folder

   ```shell
      docker compose up -d
   ```

   After you have the database running on docker, run the sprintboot backend

   ```shell
      cd onemed1a-backend
      mvn spring-boot:run
   ```

   Once you have all the backend running switch to the frontend server and run the frontend
   
   ```shell
      cd onemed1a-frontend
      npm install
      npm run dev
   ```

   To run backend tests switch to the backend folder, then run the following command

   ```shell
      cd onemed1a-backend
      mvn test
   ```
  
## Contributions we welcome
### We are looking for:
  - Bug fixes
  - New features aligned with our project goals
  - Documentation
  - Tests
  - Refactoring for readability and maintainability
  - Design and UX improvements
### We aren't looking for:
  - Features outside of our project scope
  - Large architectural changes without an RFC/discussion
  - Anything violating our [Code of Conduct](CODE_OF_CONDUCT.md)

## How newcomers can get started
Newcomers can get started by heading over to [issues](https://github.com/SOFTENG-310-OneMed1a/OneMed1a_App/issues?q=is%3Aissue%20state%3Aopen%20label%3A%22good%20first%20issue%22)
and start working on any issues labeled with "good first issue".

## Technical requirements for contributors
### Frontend:
  - Next JS

### Backend:
  - Java SpringBoot

## Project vision & roadmap
`OneMed1a` aims to develop a unified web application that aggregates movies, TV shows, books,
and podcasts into a single discovery hub. By ingesting each user’s ratings, reviews and
wish-lists, alongside their friends’ activity, the system will generate context-aware, cross-media
recommendations (e.g. “you just finished that novel; here’s its film adaptation or a related
podcast”). A consolidated activity feed will let users track, share and save everything in one
place, transforming fragmented media browsing into a cohesive, socially driven experience.

### Our current roadmap includes:
1. Area to track media you have watched
   - Movies
   - TV Shows
   - Music
   - Books / Ebooks
2. Recommendation features:
   - Cross-media recommendation features using user’s taste from their data.
3. Tracking of watched/watching media
   - Completed, watching, plan to watch
4. Creating accounts
   - Allows users to save their accounts and save their data
5. Friends feature:
   - Add friends
   - View friend’s rating
   - View friend’s analytics (number of movies/media watched)
   - Recommends to you the friend’s top picks
6. Recommendation Features
   - Modify recommendation implementation to consider mood
7. Blend Mode (if time allows)
   - Choose a friend, combine and compare data and return a compatibility score
   - Scoreboard to display compatibility scores

## High-level design
### Frontend
  - Next JS
### Backend
  - Java SpringBoot
### Database
  - Postgres (containerized through Docker)
### User authentication
  - Up to whoever is implementing's choice
### APIs
  - OpenAI
  - TMDB
  - Spotify web API
  - Google Books API

## Project ground rules
To ensure consistency throughout the source code, keep the following rules while you are working:
  - All features or bug fixes **Must be tested** by one or more tests.
  - All classes and methods **Must be documented**.
  - Follow the [code of conduct](CODE_OF_CONDUCT.md).
  - NEVER commit API keys in application.properties.

## How contributors should get in touch
Join the discord: https://discord.gg/rsDuRvQuPN
    
