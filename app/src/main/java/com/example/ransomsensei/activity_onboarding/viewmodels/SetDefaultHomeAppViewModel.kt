package com.example.ransomsensei.activity_onboarding.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ransomsensei.activity_onboarding.util.Destination

class SetDefaultHomeAppViewModel() : ViewModel() {
    var nextDestination : Destination = Destination.Finish
}
