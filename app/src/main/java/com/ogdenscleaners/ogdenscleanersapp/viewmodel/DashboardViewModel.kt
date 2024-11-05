package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    // This ViewModel is ready for handling future data or complex business logic,
    // such as fetching user-specific features for the dashboard.
}
