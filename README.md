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
