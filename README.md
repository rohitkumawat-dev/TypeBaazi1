<div align="center">

# ⌨️ TypeBaazi 🏁

### Race your friends. Type faster. Win bragging rights.

A real-time **multiplayer type-racing game** — jump into a room, share the code, and see who has the fastest fingers.

<br/>

[![Play Now](https://img.shields.io/badge/▶_PLAY_NOW-typebaazi.onrender.com-46E3B7?style=for-the-badge&logo=render&logoColor=white)](https://typebaazi.onrender.com)

<br/>

![GitHub Repo stars](https://img.shields.io/github/stars/rohitkumawat-dev/TypeBaazi1?style=for-the-badge&logo=github&color=yellow)
![GitHub forks](https://img.shields.io/github/forks/rohitkumawat-dev/TypeBaazi1?style=for-the-badge&logo=github&color=blue)
![GitHub last commit](https://img.shields.io/github/last-commit/rohitkumawat-dev/TypeBaazi1?style=for-the-badge&logo=git&logoColor=white&color=orange)
![Status](https://img.shields.io/badge/status-live-brightgreen?style=for-the-badge)

</div>

---

## 📖 About

**TypeBaazi** (*baazi* = "game / bet" in Hindi) is a fast-paced multiplayer typing game. Everyone in a room gets the same paragraph, the race starts together, and your progress is shown live against your opponents. Finish first with the best accuracy to take the crown.

Perfect for a quick break with friends, or for sharpening your typing speed while having fun.

## ✨ Features

- 🏎️ **Real-time multiplayer races** — see opponents' progress as they type
- 👥 **Play with friends** — create a room and challenge them
- 📝 **Paragraph-based races** — type real passages, not just random words
- ⚡ **Live WPM & accuracy tracking**
- 🐳 **Docker-ready** and one-click deployable on Render
- 🌐 **Runs in the browser** — nothing to install to play

## 🎮 How to Play

1. Open **[typebaazi.onrender.com](https://typebaazi.onrender.com)**
2. Enter your name and create or join a race
3. Wait for the countdown
4. Type the paragraph as fast and accurately as you can
5. Cross the finish line first 🏁

> ⏳ **Heads up:** the app is hosted on Render's free tier, so the first load after a period of inactivity can take up to a minute while the server wakes up.

## 🛠️ Tech Stack

<div align="center">

![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![Express](https://img.shields.io/badge/Express.js-000000?style=for-the-badge&logo=express&logoColor=white)
![Socket.io](https://img.shields.io/badge/Socket.io-010101?style=for-the-badge&logo=socketdotio&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

</div>

| Layer | Technology |
| ----- | ---------- |
| Frontend | React + Vite |
| Backend | Node.js + Express |
| Real-time | Socket.IO (WebSockets) |
| Containerization | Docker |
| Hosting | Render |

## 📁 Project Structure

```
TypeBaazi1/
├── backend/                  # Server — game rooms & real-time logic
├── frontend/                 # Client — game UI
├── Dockerfile                # Container build
├── render-build.sh           # Render build script
├── BUILD-AND-RUN.bat         # One-click build & run (Windows)
├── .dockerignore
└── .gitignore
```

## 🚀 Getting Started

### Prerequisites

- [Node.js](https://nodejs.org/) (v18 or later recommended)
- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/) *(optional)*

### 1️⃣ Clone the repository

```bash
git clone https://github.com/rohitkumawat-dev/TypeBaazi1.git
cd TypeBaazi1
```

### 2️⃣ Run locally

**Backend**

```bash
cd backend
npm install
npm start
```

**Frontend** *(in a new terminal)*

```bash
cd frontend
npm install
npm run dev
```

Then open the URL shown in the frontend terminal (usually `http://localhost:5173`).

### 🪟 Windows shortcut

Just double-click **`BUILD-AND-RUN.bat`** to build and launch everything.

### 🐳 Run with Docker

```bash
docker build -t typebaazi .
docker run -p 3000:3000 typebaazi
```

## ☁️ Deployment

TypeBaazi is deployed on **[Render](https://render.com)** using the included `render-build.sh` script and `Dockerfile`.

1. Push the repo to GitHub
2. Create a new **Web Service** on Render and connect the repo
3. Set the build command to `./render-build.sh`
4. Deploy 🎉

## 🗺️ Roadmap

- [ ] 🏆 Global leaderboard
- [ ] 👤 User accounts & stats history
- [ ] 🎨 Custom car / avatar skins
- [ ] 🌍 More languages and paragraph categories
- [ ] 📱 Better mobile experience

## 🤝 Contributing

Contributions are welcome!

1. Fork the project
2. Create your branch: `git checkout -b feature/AmazingFeature`
3. Commit your changes: `git commit -m "Add AmazingFeature"`
4. Push to the branch: `git push origin feature/AmazingFeature`
5. Open a Pull Request

## 👨‍💻 Author

<div align="center">

**Rohit Kumawat**

[![GitHub](https://img.shields.io/badge/GitHub-rohitkumawat--dev-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/rohitkumawat-dev)

</div>

---

<div align="center">

### ⭐ If you enjoyed TypeBaazi, give it a star — it means a lot!

**Made with ❤️ and a lot of keystrokes**

</div>
