FROM amazoncorretto:8-alpine
COPY target/stockquote-1.0-SNAPSHOT.jar /usr/local/app/grep/lib/stockquote.jar
COPY src/main/resources/properties.txt src/main/resources/properties.txt
ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/stockquote.jar"]