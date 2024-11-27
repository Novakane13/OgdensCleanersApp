package com.ogdenscleaners.ogdenscleanersapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.ogdenscleaners.ogdenscleanersapp.R
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
}
