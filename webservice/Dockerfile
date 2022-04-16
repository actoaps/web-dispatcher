FROM openjdk:15-jdk-oraclelinux8

ADD build/distributions/webservice-1.0-SNAPSHOT.tar ./webservice
WORKDIR ./webservice

EXPOSE 8080

ENTRYPOINT ["bin/webservice"]
