# Telebot

Telegram bot for eve players to do your own alliance or corporate broadcasting 

You can find this guide in russian here - [версия этого гайда на русском](https://github.com/finnetrolle/telebot/wiki)
 
Current version: 0.1.0 beta

***I don't feel my english very good, so, if you have fixes for this page - feel free to send me using email or issues.***

## What is it?

This bot is a simple telegram notifier. The main goal is to deliver a system, that can replace jabber.
I hope once you can start bot and send anything to all your pals in ally or corporation.

Main feature of this bot is EVE API based authentication. You can define list of alliances or corporations (or both) 
you allow pilots register from. Every pilot must enter his keyID and vCode to define pilot name. By the way, telebot 
doesn't need full api key. Even minimal plan is enought.
 
Feel free to make donations in isks if you like this application. My character in Eve is 
[Finne Trolle](http://evewho.com/pilot/Finne+Trolle)

You can send me an issue using [issues page](https://github.com/finnetrolle/telebot/issues)

## Features
* Add alliances and corporations you allow pilots register from
* Promote and demote moderators - users, that can send broadcasts via telegram client
* Give your teammates link to bot and they can register without your help using eve keyId and vCode
* Use REST API to broadcast
* Connect to Jabber channel to listen and broadcast messages from

## How to set it up?
I'm working on making this process easier, but now we have what we have
* Create your bot using [this manual](https://core.telegram.org/bots#botfather). Remember your token and bot name
* Install bot using one of the installation guides
* Configure your application.properties file. Don't forget to set your superuser pilot name
* Start bot and send him something from your telegram application. Bot will ask you to register
* Register into bot using keyID and vCode. Select exactly the same pilot you set as superuser
* Add your alliance or corporation.
* Add some friendly alliances or corporations.
* Give your telegram bot's link to your friends (@BotFater will provide you with this link)
* Have fun!

## Configuration
Here you can find description of settings in application.properties file. This file is a main configuration keeper of
your service.
**WARNING** don't modify settings you don't see in this list.
### telegram settings
* **telegram.bot.token** - insert here token you receive from @BotFather
* **telegram.bot.username** - insert here name you receive from @BotFather
* **telegram.bot.alive** - set **true** to start bot
### bot settings
* **telebot.superuser** - superuser in-game name.  
* **telebot.broadcast.check-before-send** - set true to force check users ally and corp before every broadcast
### db connection settings
Do not modify this settings.
### application settings
* **server.port=8080** - insert here your special port for REST API if you want
### Jabber listener settings
Application is in early beta. Jabber listener is in extreme-alpha. It can work and can fail.
* **jabber.bot.nick** - jabber username
* **jabber.bot.password** - jabber password
* **jabber.bot.domain** - domain
* **jabber.bot.server** - host
* **jabber.bot.port** - port
* **jabber.bot.broadcaster** - name of jabber broadcaster user like jabberbot@jabber.goons.com 
* **jabber.bot.alive** - **true** to start bot 

## Installation
### from sources
* First ensure you have installed [maven](https://maven.apache.org), [git](https://git-scm.com) and [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
* Clone repo `git clone https://github.com/finnetrolle/telebot.git`
* Go to source folder `cd telebot`
* Build `mvn -U clean install`
* Create dir for bin files `mkdir bin`
* Copy application to bin `cp target/telebot.jar bin`
* Copy settings to bin `cp target/application.properties bin`
* Go to folder bin `cd bin`
* Configure application.properties in bin folder
* Run application `java -jar telebot.jar &`

Tomcat have a problem with slow startup. To fix it just install haveged
`sudo apt-get install haveged -y`

## Setting up your system in details

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

## Commands list

### For moderators
- **/addally** [ticker] *to add ally into list*
- **/rmally** [ticker] *to remove ally from list*
- **/addcorp** [id] *to add corp into list*
- **/rmcorp** [ticker] *to remove corp from list*
- **/cast** [message] *broadcast message to users*
- **/check** *check all users to find goaways*
- **/lu** *show list of registered users*
- **/promote** [name] *to promote pilot to moderator*
- **/demote** [name] *to demote pilot from moderator*

### For users
- **/la** *to show allys list*
- **/lc** *to show corp list*
- **/lm** *to show all moderators*
- **/help** *to show available commands*

