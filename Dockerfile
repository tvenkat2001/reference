FROM eclipse-temurin:21-jre-alpine
WORKDIR /reference
COPY target/*.jar app.jar
EXPOSE 9432
ENTRYPOINT ["java","-jar","app.jar"]