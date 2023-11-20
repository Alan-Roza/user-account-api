FROM openjdk:8-jre-slim
WORKDIR /userAccountApi
COPY target/*.war /userAccountApi/userAccountApi-0.0.1-SNAPSHOT.war
EXPOSE 9090
CMD java -XX:+UseContainerSupport -Xmx512m -Dserver.port=9090 -jar userAccountApi-0.0.1-SNAPSHOT.war



