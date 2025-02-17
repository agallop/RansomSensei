package com.example.ransomsensei.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay

@RunWith(AndroidJUnit4::class)
class CardSetDaoTest {
    private lateinit var cardSetDao: CardSetDao
    private lateinit var cardDao: CardDao
    private lateinit var db: RansomSenseiDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RansomSenseiDatabase::class.java
        ).build()

        cardSetDao = db.cardSetDao()
        cardDao = db.cardDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun createReadUpdateDelete() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1",
            cardSetStatus = CardSetStatus.ENABLED
        )
        cardSetDao.insertCardSet(cardSet1)

        assertThat(cardSetDao.getCardSet(1)).isEqualTo(cardSet1)

        val updatedCardSet1 = cardSet1.copy(cardSetName = "updatedCardSet", cardSetStatus = CardSetStatus.DISABLED)
        cardSetDao.updateCardSet(updatedCardSet1)

        assertThat(cardSetDao.getCardSet(1)).isEqualTo(updatedCardSet1)

        cardSetDao.deleteCardSets(listOf(updatedCardSet1))

        assertThat(cardSetDao.getCardSet(1)).isNull()
    }

    @Test
    fun getAll() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1"
        )
        val cardSet2 = CardSet(
            cardSetId = 2,
            cardSetName = "cardSet2"
        )
        cardSetDao.insertCardSet(cardSet1)
        cardSetDao.insertCardSet(cardSet2)

        val cardSets = cardSetDao.getAll()
        assertThat(cardSets).containsExactly(cardSet1, cardSet2)
    }

    @Test
    fun getAllFlow() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1"
        )

        val cardSetsFlow = cardSetDao.getAllFlow();

        cardSetsFlow.test {
            val emission: List<CardSet> = awaitItem()
            assertThat(emission).isEmpty()
        }

        cardSetDao.insertCardSet(cardSet1)

        cardSetsFlow.test {
            val emission: List<CardSet> = awaitItem()
            assertThat(emission).containsExactly(cardSet1)
        }
    }

    @Test
    fun getCardSet() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1"
        )

        cardSetDao.insertCardSet(cardSet1)

        val cardSet = cardSetDao.getCardSet(1)

        assertThat(cardSet).isEqualTo(cardSet1)
    }

    @Test
    fun getCardSetFlow() = runTest {
        val cardSet1 = CardSet(
            cardSetId = 1,
            cardSetName = "cardSet1",
            cardSetStatus = CardSetStatus.DISABLED
        )
        val cardSetsFlow = cardSetDao.getCardSetFlow(1);

        cardSetsFlow.test {
            val emission: CardSet = awaitItem()
            assertThat(emission).isNull()
        }

        cardSetDao.insertCardSet(cardSet1)

        cardSetsFlow.test {
            val emission: CardSet = awaitItem()
            assertThat(emission).isEqualTo(cardSet1)
        }

        cardSetDao.updateCardSet(cardSet1.copy(cardSetStatus = CardSetStatus.ENABLED))

        cardSetsFlow.test {
            val emission: CardSet = awaitItem()
            assertThat(emission).isEqualTo(cardSet1.copy(cardSetStatus = CardSetStatus.ENABLED))
        }

    }
}
