package ru.finnetrolle.telebot.service.message

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.telegramapi.UserService
import ru.finnetrolle.telebot.util.MessageLocalization
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 21.03.16.
 */

@Component
class TelebotServantManager @Autowired constructor(
        val processor: MessageProcessor,
        val userService: UserService,
        val loc: MessageLocalization
) : ServantManager() {

    @PostConstruct
    override fun configure() {
        registerServant(
                Servantee("/ADDALLY", { c -> makeBackMessage(c, processor.addAlly(c.data)) }, true, loc.getMessage("telebot.command.description.addally")),
                Servantee("/RMALLY", { c -> makeBackMessage(c, processor.rmAlly(c.data)) }, true, loc.getMessage("telebot.command.description.rmally")),
                Servantee("/ADDCORP", { c -> makeBackMessage(c, processor.addCorp(c.data)) }, true, loc.getMessage("telebot.command.description.addcorp")),
                Servantee("/RMCORP", { c -> makeBackMessage(c, processor.rmCorp(c.data)) }, true, loc.getMessage("telebot.command.description.rmcorp")),
                Servantee("/CHECK", { c -> makeBackMessage(c, processor.checkAll()) }, true, loc.getMessage("telebot.command.description.check")),
                Servantee("/DEM", { c -> makeBackMessage(c, processor.demote(c.data)) }, true, loc.getMessage("telebot.command.description.dem")),
                Servantee("/PRO", { c -> makeBackMessage(c, processor.promote(c.data)) }, true, loc.getMessage("telebot.command.description.pro")),
                Servantee("/RENEGADE", { c -> makeBackMessage(c, processor.renegade(c.data)) }, true, loc.getMessage("telebot.command.description.renegade")),
                Servantee("/LEGALIZE", { c -> makeBackMessage(c, processor.legalize(c.data)) }, true, loc.getMessage("telebot.command.description.legalize")),
                Servantee("/LU", { c -> makeBackMessage(c, processor.listOfUsers()) }, true, loc.getMessage("telebot.command.description.lu")),
                Servantee("/CAST", { c -> makeBroadcast(userService.getLegalUsers(), c.data) }, true, loc.getMessage("telebot.command.description.cast")),
                Servantee("/SHOWGROUP", { c-> makeBackMessage(c, userService.showGroup(c.data))}, true, loc.getMessage("telebot.command.description.showgroup")),

                Servantee("/JOKE", { c -> makeBackMessage(c, processor.joke()) }, false, loc.getMessage("telebot.command.description.joke")),
                Servantee("/LM", { c -> makeBackMessage(c, processor.listOfModerators()) }, false, loc.getMessage("telebot.command.description.lm")),
                Servantee("/LA", { c -> makeBackMessage(c, processor.listOfAlliances()) }, false, loc.getMessage("telebot.command.description.la")),
                Servantee("/LC", { c -> makeBackMessage(c, processor.listOfCorporations()) }, false, loc.getMessage("telebot.command.description.lc")),
                Servantee("/HELP", { c -> makeBackMessage(c, loc.getMessage("messages.help") + this.help(userService.isModerator(c.telegramUserId))) }, false, loc.getMessage("telebot.command.description.help")),
                Servantee("/MAIL", { c -> makeBackMessage(c, processor.lastMail())}, false, loc.getMessage("telebot.command.description.mail"))
                )
    }

    private fun makeBackMessage(command: Command, text: String): List<SendMessage> =
        listOf(MessageBuilder.build(command.fromChatId, text))

    private fun makeBroadcast(pilots: List<Pilot>, text: String): List<SendMessage> =
        pilots.map { p -> MessageBuilder.build(p.id.toString(), text) }

    override fun getDefaultServant(): (Command) -> List<SendMessage> =
            { c -> makeBackMessage(c, loc.getMessage("messages.unknown")) }

    override fun getAccessDeniedServant(): (Command) -> List<SendMessage> =
            { c -> makeBackMessage(c, loc.getMessage("messages.access.denied")) }

    override fun getAccessChecker(): (Command) -> Boolean =
            { c -> userService.isModerator(c.telegramUserId)}


}