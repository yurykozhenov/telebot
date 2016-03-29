# Pull base image.
FROM java:8-jre

ADD telebot.jar /app/

ADD application.properties /app/

CMD ["java", "-jar", "/app/telebot.jar"]

EXPOSE 8080
