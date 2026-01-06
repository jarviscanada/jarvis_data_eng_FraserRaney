#
# Build stage
#
FROM maven:3.8.6-openjdk-8-slim AS build
WORKDIR /build
COPY src ./src
COPY pom.xml .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM amazoncorretto:8-alpine
COPY --from=build /build/target/trading-app-1.0.jar /usr/local/app/trading/lib/trading_app.jar
ENTRYPOINT ["java","-jar","/usr/local/app/trading/lib/trading_app.jar"]