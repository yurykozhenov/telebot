package ru.finnetrolle.telebot.service.external

import org.junit.Test

import org.junit.Assert.*

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
    fun testGetCharacters() {
        assert(eve.getCharacters(0, "") == null)
    }

    @Test
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

    @Test
    fun getAllyList() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getCharacters() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getCharacter() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getCorpId() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getAlliances() {
        throw UnsupportedOperationException()
    }

    @Test
    fun isAllianceExist() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getAlliance() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getCorporation() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getMailList() {
        throw UnsupportedOperationException()
    }

    @Test
    fun getMailBody() {
        throw UnsupportedOperationException()
    }
}