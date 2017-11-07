FROM openjdk:9.0.1-11-slim

COPY target/web-dispatcher-1.0-SNAPSHOT.jar ./web-dispatcher.jar
COPY target/lib/* ./lib/
EXPOSE 8080

ENTRYPOINT ["java","-jar","./web-dispatcher.jar"]
