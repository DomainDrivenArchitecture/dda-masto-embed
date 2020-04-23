FROM openjdk:8-alpine

COPY target/uberjar/masto-embed.jar /masto-embed/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/masto-embed/app.jar"]
