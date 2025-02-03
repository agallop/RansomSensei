package com.example.ransomsensei.viewmodel.cardset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.CardSet
import com.example.ransomsensei.data.entity.CardSetStatus

class AddCardSetViewModel(val database: RansomSenseiDatabase) : ViewModel(){
    var cardSetName by mutableStateOf("")
    var status by mutableStateOf(CardSetStatus.ENABLED)

     suspend fun addCardSet() {
         database.cardSetDao().insertCardSet(CardSet(0, cardSetName, status))
     }
}