package com.ogdenscleaners.ogdenscleanersapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.repository.AccountRepository
import com.ogdenscleaners.ogdenscleanersapp.repository.DeliveryRepository
import com.ogdenscleaners.ogdenscleanersapp.repository.OrderRepository
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(sharedPreferences: SharedPreferences): AccountRepository {
        return AccountRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(@ApplicationContext context: Context): OrderRepository {
        return OrderRepository(context)
    }

    @Provides
    @Singleton
    fun provideDeliveryRepository(sharedPreferences: SharedPreferences): DeliveryRepository {
        return DeliveryRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providePaymentSheet(@ApplicationContext context: Context): PaymentSheet {
        // Initialize PaymentConfiguration before returning PaymentSheet
        PaymentConfiguration.init(context, "pk_test_51QEmC6F9q8Y1A3UES8uzimDczaKS3xMRUNr9QN4vhQN8wjktGMEONNrWWP7mFCJRrdYDmTPADDDVxn1GvS0mTkCw00XlEDwkSY")

        // PaymentSheet typically requires an Activity context, so it's ideal to manage PaymentSheet in the activity,
        // but if you want to provide it here, you can pass a mocked Activity context or make adjustments in how it's used.
        return PaymentSheet(context as AppCompatActivity)
    }
}
