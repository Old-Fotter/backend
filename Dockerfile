FROM openjdk:21-jdk-slim

WORKDIR /app

COPY . .

RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/communal-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
