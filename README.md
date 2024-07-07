# Cinemate backend
Java Spring Boot application serving as the backend for a cinema application (frontend - https://github.com/kkkont/cinemate-frontend).
It offers a range of endpoints for managing movie data, schedules, seats, user history, and seat occupancy updates. The backend includes an algorithm for seat recommendations and integrates with external APIs to fetch movie data.
## Technologies Used
- Java Spring Boot
- H2 Database

## Features
- **Database Schema**: The application features tables for movie schedules, movies, user history, seats and genres.
- **RESTful Endpoints**: Provides endpoints for CRUD operations on movies, schedules, seats and user history.
- **Seat Recommendation Algorithm**: Implements an algorithm to recommend seats based on user preferences and seat availability.
- **External API Integration**: Uses the OMDb API to enrich movie data with external details such as ratings, plot summaries, and cast information.

## Setup 
1. Clone the repository
```
git clone https://github.com/kkkont/cinemate-backend/
```
2. Run the main application
  - Navigate to the root directory of the project.
  - Run the main application class CinemateApplication.java located in the src/main/java/com/proovitoo/cinemate directory.
