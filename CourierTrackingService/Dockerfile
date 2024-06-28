# Build stage
FROM gradle:7.5.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build --no-daemon

# Run stage
FROM openjdk:19-jdk
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar /app/app.jar
VOLUME /tmp
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["sh", "-c", "/wait-for-it.sh courier-postgis:5432 -- java -jar /app/app.jar"]
EXPOSE 8080
