# xXDEATHXx telegram bot

Telegram bot for notifications broadcasting.
 
Version 0.0.x - pre alpha. Using this version is very risky!

## What is it?

This bot is a simple notifier via telegram. I hope once you can start bot and send anything to all your pals in ally

## Installation

- install and configure database (prefer postgres)
- create database and user for your telebot instance
- 'git clone' application
- 'mvn -U clean install -P production'
- add application.properties file
- 'sudo ln -s /path-to-app/telebot.jar /etc/init.d/telebot'
- 'sudo service telebot start'

Tomcat have a problem with slow startup. To fix it just install haveged
'sudo apt-get install haveged -y'

## In 0.1.0 version
- superuser in properties file
- allowed ally list
- allowed corp list
- check-before-broadcast
- add/remove ally from list
- add/remove corp from list
- unsecured API to make broadcast
- add/remove moderators
- RPM and easy install guide
- liquibase


