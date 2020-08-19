FROM openjdk:11-jre-slim

ADD target/tic-tac-toe.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/app.jar"]
