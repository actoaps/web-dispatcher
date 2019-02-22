FROM openjdk:11.0.1-slim

ADD build/distributions/web-dispatcher-1.0-SNAPSHOT.tar ./
EXPOSE 8080

ENTRYPOINT ["bin/web-dispatcher"]
