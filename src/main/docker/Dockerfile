# Pull base image.
FROM java:8-jre

ADD telebot.jar /app/

ADD application.properties /app/

RUN tail /app/application.properties

CMD ["java", "-jar", "/app/telebot.jar"]

EXPOSE 8000
