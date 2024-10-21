package com.ogdenscleaners.ogdenscleanersapp

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Stripe SDK with your publishable key
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_YOUR_PUBLISHABLE_KEY" // Replace with your actual publishable key
        )
    }
}
