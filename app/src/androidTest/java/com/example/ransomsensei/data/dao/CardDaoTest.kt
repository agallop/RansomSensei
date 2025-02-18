package com.example.ransomsensei.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import com.example.ransomsensei.data.entity.Difficulty
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class CardDaoTest {
    private lateinit var cardSetDao: CardSetDao
    private lateinit var cardDao: CardDao
    private lateinit var db: RansomSenseiDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RansomSenseiDatabase::class.java
        ).build()
        cardDao = db.cardDao()
        cardSetDao = db.cardSetDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getRandomActive() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1",
            cardSetStatus = CardSetStatus.ENABLED
        )
        val cardSet2 = CardSet(
            cardSetId = 2,
            cardSetName = "cardSet2",
            cardSetStatus = CardSetStatus.DISABLED
        )
        val card1 = Card(
            cardSetId = 1,
            cardId = 1,
            kanaValue = "いれます",
            kanjiValue = "入れます",
            englishValue = "to put in",
            difficulty = Difficulty.EASY
        )
        val card2 = Card(
            cardSetId = 1,
            cardId = 2,
            kanaValue = "たべます",
            kanjiValue = "食べます",
            englishValue = "to eat",
            difficulty = Difficulty.MEDIUM
        )
        val card3 = Card(
            cardSetId = 2,
            cardId = 3,
            kanaValue = "食べます",
            kanjiValue = "たべます",
            englishValue = "to eat",
            difficulty = Difficulty.EASY
        )
        cardSetDao.insertCardSet(cardSet1)
        cardSetDao.insertCardSet(cardSet2)
        cardDao.insertCards(card1, card2, card3)

        val card = cardDao.getRandomActive()

        assertThat(card).isIn(listOf( card1, card2))
    }

    @Test
    fun getCardsInSet() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1",
            cardSetStatus = CardSetStatus.ENABLED
        )
        val card1 = Card(
            cardSetId = 1,
            cardId = 1,
            kanaValue = "いれます",
            kanjiValue = "入れます",
            englishValue = "to put in",
            difficulty = Difficulty.EASY
        )
        val card2 = Card(
            cardSetId = 1,
            cardId = 2,
            kanaValue = "たべます",
            kanjiValue = "食べます",
            englishValue = "to eat",
            difficulty = Difficulty.MEDIUM
        )

        cardSetDao.insertCardSet(cardSet1)
        cardDao.insertCards(card1, card2)

        val cards = cardDao.getCardsInSet(1)

        assertThat(cards).containsExactly(card1, card2)
    }

    @Test
    fun getCardsInSetFlow() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1",
            cardSetStatus = CardSetStatus.ENABLED
        )

        cardSetDao.insertCardSet(cardSet1)

        val cardsInSetFlow = cardDao.getCardsInSetFlow(1)
        cardsInSetFlow.test {
            val emission: List<Card> = awaitItem()
            assertThat(emission).isEmpty()
        }

        val card1 = Card(
            cardSetId = 1,
            cardId = 1,
            kanaValue = "いれます",
            kanjiValue = "入れます",
            englishValue = "to put in",
            difficulty = Difficulty.EASY
        )
        val card2 = Card(
            cardSetId = 1,
            cardId = 2,
            kanaValue = "たべます",
            kanjiValue = "食べます",
            englishValue = "to eat",
            difficulty = Difficulty.MEDIUM
        )

        cardDao.insertCards(card1, card2)

        cardsInSetFlow.test {
            val emission: List<Card> = awaitItem()
            assertThat(emission).containsExactly(card1, card2)
        }
    }
}
