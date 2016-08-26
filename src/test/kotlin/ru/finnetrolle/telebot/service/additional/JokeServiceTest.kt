package ru.finnetrolle.telebot.service.additional

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.test.util.ReflectionTestUtils
import ru.finnetrolle.telebot.model.Joke
import ru.finnetrolle.telebot.model.JokeRepository
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */
class JokeServiceTest {

    @Mock private lateinit var loc: MessageLocalization
    @Mock lateinit private var repo: JokeRepository

    @InjectMocks private var service = JokeService()

    private val TEXT: String = "Some text, bro"
    private val PILOT_NAME: String = "Vasya"
    private val PILOT_ID: Long = 100L
    private val PILOT = Pilot(characterName = PILOT_NAME, characterId = PILOT_ID)
    private val JOKE = Joke(fromName = PILOT_NAME, fromId = PILOT_ID, text = TEXT)

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(loc.getMessage(Mockito.anyString())).thenAnswer { a -> a.arguments[0] }
        Mockito.`when`(loc.getMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenAnswer { a -> a.arguments[0] }
        Mockito.`when`(repo.save(Mockito.any(Joke::class.java))).thenAnswer { a -> a.arguments[0] }
    }

    @Test
    fun userCanAddJoke() {
        Mockito.`when`(repo.findAll()).thenReturn(listOf(JOKE))
        assertEquals(true, service.addJoke(PILOT, TEXT))
        Mockito.verify(repo, Mockito.times(1)).save(Mockito.any(Joke::class.java))
        Mockito.verify(repo, Mockito.times(1)).findAll()
    }

    @Test
    fun returnFalseIfSomeException() {
        Mockito.`when`(repo.save(Mockito.any(Joke::class.java))).thenThrow(RuntimeException::class.java)
        assertEquals(false, service.addJoke(PILOT, TEXT))
    }

    @Test
    fun jokeInsertionChangesCurrentPool() {
        Mockito.`when`(repo.findAll()).thenReturn(listOf(JOKE))
        Mockito.`when`(repo.findAll()).thenReturn(listOf(JOKE, JOKE))
        assertEquals(true, service.addJoke(PILOT, TEXT))
        assertEquals(true, service.addJoke(PILOT, TEXT))
        assertEquals(2, (ReflectionTestUtils.getField(service, "jokes") as List<*>).size )
        Mockito.verify(repo, Mockito.times(2)).save(Mockito.any(Joke::class.java))
        Mockito.verify(repo, Mockito.times(2)).findAll()
    }

    @Test
    fun userCanGetJoke() {
        ReflectionTestUtils.setField(service, "jokes", listOf(JOKE))
        assertEquals("telebot.joke", service.joke())
    }

}