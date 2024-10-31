package com.ogdenscleaners.ogdenscleanersapp

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Replace with your actual publishable key
        PaymentConfiguration.init(
            applicationContext,
            "pk_live_51QEmC6F9q8Y1A3UEvXwzb4Y3JPBfczyXhHgMRpn7LWAUQhkaYnD3MQC6giFb0UeLRtil4DuyFNI0HMJ4N85wMzqI00sJ2IvXEh"
        )
    }
}
