package com.ogdenscleaners.ogdenscleanersapp.di

import android.content.Context
import android.content.SharedPreferences
import com.ogdenscleaners.ogdenscleanersapp.repository.AccountRepository
import com.ogdenscleaners.ogdenscleanersapp.repository.DeliveryRepository
import com.ogdenscleaners.ogdenscleanersapp.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideOrderRepository(): OrderRepository {
        return OrderRepository()
    }

    @Provides
    @Singleton
    fun provideDeliveryRepository(sharedPreferences: SharedPreferences): DeliveryRepository {
        return DeliveryRepository(sharedPreferences)
    }
}
