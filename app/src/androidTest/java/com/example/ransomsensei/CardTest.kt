package com.example.ransomsensei

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ransomsensei.data.dao.CardDao
import androidx.test.core.app.ApplicationProvider
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.Difficulty
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class CardTest {
    private lateinit var cardDao: CardDao
    private lateinit var db: RansomSenseiDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RansomSenseiDatabase::class.java
        ).build()
        cardDao = db.cardDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCardAndRead() = runTest {
        val card1 = Card(kanaValue = "いれます", kanjiValue = "入れます", englishValue = "to put in", difficulty = Difficulty.EASY)
        val card2 = Card(kanaValue = "たべます", kanjiValue = "食べます", englishValue = "to eat", difficulty = Difficulty.MEDIUM)

        cardDao.insertCards(card1, card2)
        val cards = cardDao.getAll()
        assertThat(cards, hasItems(card1, card2))
    }


    @Test
    @Throws(Exception::class)
    fun writeCardAndGetEasy() = runTest {
        val card1 = Card(kanaValue = "いれます", kanjiValue = "入れます", englishValue = "to put in", difficulty = Difficulty.EASY)
        val card2 = Card(kanaValue = "たべます", kanjiValue = "食べます", englishValue = "to eat", difficulty = Difficulty.MEDIUM)

        cardDao.insertCards(card1, card2)
        val cards = cardDao.loadAllEasy()
        assertThat(cards.get(0), equalTo(card1))
    }

    @Test
    @Throws(Exception::class)
    fun writeCardAndGetMedium() = runTest {
        val card1 = Card(kanaValue = "いれます", kanjiValue = "入れます", englishValue = "to put in", difficulty = Difficulty.EASY)
        val card2 = Card(kanaValue = "たべます", kanjiValue = "食べます", englishValue = "to eat", difficulty = Difficulty.MEDIUM)


        cardDao.insertCards(card1, card2)
        val cards = cardDao.loadAllMedium()
        assertThat(cards.get(0), equalTo(card2))
    }
}
