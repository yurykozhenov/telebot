package ru.finnetrolle.telebot.service.message

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.api.methods.SendMessage
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.telegramapi.UserService
import javax.annotation.PostConstruct

/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 21.03.16.
 */

@Component
class TelebotServantManager @Autowired constructor(
        val processor: MessageProcessor,
        val userService: UserService
) : ServantManager() {

    @PostConstruct
    override fun configure() {
        registerServant(
                Servantee("/ADDALLY", { c -> makeBackMessage(c, processor.addAlly(c.data)) }, true),
                Servantee("/RMALLY", { c -> makeBackMessage(c, processor.rmAlly(c.data)) }, true),
                Servantee("/ADDCORP", { c -> makeBackMessage(c, processor.addCorp(c.data)) }, true),
                Servantee("/RMCORP", { c -> makeBackMessage(c, processor.rmCorp(c.data)) }, true),
                Servantee("/CHECK", { c -> makeBackMessage(c, processor.checkAll()) }, true),
                Servantee("/DEM", { c -> makeBackMessage(c, processor.demote(c.data)) }, true),
                Servantee("/PRO", { c -> makeBackMessage(c, processor.promote(c.data)) }, true),
                Servantee("/RENEGADE", { c -> makeBackMessage(c, processor.renegade(c.data)) }, true),
                Servantee("/LEGALIZE", { c -> makeBackMessage(c, processor.legalize(c.data)) }, true),
                Servantee("/LU", { c -> makeBackMessage(c, processor.listOfUsers()) }, true),
                Servantee("/CAST", { c -> makeBroadcast(userService.getLegalUsers(), c.data) }, true),

                Servantee("/JOKE", { c -> makeBackMessage(c, processor.joke()) }),
                Servantee("/LM", { c -> makeBackMessage(c, processor.listOfModerators()) }),
                Servantee("/LA", { c -> makeBackMessage(c, processor.listOfAlliances()) }),
                Servantee("/LC", { c -> makeBackMessage(c, processor.listOfCorporations()) }),
                Servantee("/HELP", { c -> makeBackMessage(c, this.help(userService.isModerator(c.telegramUserId))) })
                )
    }

    private fun makeBackMessage(command: Command, text: String): List<SendMessage> =
        listOf(MessageBuilder.build(command.fromChatId, text))

    private fun makeBroadcast(pilots: List<Pilot>, text: String): List<SendMessage> =
        pilots.map { p -> MessageBuilder.build(p.id.toString(), text) }

    override fun getDefaultServant(): (Command) -> List<SendMessage> =
            { c -> makeBackMessage(c, Messages.UNKNOWN) }

    override fun getAccessDeniedServant(): (Command) -> List<SendMessage> =
            { c -> makeBackMessage(c, Messages.ACCESS_DENIED) }

    override fun getAccessChecker(): (Command) -> Boolean =
            { c -> userService.isModerator(c.telegramUserId)}


}