FROM openjdk:11
EXPOSE 8082
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/projet-0.0.1-SNAPSHOT.jar projet-0.0.1-SNAPSHOT.jar
CMD ["java","-Dserver.port=8082","-jar","projet-0.0.1-SNAPSHOT.jar"]



