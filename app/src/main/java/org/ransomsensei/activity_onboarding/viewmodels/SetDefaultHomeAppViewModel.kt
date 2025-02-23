package org.ransomsensei.activity_onboarding.viewmodels

import androidx.lifecycle.ViewModel
import org.ransomsensei.activity_onboarding.util.Destination

class SetDefaultHomeAppViewModel() : ViewModel() {
    var nextDestination : Destination = Destination.Finish
}
