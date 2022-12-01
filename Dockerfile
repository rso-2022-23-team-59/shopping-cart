# Docker image for building a jar file using maven.
FROM maven:3.8.6-amazoncorretto-18 AS build

COPY ./ /app
WORKDIR /app
RUN mvn --show-version --update-snapshots --batch-mode clean package

# Docker image that copies jar file from "build" Docker image and executes it.
FROM amazoncorretto:18

WORKDIR /app
COPY --from=build ./app/api/target/shopping-cart-api-1.0.0-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "shopping-cart-api-1.0.0-SNAPSHOT.jar"]