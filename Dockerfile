FROM openjdk:17-jdk-slim-buster
FROM maven:3.8.1-openjdk-17-slim
LABEL maintainer="shukla.ravindra@yahoo.com"

WORKDIR /app
ADD pom.xml /app
RUN mvn verify clean --fail-never

COPY . /app
RUN mvn -v
RUN mvn clean install -DskipTests

EXPOSE 9092
ARG JAR_FILE=/app/target/zilchtest-0.1.jar

ENV profile=docker
# Run the jar file 
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=${profile} /app/target/zilchtest-0.1.jar" ]
