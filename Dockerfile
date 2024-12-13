FROM eclipse-temurin:23-noble AS builder
WORKDIR /src

COPY mvnw .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

RUN chmod a+x mvnw && /src/mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jre-noble
WORKDIR /app

COPY --from=builder /src/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

ENV REDIS_HOST=localhost \
    REDIS_PORT=6379 \
    REDIS_DATABASE=0 \
    NOTICE_PUBLISH_URL=https://publishing-production-d35a.up.railway.app/notice \
    SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

HEALTHCHECK --interval=1m --timeout=3s --start-period=2m \
  CMD curl -f http://localhost:${SERVER_PORT}/status || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"] 