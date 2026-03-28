# ── Base image with Maven and Java 21 ────────────────────────
FROM maven:3.9.6-eclipse-temurin-21

# ── Install Chrome ────────────────────────────────────────────
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    curl \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libcups2 \
    libdbus-1-3 \
    libgdk-pixbuf2.0-0 \
    libnspr4 \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    xdg-utils \
    --no-install-recommends \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
       > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable --no-install-recommends \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# ── Set working directory ─────────────────────────────────────
WORKDIR /app

# ── Copy pom.xml and download dependencies first ──────────────
# This layer is cached — only re-downloads if pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -B

# ── Copy source code ──────────────────────────────────────────
COPY src ./src

# ── Run tests automatically when container starts ─────────────
CMD ["mvn", "clean", "test", "-Dheadless=true", "-Dbrowser=chrome", "-B"]