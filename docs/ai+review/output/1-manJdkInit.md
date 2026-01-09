Understood. If you’re building your own automation and want to stay away from the "weight" of tools like SDKMAN!, doing it manually is the way to go. Since you are on a Debian-based system (given your mention of `.deb` and `bashrc`), here is the surgical, manual path to Java 25.

### 1. Manual Java 25 Setup (The "No-Magic" Way)

Instead of a script doing it for you, download the binary directly. In 2026, Java 25 is the stable LTS.

1. **Download & Extract:**
```bash
# Move to your preferred manual install directory
cd /opt
sudo wget https://download.oracle.com/java/25/latest/jdk-25_linux-x64_bin.tar.gz
sudo tar -xvf jdk-25_linux-x64_bin.tar.gz
# Identify the folder (usually jdk-25.x.x)
ls -d jdk-25*

```


2. **Clean Up Old Versions:**
Before setting the new one, purge the old apt-installed versions to prevent path conflicts.
```bash
# Remove OpenJDK packages if they exist
sudo apt-get purge openjdk-* # Manually delete old folders in common locations
sudo rm -rf /usr/lib/jvm/*

```


3. **Link to Bashrc:**
Add these lines to the bottom of your `~/.bashrc` to point specifically to your manual `/opt` install:
```bash
export JAVA_HOME=/opt/jdk-25  # Ensure this matches your extracted folder name
export PATH=$JAVA_HOME/bin:$PATH

```


Run `source ~/.bashrc` to apply.

---

### 2. Spring Initializr & Package Naming

Since you are rolling with **`ke.bluestron.api`** (the clear winner for industry standards), here is how to launch your backend via CLI using the Spring Initializr API directly (since you like automation/manual control):

```bash
curl https://start.spring.io/starter.tgz \
  -d dependencies=web,data-jpa,postgresql \
  -d javaVersion=25 \
  -d type=maven-project \
  -d groupId=ke.bluestron \
  -d artifactId=api \
  -d name=api \
  -d packageName=ke.bluestron.api \
  | tar -xzvf -

```

---

### 3. Naming Convention: The "BS" Debate

I strongly agree with **unique root names** but disagree with **abbreviated sub-modules** like `bsdb`.

* **The Consensus:** In a world of microservices and clean architecture, the "uniqueness" belongs to the **Group ID** (`ke.bluestron`).
* **The Logic:** If your folder structure is `bluestron/api` and `bluestron/data`, the context is already provided by the parent folder. Naming a folder `bsapi` inside a `bluestron` directory is redundant ("Bluestron Bluestron API").

**Industry Best Practice Path:**

* **Repo Root:** `bluestron-backend`
* **Module 1:** `api` (inside `ke.bluestron.api`)
* **Module 2:** `persistence` or `data` (inside `ke.bluestron.data`)

And remember, for your manual "automation," keep your JPA entities strictly mapped as we discussed earlier:

```java
// Manual control over column types as requested
@Column(name = "content", columnDefinition = "TEXT")
private String content;

```

Would you like me to write a custom bash script that combines the download, extraction, and `bashrc` update into a single "Bluestron-Standard" installer for your team?

[How to Install Java JDK 25 on Ubuntu Linux](https://www.youtube.com/watch?v=0IjOnll4hPU)
This video demonstrates the manual installation of the Java 25 Debian package using the terminal, which aligns with your preference for manual control over automated tools.