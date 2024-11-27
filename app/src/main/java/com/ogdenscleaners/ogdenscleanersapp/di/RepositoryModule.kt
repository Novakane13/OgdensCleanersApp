package com.ogdenscleaners.ogdenscleanersapp.di

import com.ogdenscleaners.ogdenscleanersapp.api.ApiService
import com.ogdenscleaners.ogdenscleanersapp.repository.AuthRepository
import com.ogdenscleaners.ogdenscleanersapp.repository.BillingRepository
import com.ogdenscleaners.ogdenscleanersapp.repository.RegisterRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        AuthRepository(firebaseAuth)

    @Provides
    @Singleton
    fun provideRegisterRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RegisterRepository = RegisterRepository(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideBillingRepository(apiService: ApiService): BillingRepository =
        BillingRepository(apiService)
}
