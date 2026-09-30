FROM eclipse-temurin:25

LABEL authors="DELL"

COPY ./target/seMethods-0.1.0.4-jar-with-dependencies.jar /tmp

WORKDIR /tmp

ENTRYPOINT ["java", "-jar", "seMethods-0.1.0.4-jar-with-dependencies.jar"]