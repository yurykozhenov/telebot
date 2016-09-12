FROM java:8-jre
MAINTAINER Max Syachin <maxsyachin@gmail.com>

ADD ./target/telebot.jar /app/

CMD ["java", "-jar", "/app/telebot.jar"]

EXPOSE 8080