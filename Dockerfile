# Use a base image with Java
FROM openjdk:11-jdk

# Install curl
RUN apt-get update \
    && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /user-account-api
COPY target/*.war /user-account-api/user-account-api-0.0.1-SNAPSHOT.war

EXPOSE 9090
CMD java -XX:+UseContainerSupport -Xmx512m -Dserver.port=9090 -jar /user-account-api/user-account-api-0.0.1-SNAPSHOT.war
