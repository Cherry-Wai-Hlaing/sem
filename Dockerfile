FROM eclipse-temurin:25

LABEL authors="DELL"

COPY ./target/seMethods-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp

WORKDIR /tmp

ENTRYPOINT ["java", "-jar", "seMethods-1.0-SNAPSHOT-jar-with-dependencies.jar"]