FROM gradle:8-jdk19 AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

FROM openjdk:19

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/comment-service.jar

ENV MONGO_DB_USERNAME=rootadmin \
    MONGO_DB_PWD=password

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/comment-service.jar"]
