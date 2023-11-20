FROM openjdk:8-jre-slim
WORKDIR /user-account-api
COPY target/user-account-api-0.0.1-SNAPSHOT.war /user-account-api/
EXPOSE 9090

# Create a non-root user
RUN adduser --system --group --no-create-home myuser
USER myuser

# Optional: Include a health check
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 CMD curl -f http://localhost:9090/actuator/health || exit 1

# Metadata as defined above
LABEL maintainer="Your Name alancruz@outlook.com" \
      description="Description of your application"

CMD ["java", "-XX:+UseContainerSupport", "-Xmx512m", "-Dserver.port=9090", "-jar", "user-account-api-0.0.1-SNAPSHOT.war"]
