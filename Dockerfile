FROM openjdk:18
EXPOSE 8080
ADD target/Xeia-EStore.jar Xeia-EStore.jar
ENTRYPOINT ["java", "-jar", "/Xeia-EStore.jar"]