FROM openjdk:17

EXPOSE 8090

ADD target/EffectiveMobileTestTask-0.0.1-SNAPSHOT.jar backend.jar

CMD ["java", "-jar", "backend.jar"]
