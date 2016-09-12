# Telebot

[![Build Status](https://travis-ci.org/finnetrolle/telebot.svg?branch=master)](https://travis-ci.org/finnetrolle/telebot)
[![codecov](https://codecov.io/gh/finnetrolle/telebot/branch/master/graph/badge.svg)](https://codecov.io/gh/finnetrolle/telebot)

Telegram bot for eve players to do your own alliance or corporation broadcasting 
 
Current version: 0.3.0 **unstable beta**

## What is it?

This bot is a simple telegram notifier. The main goal is to deliver a system, that can replace jabber.
I hope once you can start bot and send anything to all your pals in ally or corporation.
 
Feel free to make donations in isks if you like this application. My character in Eve is 
[Finne Trolle](http://evewho.com/pilot/Finne+Trolle)

You can send me an issue using [issues page](https://github.com/finnetrolle/telebot/issues)

## Features
* External registration!
* Add alliances and corporations you allow pilots register from
* Promote and demote moderators - users, that can send broadcasts via telegram client
* Connect to Jabber channel to listen and broadcast messages from
* Check eve ingame mail

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

## Patch notes

[0.3.X - global refactoring](https://github.com/finnetrolle/telebot/blob/master/docs/patchnotes/0.3.X.MD)

[0.2.X - preparing to release](https://github.com/finnetrolle/telebot/blob/master/docs/patchnotes/0.2.X.MD)

[0.1.X - proof of concept](https://github.com/finnetrolle/telebot/blob/master/docs/patchnotes/0.1.X.MD)


