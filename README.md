<div align="center">

# ⌨️ TypeBaazi

### A fun multiplayer type-racing game to play with your friends

[![Live Demo](https://img.shields.io/badge/Play%20Now-typebaazi.onrender.com-46E3B7?style=for-the-badge&logo=render&logoColor=white)](https://typebaazi.onrender.com)

![React](https://img.shields.io/badge/React-20232A?style=flat-square&logo=react&logoColor=61DAFB)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java%2017-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js%2022-339933?style=flat-square&logo=nodedotjs&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Deployed%20on%20Render-46E3B7?style=flat-square&logo=render&logoColor=white)

</div>

---

## 📖 About

**TypeBaazi** is a type-racing game built for playing with friends. Join a race, type the paragraph as fast and as accurately as you can, and see who finishes first.

🔗 **Live:** [https://typebaazi.onrender.com](https://typebaazi.onrender.com)

> ⏳ The app is hosted on Render. If it has been idle, the first load may take a short while to wake up.

---

## 🛠️ Tech Stack

| Layer | Technology |
| ----- | ---------- |
| **Frontend** | React (built with `npm run build`, output in `frontend/dist`) |
| **Backend** | Spring Boot (Java 17), built with Maven / Maven Wrapper |
| **Packaging** | The React build is bundled into Spring Boot's `static` resources and served as a single JAR |
| **Containerization** | Docker (multi-stage build) |
| **Hosting** | Render |
| **Build tooling** | Node.js 22, Maven 3.9, Eclipse Temurin JDK/JRE 17 |

### How it fits together

The project ships as **one deployable unit**. The React frontend is compiled first, and its `dist/` output is copied into `backend/src/main/resources/static/`. Spring Boot then packages everything into a single JAR, so the same server on port `8080` serves both the website and the backend.

```
React (frontend/)  ──npm run build──▶  dist/
                                         │
                                         ▼ copied into
Spring Boot (backend/)  ◀── src/main/resources/static/
         │
         ▼ mvn package
     app.jar  ──▶  http://localhost:8080
```

---

## 📁 Project Structure

```
TypeBaazi1/
├── backend/                               # Spring Boot application (Java 17, Maven)
├── frontend/                              # React application
├── backup-paragraph-results-10448-10408/  # Backup of paragraph results
├── backup-paragraph-results-9951-15050/   # Backup of paragraph results
├── BUILD-AND-RUN.bat                      # One-click build + run script for Windows
├── Dockerfile                             # Multi-stage build (frontend → backend → runtime)
├── render-build.sh                        # Build script used for Render deployment
├── .dockerignore
└── .gitignore
```

---

## ✅ Prerequisites

| Tool | Version | Needed for |
| ---- | ------- | ---------- |
| Node.js + npm | 22 (recommended) | Building the frontend |
| JDK | 17 | Running / building the backend |
| Maven | 3.9+ (or the bundled `mvnw`) | Building the backend |
| Docker | Any recent version | Optional, for container runs |

---

## 🚀 Getting Started

### Option 1: Windows one-click script

From the project root:

```bat
cd frontend
npm install
cd ..
BUILD-AND-RUN.bat
```

The script builds the React app, copies it into the backend's static folder, and starts Spring Boot with the Maven Wrapper. Wait for the `Started BackendApplication` message, then open **http://localhost:8080**.

> The script looks for JDK 17 under `%LOCALAPPDATA%\Programs\Eclipse Adoptium\` and otherwise uses your `JAVA_HOME`. Make sure Java 17 is installed.

### Option 2: Manual build (any OS)

```bash
# 1. Build the frontend
cd frontend
npm ci            # or: npm install
npm run build

# 2. Copy the build into Spring Boot's static folder
cd ../backend
rm -rf src/main/resources/static
mkdir -p src/main/resources/static
cp -R ../frontend/dist/. src/main/resources/static/

# 3. Run the backend
./mvnw spring-boot:run
```

Then open **http://localhost:8080**.

### Option 3: Docker

```bash
docker build -t typebaazi .
docker run -p 8080:8080 typebaazi
```

The container reads the `PORT` environment variable and falls back to `8080`:

```bash
docker run -e PORT=9090 -p 9090:9090 typebaazi
```

---

## ☁️ Deployment (Render)

TypeBaazi is live on Render. Two build paths are included in the repo:

- **Docker:** the `Dockerfile` runs a three-stage build:
  1. `node:22-alpine` builds the React frontend.
  2. `maven:3.9.9-eclipse-temurin-17` copies the frontend build into Spring Boot's static folder and packages the JAR (tests skipped).
  3. `eclipse-temurin:17-jre` runs the final JAR with `--server.port=${PORT:-8080}`.
- **Script:** `render-build.sh` does the same build steps without Docker (builds the frontend, copies `dist/` into `backend/src/main/resources/static/`, then runs `mvn clean package -DskipTests`, using `mvnw` if present).

---

## 🎮 How to Play

1. Open the [live site](https://typebaazi.onrender.com) (or your local server).
2. Join a race with your friends.
3. Type the paragraph shown as quickly and accurately as you can.
4. Check the results to see who won.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome. Feel free to fork the repo and open a pull request.

---

## 👤 Author

**Rohit Kumawat**

- GitHub: [@rohitkumawat-dev](https://github.com/rohitkumawat-dev)

---

<div align="center">

⭐ If you enjoyed TypeBaazi, consider giving the repo a star!

</div>
