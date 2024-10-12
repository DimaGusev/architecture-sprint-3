FROM openjdk:17-jdk-alpine as builder
WORKDIR /home/app
ADD mvnw /home/app/mvnw
ADD .mvn /home/app/.mvn
ADD pom.xml /home/app/pom.xml
ADD src /home/app/src
RUN chmod +x ./mvnw
RUN ./mvnw -Dmaven.test.skip=true clean package

FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY --from=builder /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]