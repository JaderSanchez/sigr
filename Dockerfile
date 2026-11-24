# Imagen de ejecución del SIGR. El JAR lo construye la etapa "build" del pipeline
# (./mvnw verify) y llega a este contexto como artefacto.
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S sigr && adduser -S sigr -G sigr
WORKDIR /app
COPY target/sigr-*.jar app.jar
USER sigr
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --retries=10 \
  CMD wget -qO- http://localhost:8080/api/platos > /dev/null || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
