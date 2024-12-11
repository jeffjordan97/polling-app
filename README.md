# Polling Application

This project is a **polling application** that allows users to vote in real-time on a currently active poll and view the active poll results, following their vote. The application consists of a backend server implemented in Java with Spring Boot, a UI implemented in Angular, and a MySQL database. Real-time updates are enabled using Server-Sent Events (SSE).

## Requirements

- Polls can have between 2 and 7 options.
- The website should be responsive.
- Polls and votes should be stored in a database of some sort.
- New polls should be created through an API.
- An API should be available to view individual votes for a given poll and the time the vote was made.
- There should be an example of how you would test front and backend code. There is no need to test the entire code base.
- A README should be provided explaining how to run any code to allow us to test the solution.

## Features

- **Vote in Polls:** Users can view the currently active poll and cast a vote.
- **Real-Time Updates:**
  - New polls are pushed to all clients in real-time.
  - Vote percentage results are updated in real-time as votes are submitted.
- **Vote Persistence:** User votes are saved in the browser's local storage to prevent repeat votes from the same user on the active poll.
- **Backend API:** RESTful endpoints for creating polls, submitting votes, and fetching poll results.
- **Database:** MySQL database to store polls and votes.

## Project Structure

### Backend (Spring Boot)

- **API Endpoints**:

| Endpoint                                | Method | Description                             |
| --------------------------------------- | ------ | --------------------------------------- |
| `/api/polls/active`                     | GET    | Get the currently active poll.          |
| `/api/polls/active/end`                 | PUT    | End the currently active poll.          |
| `/api/polls`                            | POST   | Create a new poll.                      |
| `/api/polls/{id}`                       | GET    | Get a poll by its id.                   |
| `/api/polls/{id}`                       | PUT    | Update a poll by its id.                |
| `/api/polls/{id}`                       | DELETE | Delete a poll by its id.                |
| `/api/polls/name/{name}`                | GET    | Get a poll by its name.                 |
| `/api/polls/all`                        | GET    | Get all polls.                          |
| `/api/votes`                            | POST   | Submit a vote.                          |
| `/api/votes/{pollId}/percentageResults` | GET    | Get vote percentage results for a poll. |
| `/api/polls/updates`                    | SSE    | Endpoint for real-time poll updates.    |
| `/api/votes/updates`                    | SSE    | Endpoint for real-time vote updates.    |

- **Technologies:**
  - Spring Boot
  - MySQL
  - SSE for real-time updates

### Frontend (Angular)

- **Features**:
  - Display the active poll and its options.
  - Real-time UI updates using SSE for new polls and vote results.
  - Submit votes and view percentage results.
  - Prevent duplicate voting by saving the user's vote in local storage.
- **Technologies:**
  - Angular
  - Reactive Forms
  - RxJS for observables
  - Tailored UI without Angular Material

## Getting Started

### Prerequisites

1. **Java 17**
2. **Node.js (v16 or above)** and npm
3. **Angular CLI** (`npm install -g @angular/cli`)
4. **MySQL Server**

### Setting Up the Database

1. **Start MySQL Server**

   - Ensure your MySQL server is running locally.

2. **Create the Database**

   ```sql
   CREATE DATABASE polling_app;
   ```

3. **Update Database Credentials**

   - Update `application.properties` in the Spring Boot project:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/polling_app
     spring.datasource.username=<your_username>
     spring.datasource.password=<your_password>
     ```

4. **Run Database Migrations**
   - The application uses Hibernate to automatically create tables when the server starts.

### Setting Up the Backend

1. **Clone the Repository**

   ```bash
   git clone https://github.com/jeffjordan97/polling-app.git
   cd polling-service
   ```

2. **Build the Project**

   ```bash
   ./mvnw clean install
   ```

3. **Run the Backend**
   ```bash
   ./mvnw spring-boot:run
   ```
   - The backend will start on `http://localhost:8080`.

### Setting Up the Frontend

1. **Navigate to the UI Directory**

   ```bash
   cd polling-ui
   ```

2. **Install Dependencies**

   ```bash
   npm install
   ```

3. **Run the UI**
   ```bash
   ng serve
   ```
   - The UI will be available at `http://localhost:4200`.

### Running the Full Application

1. Start the backend server.
2. Start the frontend server.
3. Open `http://localhost:4200` in your browser to interact with the application.

## Real-Time Features

### Server-Sent Events (SSE)

1. **Active Poll Updates**

   - Backend sends new poll data to `/api/polls/updates` when a new poll is created.
   - Frontend listens to this endpoint to dynamically update the UI.

2. **Vote Percentage Updates**
   - Backend sends vote percentage updates to `/api/votes/updates` when a new vote is added.
   - Frontend listens to this endpoint to update vote results in real-time.

### Angular SSE Implementation

- **Poll Updates:**
  - The `PollService` uses `EventSource` to subscribe to the `/api/polls/updates` endpoint.
  - Updates the UI with the new poll.
- **Vote Updates:**
  - Subscribes to `/api/votes/updates` for the active poll's vote percentages.

### Example Usage

#### Active Poll UI

- Displays the active poll with its options.
- Allows users to select one option and submit their vote.

#### Real-Time Results

- Vote results update dynamically as other users cast their votes.

## Testing

### Backend Tests

1. **Run Tests**

   ```bash
   ./mvnw test
   ```

2. **Test Coverage**
   - PollService and VoteService are tested for core functionalities.

### Frontend Tests

1. **Run Tests**

   ```bash
   ng test
   ```

2. **Unit Tests**
   - PollService and PollVotingComponent are tested with mocked dependencies.

## Future Improvements

- **WebSocket Integration:** Replace SSE for two-way communication if needed.
- **Authentication:** Add JWT-based security for API endpoints.
- **Server-Side Caching:** Implement caching to reduce database calls and improve performance.

## Contributing

1. Fork the repository.
2. Create a new feature branch.
3. Commit and push your changes.
4. Create a pull request.

## Contact

X - [@JeffreyJordan97](https://x.com/JeffreyJordan97) <br />
LinkedIn - [@Jeffrey-Jordan1997](https://www.linkedin.com/in/jeffrey-jordan1997/)
