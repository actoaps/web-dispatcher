FROM openjdk:15-jdk-alpine

ADD build/distributions/web-dispatcher-1.0-SNAPSHOT.tar ./

ENTRYPOINT ["bin/web-dispatcher"]
