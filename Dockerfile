#
# Build stage
#
FROM maven:3.8.1-openjdk-17-slim AS build
COPY pom.xml .

COPY src ./src

# build the app (no dependency download here)
RUN mvn clean package -Dmaven.test.skip


#
# Package stage
#
FROM openjdk:17-alpine

copy --from=build ./target/encrypt-spring-boot-0.0.1-SNAPSHOT.jar encrypt-spring-boot-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","encrypt-spring-boot-0.0.1-SNAPSHOT.jar"]