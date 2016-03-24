# xXDEATHXx telegram bot

Telegram bot for notifications broadcasting.
 
Version 0.0 - pre alpha. Using this version is very risky!

## What is it?

This bot is a simple telegram notifier. The main goal is to deliver a system, that can replace jabber.
I hope once you can start bot and send anything to all your pals in ally or corporation.

## Installation

**Current version have some difficulties with application.properties file.** You should copy this file from resources
directory, configure and put near your .jar. But don't worry - RPM is on the way!

- install and configure database (prefer postgres)
- create database and user for your telebot instance
- `git clone` application
- `mvn -U clean install`
- add application.properties file
- `sudo rm /etc/init.d/telebot` if you already have previous version of telebot
- `sudo ln -s /path-to-app/telebot.jar /etc/init.d/telebot`
- `sudo service telebot start`

Tomcat have a problem with slow startup. To fix it just install haveged
`sudo apt-get install haveged -y`

## Service configuration

## Setting up your system

### Super user
Your service need special user that nobody can demote. This user is moderator by default and even if one of your 
moderators dicide to kick all other moderators you still have a chance to take control. 
Set eve character name into superuser property to enable superuser. 

Next step is to attach your telegram account to this character. Open telegram and register using **/register** 
command with your keyId and vCode. Without saying you should select character with superuser's name. If you want to
show your character's name in moderator list - **/promote** yourself (This is not necessary to superuser to be a moderator)

### Add your first alliance or corporation
At first your service have empty lists for allowed alliances and corporations. This is a time when every person having
Eve Online character can register into your system. To prevent registering for all except your friends you should add
some alliances or corporations into service. 

Use **/addally** and **/addcorp** commands to fulfill your allowed lists. If you have at least one alliance or 
one corporation, nobody can register except the pilots of added alliance / corporation.

### Promote moderators
You probably need help of your friends to make broadcasts, remove alliances and corporations from allowed lists and 
make a coffee. Add some helpers using **/promote** command. Your moderators can promote and demote any person except 
super user, so your person is safe. But don't make your servants a slaves because they can make some problems 
for your system.

## Next version named 'Users to manage' is 0.1

### TBD

#### Docker instead of RPM
Because it is easier to distribute

#### TESTS!!!
We need more unit tests at least

#### Easy registration 
It is a nice idea to make just one page to make registration an easy process because nobody want to copy 
keyID and vCode to phone client

### Done

#### Superuser in properties file
One user to rule them all. Moderator by default, can not be moved away. Can execute any command.

#### Allowed ally and corp list
Periodically, system will check every user for corporation and alliance. If registered user's alliance or corporation
is in one of the lists - he'll get his broadcasts. In other way this user will be marked 'left' to ignore broadcasts. 
Marked users will be also removed from moderators list. User can remove his mark after welcoming in listed corp or ally.
But only other moderator can return moderator access back to him.

#### Check-before-broadcast
In fact broadcast will be sent only to users without 'left' mark to make broadcast faster. But there will be a component
scanning every unmarked person with eve api to find renegades. Administrator will be able to set time between scans.

#### Add/remove ally or corp from list
This feature will be available in bot's chat (only for moderators).

- **/addally** [ticker] *to add ally into list*
- **/rmally** [ticker] *to remove ally from list*
- **/la** *to show allys list*
- **/addcorp** [id] *to add corp into list*
- **/rmcorp** [ticker] *to remove corp from list*
- **/lc** *to show corp list*

#### Unsecured API to make broadcast
You'll be able to make broadcast from unsecured endpoint like http://mybothost:8080/cast with POST method. 
No login and password is needed to access this endpoint so you should be careful.

#### Add/remove moderators
This feature will give you a way to have more than one moderator.

- **/promote** [name] *to promote pilot to moderator*
- **/demote** [name] *to demote pilot from moderator*
- **/lm** *to show all moderators*

#### Liquibase
Hibernate is ok with table creation, but if we want to keep your production database up-to-date with new versions of 
application, we must use liquibase.
 
## Ideas pool
- Embedded database is perfect to make installation easy. H2 is nice and have a chance to replace postgres
- Jabber listener will listen to one jabber person (You'll can find a friend to your old jabber bot!)

