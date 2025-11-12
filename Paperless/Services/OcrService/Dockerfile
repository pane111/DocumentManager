FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y     tesseract-ocr     tesseract-ocr-eng     libtesseract-dev     && rm -rf /var/lib/apt/lists/*
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/tessdata/

COPY --from=build /app/target/*.jar app.jar
COPY src/test/resources /app/resources
ENTRYPOINT ["java", "-jar", "app.jar"]