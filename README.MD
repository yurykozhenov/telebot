# xXDEATHXx telegram bot

Telegram bot for notifications broadcasting.
 
Version 0.0 - pre alpha or even a proof oc concept. Using this version is very risky!

## What is it?

This bot is a simple telegram notifier. I hope once you can start bot and send anything to all your pals in ally

## Installation

- install and configure database (prefer postgres)
- create database and user for your telebot instance
- `git clone` application
- `mvn -U clean install -P production`
- add application.properties file
- `sudo rm /etc/init.d/telebot` if you already have previous version of telebot
- `sudo ln -s /path-to-app/telebot.jar /etc/init.d/telebot`
- `sudo service telebot start`

Tomcat have a problem with slow startup. To fix it just install haveged
`sudo apt-get install haveged -y`

## Next version named 'Users to manage' is 0.1

### Superuser in properties file
One user to rule them all. Moderator by default, can not be moved away. Can execute any command.

### Allowed ally and corp list
Periodically, system will check every user for corporation and alliance. If registered user's alliance or corporation
is in one of the lists - he'll get his broadcasts. In other way this user will be marked 'left' to ignore broadcasts. 
Marked users will be also removed from moderators list. User can remove his mark after welcoming in listed corp or ally.
But only other moderator can return moderator access back to him.

### Check-before-broadcast
In fact broadcast will be sent only to users without 'left' mark to make broadcast faster. But there will be a component
scanning every unmarked person with eve api to find renegades. Administrator will be able to set time between scans.

### Add/remove ally or corp from list
This feature will be available in bot's chat (only for moderators).

- **/addally** [id or name] *to add ally into list*
- **/rmally** [id or name] *to remove ally from list*
- **/la** *to show allys list*
- **/addcorp** [id or name] *to add corp into list*
- **/rmcorp** [id or name] *to remove corp from list*
- **/lc** *to show corp list*

### Unsecured API to make broadcast
You'll be able to make broadcast from unsecured endpoint like http://mybothost:8080/cast with POST method. 
No login and password is needed to access this endpoint so you should be careful.

### Add/remove moderators
This feature will give you a way to have more than one moderator.

- **/promote** [name] *to promote pilot to moderator*
- **/demote** [name] *to demote pilot from moderator*
- **/moders** *to show all moderators*

### RPM and easy install guide
Application is already is executable (it means you shouldn't exec java -jar), but you still need git and maven to build it.
In this patch you'll get RPM package with settings file and you'll can install it with help of easy install guide.

You will also have an opportunity to build application from sources with maven.

### Liquibase
Hibernate is ok with table creation, but if we want to keep your production database up-to-date with new versions of 
application, we must use liquibase.
 
### Optional enhancements
- It is a nice idea to make just one page to make registration an easy process because nobody want to copy keyID and vCode to phone
- Embedded database is perfect to make installation easy. H2 is nice and have a chance to replace postgres
- Jabber listener will listen to one jabber person (You'll can find a friend to your old jabber bot!)

