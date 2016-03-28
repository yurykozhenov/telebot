# Pull base image.
FROM java:8-jre

VOLUME /usr/share/telebot:/app

ADD telebot.jar /app/

ADD application.properties /app/

CMD ["java", "-jar", "/app/telebot.jar"]

EXPOSE 8080
