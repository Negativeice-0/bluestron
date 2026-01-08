To boost your laptop performance and eliminate lingering system processes—especially when working with Docker and development tools—follow these steps:

### 1. **Stop and Clean Docker Resources (OS-Wide)**
Docker can consume significant RAM and CPU, especially if containers are running in the background.

- **Stop all Docker containers**:
  ```bash
  docker stop $(docker ps -q) 2>/dev/null || true
  ```
- **Remove all containers**:
  ```bash
  docker rm $(docker ps -aq) 2>/dev/null || true
  ```
- **Remove unused images**:
  ```bash
  docker image prune -a -f
  ```
- **Clean up networks and volumes**:
  ```bash
  docker network prune -f
  docker volume prune -f
  ```

> 💡 Use `2>/dev/null || true` to avoid errors if no containers exist.

---

### 2. **Check for High-RAM/CPU Processes**
Use your system’s task manager or terminal tools:

- **On macOS**:
  - Open **Activity Monitor**.
  - Sort by **Memory** or **CPU**.
  - Quit any unnecessary processes (e.g., old IDEs, Docker, Chrome tabs, virtual machines).

- **On Windows**:
  - Open **Task Manager**.
  - End tasks for high-memory apps (e.g., `Docker Desktop`, `Visual Studio Code`, `Chrome`, `Java` processes).

- **On Linux**:
  ```bash
  top
  # or
  htop
  ```
  Kill non-essential processes using `kill <PID>` or `pkill <process_name>`.

---

### 3. **Close Unnecessary Applications**
- **Close unused browser tabs** (especially those with dev tools or video streams).
- **Quit background apps** like Slack, Zoom, Spotify, or cloud sync tools.
- **Disable startup apps**:
  - **macOS**: System Settings → General → Login Items.
  - **Windows**: Task Manager → Startup → Disable.
  - **Linux**: Check `.config/autostart` or `systemctl` for services.

---

### 4. **Free Up Disk Space**
- Delete temporary files:
  ```bash
  # On macOS
  rm -rf /private/var/tmp/*
  rm -rf /tmp/*
  ```
  ```bash
  # On Linux
  sudo rm -rf /tmp/*
  ```
- Clear cache from apps like:
  - **Chrome**: `chrome://settings/clearBrowserData`
  - **VS Code**: Delete `~/.vscode` or `~/.cache` folders.

---

### 5. **Optimize System Settings**
- **Enable power-saving mode** (if on battery).
- **Disable animations** (in system preferences).
- **Set CPU governor to "powersave"** (Linux only, via `cpupower` or `ondemand`).

---

### 6. **Reboot (Optional but Effective)**
A full reboot clears all lingering processes and memory leaks. It’s the fastest way to restore performance.

---

### 7. **Verify Docker is Fully Stopped**
After cleanup, check:
```bash
docker ps -a
```
Ensure no containers are running.

Then restart your project:
```bash
make up
```

---

✅ **Final Tip**: Use `htop`, `docker stats`, or `top` to monitor resource usage before and after cleanup. This helps you confirm improvements.

By cleaning up Docker and system processes, you’ll free up RAM, reduce CPU load, and improve overall performance—especially important when running full-stack apps like Bluestron.