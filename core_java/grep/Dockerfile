FROM amazoncorretto:8-alpine
COPY target/JavaGrep-1.0.jar /usr/local/app/grep/lib/grep.jar
ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]
