/*
 *  Copyright (c) 2025 Anthony Gallop <agallopdev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ransomsensei.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.ransomsensei.data.RansomSenseiDatabase
import org.ransomsensei.data.entity.Card
import org.ransomsensei.data.entity.CardSet
import org.ransomsensei.data.entity.CardSetStatus
import org.ransomsensei.data.entity.Difficulty
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
            cardSetId = 2,
            cardId = 2,
            kanaValue = "たべます",
            kanjiValue = "食べます",
            englishValue = "to eat",
            difficulty = Difficulty.MEDIUM
        )
        cardSetDao.insertCardSet(cardSet1)
        cardSetDao.insertCardSet(cardSet2)

        cardDao.insertCard(card1)
        cardDao.insertCard(card2)

        val card = cardDao.getRandomActive()

        assertThat(card).isEqualTo(card1)
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
        cardDao.insertCard(card1)
        cardDao.insertCard(card2)

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

        cardDao.insertCard(card1)
        cardDao.insertCard(card2)

        cardsInSetFlow.test {
            val emission: List<Card> = awaitItem()
            assertThat(emission).containsExactly(card1, card2)
        }
    }
}
