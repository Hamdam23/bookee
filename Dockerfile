FROM maven:3.6.3-openjdk-17 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM openjdk:17
COPY --from=build /usr/src/app/target/*.jar /usr/app/server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/app/server-0.0.1-SNAPSHOT.jar"]
