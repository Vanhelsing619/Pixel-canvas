<div align="center">

# 🎨 Pixel Pulse — Real-Time Collaborative Canvas

A shared pixel canvas where **multiple people paint together live**. Click a cell, and your pixel
appears on everyone else's screen instantly — powered by **WebSockets**. Built with **Spring Boot**
and **React**, served from a single server with a zero-setup database.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-6DB33F.svg)](https://spring.io/projects/spring-boot)
[![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-010101.svg)](https://stomp.github.io/)
[![React](https://img.shields.io/badge/React-18-61DAFB.svg)](https://react.dev/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](#license)

</div>

---



## ✨ Features

- ⚡ **Real-time multi-user sync** — every paint is broadcast to all connected clients over WebSockets
- 👥 **Live presence counter** — see how many people are on the canvas right now
- 🎨 **16-color palette** with click-to-paint on an HTML5 canvas
- 🛡️ **Server-side validation** — out-of-bounds coordinates and off-palette colors are rejected
- 💾 **Persistent artwork** — every pixel is saved to the database and reloaded on startup, so the
  canvas survives refreshes and restarts
- 🚀 **Single server, zero setup** — backend serves the React frontend; no separate frontend process

---

## 🧱 Tech stack

| Layer        | Technology                                        |
|--------------|---------------------------------------------------|
| Backend      | Spring Boot 3, Spring WebSocket (STOMP)           |
| Persistence  | Spring Data JPA + H2 (file-based)                 |
| Frontend     | React 18 + HTML5 Canvas                           |
| Realtime     | SockJS + STOMP.js                                 |
| Build        | Maven                                             |

---

## 🔧 How the real-time sync works

1. The browser loads the current canvas via `GET /api/canvas` and draws it.
2. It opens a **WebSocket** connection to `/ws` (SockJS) and speaks **STOMP** over it.
3. Clicking a cell publishes a message to `/app/paint`.
4. The server validates the pixel, saves it, and broadcasts it to `/topic/pixels`.
5. Every connected client receives the broadcast and updates that cell — instantly.
6. Connect/disconnect events update a live count on `/topic/presence`.

---

## 🚀 Getting started

You only need **JDK 17+** installed.

### Option A — IDE (just press Run ▶)
1. Open the project in **IntelliJ IDEA**, **Eclipse**, or **VS Code** (Java extension).
2. Let Maven import dependencies.
3. Run `PixelCanvasApplication.java`.
4. Open **http://localhost:8082** — then open it in a **second tab** and paint to see live sync.

### Option B — Command line
```bash
mvn spring-boot:run
```
Then open **http://localhost:8082** in two windows.

> ℹ️ The frontend loads React, SockJS, and STOMP.js from a CDN at runtime, so an internet connection
> is needed the first time you open the page.

---

## 🔗 API & messaging reference

**REST**

| Method | Endpoint       | Description                                  |
|--------|----------------|----------------------------------------------|
| `GET`  | `/api/canvas`  | Current state: grid size, palette, painted pixels |

**WebSocket (STOMP over SockJS at `/ws`)**

| Direction        | Destination        | Payload                          |
|------------------|--------------------|----------------------------------|
| Client → Server  | `/app/paint`       | `{ "x": 3, "y": 5, "color": "#E50000" }` |
| Server → Clients | `/topic/pixels`    | the applied pixel                |
| Server → Clients | `/topic/presence`  | `{ "online": 4 }`                |

---

## 📂 Project structure

```
src/main/java/com/example/pixelcanvas/
├── PixelCanvasApplication.java        # entry point
├── config/
│   ├── WebSocketConfig.java           # STOMP endpoints + broker
│   └── PresenceListener.java          # online-user tracking
├── controller/
│   ├── CanvasController.java          # REST: initial state
│   └── CanvasWebSocketController.java # handles /app/paint
├── service/CanvasService.java         # in-memory board + persistence + validation
├── repository/PixelRepository.java
├── model/Pixel.java
└── dto/PixelMessage.java
src/main/resources/
├── application.properties             # port 8082 + H2 config
└── static/index.html                  # React + canvas + STOMP frontend
```

---

## 🛣️ Roadmap / ideas to extend

- [ ] Rate limiting / cooldown per user (like the original r/place)
- [ ] Pan & zoom for a larger canvas
- [ ] Live cursors showing where others are hovering
- [ ] Accounts so each pixel records who placed it
- [ ] Switch H2 for PostgreSQL + Redis pub/sub to scale across servers

---

## 📝 License

Released under the MIT License. Feel free to use, modify, and learn from it.
