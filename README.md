<div align="center">

# ⌨️ TypeBaazi

### Multiplayer Typing Race Game built with Java & Spring Boot

A full-stack multiplayer typing race application where two players can create or join a room, compete on the same paragraph, and compare typing speed, accuracy, and mistakes.

<br>

[![Live Demo](https://img.shields.io/badge/Live_Demo-TypeBaazi-46E3B7?style=for-the-badge&logo=render&logoColor=white)](https://typebaazi.onrender.com)

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![React](https://img.shields.io/badge/React-Frontend-61DAFB?style=for-the-badge&logo=react&logoColor=black)

</div>

---

## 📌 About the Project

**TypeBaazi** is a Java-based multiplayer typing race application developed as a microproject for the **Java Programming** subject.

The project demonstrates the practical use of Java in building a complete backend system using **Spring Boot**, along with database integration, authentication, REST APIs, game logic, and frontend communication.

Two users can create or join a private room, participate in a timed typing race, and receive results including:

- Typing speed in WPM
- Accuracy percentage
- Number of mistakes
- Winner of the race
- Previous match history
- Personal best typing speed

The frontend is built using React, while the main application logic and backend services are implemented in Java.

---

## 🎯 Project Objective

The objective of TypeBaazi is to develop an interactive Java application that demonstrates concepts such as:

- Object-Oriented Programming
- Java classes and records
- Collections
- REST API development
- Exception handling
- Database connectivity
- Authentication and authorization
- Backend business logic
- Concurrent room management
- Full-stack application development

---

## ✨ Features

### 👤 User Authentication

- User registration
- User login
- Secure password storage
- Session-based authentication
- Protected application endpoints
- CSRF protection using Spring Security

### 🎮 Multiplayer Rooms

- Create a private typing room
- Automatic 6-character room code generation
- Join using room code
- Maximum of two players per room
- Room creator acts as host
- Guest player ready system
- Host-controlled game start

### ⏱️ Custom Race Duration

The room creator can select:

- 30 seconds
- 60 seconds
- 90 seconds
- 120 seconds

### 📝 Typing Race

- Both players receive the same paragraph
- Automatic 3-second countdown
- Random paragraph selection
- Server-controlled race timing
- Typing progress sent to the backend during the race
- Duplicate and outdated typing updates are prevented using sequence numbers

### 📊 Performance Calculation

After the race, TypeBaazi calculates:

- Words Per Minute (WPM)
- Accuracy
- Number of mistakes
- Correctly typed characters
- Winner of the race

### 🏆 Race Results

The result screen displays:

- Winner
- Player WPM
- Opponent WPM
- Accuracy
- Mistakes
- Race duration

### 📜 Match History

Logged-in users can view:

- Total races played
- Total wins
- Best WPM
- Previous opponents
- Match outcome
- Accuracy
- Mistakes
- Match date and time

### 🔊 User Experience

- Countdown sounds
- Game result sounds
- Responsive user interface
- Modern typing-race design
- Browser-based gameplay

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| Programming Language | Java 17 |
| Backend Framework | Spring Boot 4.1.1 |
| Web Layer | Spring Web MVC |
| Security | Spring Security |
| Database ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Backend Build Tool | Maven |
| Frontend | React |
| Frontend Build Tool | Vite |
| Styling | Tailwind CSS / CSS |
| Icons | Lucide React |
| Containerization | Docker |
| Deployment | Render |
| Version Control | Git & GitHub |

---

## 🏗️ System Architecture

```text
                       ┌──────────────────────┐
                       │        User          │
                       │    Web Browser       │
                       └──────────┬───────────┘
                                  │
                                  ▼
                       ┌──────────────────────┐
                       │    React Frontend    │
                       │    React + Vite      │
                       └──────────┬───────────┘
                                  │
                              REST API
                                  │
                                  ▼
                ┌────────────────────────────────┐
                │      Java Spring Boot API      │
                │                                │
                │  • Authentication              │
                │  • Room Management             │
                │  • Race Logic                  │
                │  • WPM / Accuracy Calculation  │
                │  • Match History               │
                └───────────────┬────────────────┘
                                │
                          Spring Data JPA
                                │
                                ▼
                       ┌──────────────────────┐
                       │     PostgreSQL       │
                       │      Database        │
                       └──────────────────────┘
```

---

## ☕ Java Backend

The Java backend is the core of TypeBaazi.

Important backend classes include:

```text
backend/src/main/java/com/typebaazi/
│
├── BackendApplication.java
├── AuthController.java
├── RoomController.java
├── HistoryController.java
├── RaceScoring.java
├── RaceResult.java
├── RaceResultRepository.java
├── AppUser.java
├── UserRepository.java
├── SecurityConfig.java
├── Paragraphs.java
└── ApiErrors.java
```

### Main Responsibilities

#### `RoomController.java`

Handles:

- Room creation
- Joining rooms
- Player readiness
- Starting the race
- Race timing
- Typing progress
- Winner determination
- Automatic result saving
- Expired room cleanup

#### `RaceScoring.java`

Responsible for calculating:

- WPM
- Accuracy
- Mistakes
- Correct characters

#### `AuthController.java`

Handles:

- Registration
- Login
- Session creation
- User profile retrieval
- Password validation

#### `HistoryController.java`

Returns:

- Previous races
- Total matches
- Wins
- Best WPM
- Opponent statistics

#### `SecurityConfig.java`

Configures application security using Spring Security.

---

## 🗄️ Database

TypeBaazi uses **PostgreSQL** for persistent data storage.

The database stores information such as:

### Users

```text
User ID
Username
Email
Password Hash
Account Creation Time
```

### Race Results

```text
Race ID
Room Code
Player 1
Player 2
Player 1 WPM
Player 2 WPM
Player 1 Accuracy
Player 2 Accuracy
Mistakes
Winner
Race Duration
Completion Time
```

Spring Data JPA is used to communicate between Java classes and PostgreSQL.

---

## 🔄 Application Flow

```text
Register / Login
       │
       ▼
Create Room
       │
       ├──────── Share Room Code ────────┐
       │                                  │
       ▼                                  ▼
     Host                            Player 2
       │                                  │
       │                           Join Room
       │                                  │
       │                            Click Ready
       │                                  │
       └──────────── Start Race ◄─────────┘
                       │
                       ▼
                3 Second Countdown
                       │
                       ▼
                  Typing Race
                       │
                       ▼
            WPM + Accuracy + Mistakes
                       │
                       ▼
                  Race Result
                       │
                       ▼
               Save to PostgreSQL
                       │
                       ▼
                  Match History
```

---

## 📁 Project Structure

```text
TypeBaazi1/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── typebaazi/
│   │   │   │
│   │   │   └── resources/
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── frontend/
│   ├── src/
│   │   ├── App.jsx
│   │   ├── Lobby.jsx
│   │   ├── RaceResults.jsx
│   │   ├── api.js
│   │   ├── sound.js
│   │   └── index.css
│   │
│   ├── package.json
│   └── vite.config.js
│
├── Dockerfile
├── render-build.sh
├── BUILD-AND-RUN.bat
└── README.md
```

---

## 🚀 Running the Project Locally

### Prerequisites

Make sure the following are installed:

- Java 17
- Maven
- PostgreSQL
- Node.js
- npm
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/rohitkumawat-dev/TypeBaazi1.git
cd TypeBaazi1
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE typebaazi;
```

Configure the database connection in your local Spring Boot configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/typebaazi
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
```

> Do not commit your actual PostgreSQL password to GitHub.

### 3. Start the Backend

```bash
cd backend
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

The backend normally runs at:

```text
http://localhost:8080
```

### 4. Start the Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

The frontend normally runs at:

```text
http://localhost:5173
```

---

## 🐳 Docker Deployment

The project uses a multi-stage Docker build.

The Dockerfile:

1. Builds the React frontend using Node.js
2. Copies the generated frontend into Spring Boot static resources
3. Builds the Java backend using Maven
4. Packages the complete application as a JAR
5. Runs the application using Java 17 JRE

Build the Docker image:

```bash
docker build -t typebaazi .
```

Run it:

```bash
docker run -p 8080:8080 typebaazi
```

---

## ☁️ Production Deployment

TypeBaazi is deployed using **Render**.

The production Spring Boot configuration uses environment variables:

```text
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
PORT
```

This helps keep sensitive database credentials outside the source code.

---

## 🔐 Security

The project uses Spring Security for authentication and application protection.

Implemented security features include:

- Secure password hashing
- Session-based authentication
- CSRF protection
- Authentication-required API endpoints
- Secure production cookies
- Server-side input validation
- Database-backed user accounts

Passwords are not stored in plain text.

---

## 📚 Java Concepts Demonstrated

This project demonstrates several important Java concepts:

- Classes and Objects
- Object-Oriented Programming
- Encapsulation
- Java Records
- Collections
- Lists
- Maps
- ConcurrentHashMap
- UUID generation
- Exception handling
- REST Controllers
- Dependency Injection
- Interfaces
- Spring Data repositories
- JPA entities
- Scheduled tasks
- Authentication
- Server-side validation

---

## 🎓 Academic Purpose

TypeBaazi was developed as a **Java Programming Microproject**.

Instead of creating only a console-based program, this project demonstrates how Java can be used to build a modern full-stack web application.

The Java Spring Boot backend handles:

- User authentication
- Application logic
- Multiplayer room management
- Race management
- Race timing
- Typing progress
- WPM and accuracy calculation
- Result processing
- Match history
- PostgreSQL database operations
- Security

---

## 🔮 Future Improvements

Possible future improvements include:

- Global leaderboard
- Player profile statistics
- More typing categories
- Difficulty levels
- Custom avatars
- Friends system
- Public matchmaking
- Tournament mode
- Mobile optimization
- Detailed performance analytics

---

## 👨‍💻 Developer

<div align="center">

### Rohit Kumawat

B.Tech Student  
Java Programming Microproject

[![GitHub](https://img.shields.io/badge/GitHub-rohitkumawat--dev-181717?style=for-the-badge&logo=github)](https://github.com/rohitkumawat-dev)

</div>

---

<div align="center">

### ⌨️ Type. Race. Win. 🏁

Built using **Java, Spring Boot, PostgreSQL and React**

⭐ If you like the project, consider giving the repository a star.

</div>
