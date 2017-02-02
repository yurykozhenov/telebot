package ru.finnetrolle.telebot.service.external

import org.junit.Test

import org.junit.Assert.*
import org.junit.Ignore

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */





/**
 * Licence: MIT
 * Legion of xXDEATHXx notification bot for telegram
 * Created by maxsyachin on 19.03.16.
 */
class EveApiConnectorTest {

    val eve = EveApiConnector()

    @Test
    @Ignore
    fun testGetCharacters() {
        assert(eve.getCharacters(0, "") == null)
        assert(eve.getCharacter(90659919) != null)
        assert(eve.getCharacter(90659919).characterName == "Finne Trolle")
    }

    @Test
    @Ignore
    fun testGetCorpId() {
        assert(eve.getCorpId(0) == 0L)
    }

    @Test
    fun testIsAllianceExist() {
        assert(eve.isAllianceExist("X.I.X") == true)
        assert(eve.isAllianceExist("NONONO")== false)
    }

    @Test
    fun testGetAlliance() {
        assert(eve.getAlliance("X.I.X")!!.name.equals("Legion of xXDEATHXx"))
        assert(eve.getAlliance("NONONO")==null)
    }

    @Test
    fun testGetCorporation() {
        assert(eve.getCorporation(877122797)!!.ticker.equals("XDSQX"))
        assert(eve.getCorporation(-1) == null)
    }
}