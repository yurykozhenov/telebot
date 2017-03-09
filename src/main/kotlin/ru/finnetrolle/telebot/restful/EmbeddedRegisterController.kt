package ru.finnetrolle.telebot.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import ru.finnetrolle.telebot.service.external.EveApiConnector
import ru.finnetrolle.telebot.service.external.ExternalRegistrationService

/**
 * Created by finnetrolle on 09.03.2017.
 */


@Controller
@RequestMapping("/signup")
class EmbeddedRegisterController {

    @Autowired
    private lateinit var service: ExternalRegistrationService

    @Autowired
    private lateinit var eve: EveApiConnector

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun register(model: Model,
                 @RequestParam(value = "keyid", required = true) keyId: Int,
                 @RequestParam(value = "vcode", required = true) vCode: String,
                 @RequestParam(value = "cname", required = true) cName: String): String {
        val contender = eve.getCharacters(keyId, vCode)
                ?.filter { it.name.toUpperCase() == cName.toUpperCase() }
                ?.first()
        contender?.let {
            model.addAttribute("code", service.registerContender(it.name, it.id))
        }
        return "telecode"
    }
}