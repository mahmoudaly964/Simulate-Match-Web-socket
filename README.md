# Simulate-Match-Web-socket

## Description

This project is a simple live match tracking system built with Java and Spring Boot. It consists of two main modules:

- **match**: A Spring Boot server application that manages football (or other sports) matches, tracks scores, and provides real-time updates to clients using REST and WebSocket APIs.
- **matchclient**: A Java client application that allows users to create matches, update scores, subscribe to live match updates, and view match statistics by interacting with the server.

The system demonstrates the use of RESTful APIs for data retrieval and updates, as well as WebSocket communication for real-time score updates and notifications when matches end.

## Features

### Server (`match`)
- Create new matches between two teams.
- Update scores in real-time via WebSocket.
- End matches and notify all subscribers.
- Query the number of active matches and total goals per team via REST endpoints.

### Client (`matchclient`)
- Create a match and update scores as the match creator.
- Subscribe to live match updates as a viewer.
- View match statistics (active matches, team goals).

## Getting Started

### Prerequisites
- Java 17+
- Maven

### Build and Run

#### Server
```sh
cd match
./mvnw spring-boot:run
```

#### Client
```sh
cd matchclient
./mvnw compile exec:java -Dexec.mainClass="com.example.matchclient.MatchclientApplication"
```

## Usage

1. Start the server (`match`) first.
2. Run the client (`matchclient`) in a separate terminal.
3. Follow the on-screen prompts to create matches, update scores, subscribe, or view statistics.

## Project Structure

- `match/`: Spring Boot server application
- `matchclient/`: Java client application

## Technologies

- Java 17
- Spring Boot (Web, WebSocket)
- Maven

## License

This project is licensed under the Apache License 2.0.
