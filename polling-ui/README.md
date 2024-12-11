# Polling UI

This project is a **Polling Application** built with **Angular UI**. It allows users to vote on the currently active poll and view the poll results following their vote.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 18.1.4.

---

## Features

- **Vote on Active Poll**: Presents the currently active poll with options for users to vote on.
- **Vote Results**: See voting percentage results for each option, following the user vote.
- **Cached Vote**: User vote is cached in the browser's local storage. If the page is reloaded, the user is unable to submit a vote on the same poll again.
- **Responsive UI**: User-friendly and responsive interface.

---

## Prerequisites

Ensure the following are installed on your local machine:

- [Node.js 16+](https://nodejs.org/en/download/)
- [Angular CLI](https://angular.io/cli) (`npm install -g @angular/cli`)

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/real-time-polling-app-ui.git
   cd real-time-polling-app-ui
   ```
2. Install dependencies
   ```bash
   npm install
   ```
3. Ensure the backend service is running on http://localhost:8080.

---

## Running the Application

1. Start the Angular development server:
   ```bash
   ng serve
   ```
2. Open your browser and navigate to:
   ```markdown
   http://localhost:4200
   ```

---

## Configuration

The UI communicates with the backend service running at http://localhost:8080. If the backend URL changes, update the `baseUrl` in the environment file:

`src/environments/environment.ts:`

```typescript
export const environment = {
  production: false,
  baseUrl: "http://localhost:8080",
};
```

---

## Project Structure

```
public/
└── assets/                 # Static assets (CSS, images, etc.)
src/
├── app/
│   ├── service/           # Angular services for backend communication
│   ├── models/             # Type definitions for Poll and Vote
│   └── poll-voting/        # Main component for voting UI
└── environments/           # Environment-specific configurations

```

---

## Testing

1. Run unit tests:
   ```bash
   ng test
   ```

## API Integration

| Endpoint                               | Method | Description                                |
| -------------------------------------- | ------ | ------------------------------------------ |
| /api/polls/active                      | GET    | Retrieve the currently active poll         |
| /api/votes                             | POST   | Submit a vote for a poll                   |
| /api/votes/${pollId}/percentageResults | GET    | Retrieve the percentage results for a poll |

---

## Roadmap

- [x] Display and vote on the current poll.
- [x] Responsive UI.
- [x] Clean CSS animations.
- [ ] Real-time active poll retrieval.
- [ ] Real-time poll results updates.

## Contributing

If you want to contribute to this project, you can:

1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature/NewFeature`).
3. Commit your changes (`git commit -m 'Add new feature`).
4. Push to the branch (`git push origin feature/NewFeature`).
5. Open a Pull Request.

## Contact

For questions or feedback, feel free to open an issue or contact me via -

X - [@JeffreyJordan97](https://x.com/JeffreyJordan97) <br />
LinkedIn - [@Jeffrey-Jordan1997](https://www.linkedin.com/in/jeffrey-jordan1997/)
