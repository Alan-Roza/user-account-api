FROM openjdk:8-jre-slim

# Instalar o curl
RUN apt-get update && apt-get install -y curl

WORKDIR /user-account-api
COPY target/*.war /user-account-api/user-account-api-0.0.1-SNAPSHOT.war
EXPOSE 9090
CMD java -XX:+UseContainerSupport -Xmx512m -Dserver.port=9090 -jar user-account-api-0.0.1-SNAPSHOT.war