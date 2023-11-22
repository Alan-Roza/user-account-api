FROM openjdk:8-jre-slim

WORKDIR /user-account-api

COPY target/user-account-api-0.0.1-SNAPSHOT.war .

EXPOSE 9090

CMD ["java", "-XX:+UseContainerSupport", "-Xmx512m", "-Dserver.port=9090", "-jar", "user-account-api-0.0.1-SNAPSHOT.war"]
HEALTHCHECK --interval=1m --timeout=10s CMD curl -f http://localhost:9090/actuator/health || exit 1
