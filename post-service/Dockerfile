# ------------------------------
# Dockerfile for user-service
# ------------------------------

#----
# BUILD stage
#----

#base image
FROM gradle:8-jdk19 AS builder

# Copy the application files from the host machine to the image filesystem
COPY --chown=gradle:gradle . /home/gradle/src

# set the directory within the image, for executing future commands
WORKDIR /home/gradle/src

# run gradle build within the container - NOT the host machine
RUN gradle build -x test

#--------------------
#     Package stage
#--------------------

FROM openjdk:19

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/post-service.jar

ENV MONGO_DB_USERNAME=rootadmin \
    MONGO_DB_PWD=password

EXPOSE 8084

ENTRYPOINT ["java","-jar","/app/post-service.jar"]

