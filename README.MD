# xXDEATHXx telegram bot

Telegram bot for notifications broadcasting

## Installation

- install and configure postgresql database
- create database and user for your telebot instance
- git clone application
- mvn -U clean install
- add application.properties file
- java -jar bot.jar &




## Known issues

Tomcat have a problem with slow startup. To fix it just install haveged
sudo apt-get install haveged -y