ChessTracker is a web application that allows users to look up chess players and view detailed statistics such as recent matches and current ratings. The data is fetched from the chess.com public API.

Project Overview
ChessTracker consists of two main components:

Backend – Java & Spring Boot
Built with Java and Spring Boot.

Provides a RESTful API to fetch and process data from the chess.com API.

Handles routing, player data aggregation, and response formatting.

Frontend – SvelteKit
Developed with SvelteKit, offering fast and reactive UI.

Users can search for players and view:

Title, username, and country.

Ratings (Blitz, Rapid, Bullet).

Recent match history and results.

Includes lightweight widgets, ideal for embedding in OBS or other platforms.



Search players by username


View Blitz, Rapid, and Bullet ratings

See recent match results and statistics

Country flag and title support

REST API for data access


Widget mode for stream overlays(to be upgraded)

