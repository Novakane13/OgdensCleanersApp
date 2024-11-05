package com.ogdenscleaners.ogdenscleanersapp.repository

import android.content.SharedPreferences
import com.ogdenscleaners.ogdenscleanersapp.models.Customer
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun getUserInfo(): Customer.UserInfo {
        val name = sharedPreferences.getString("name", "") ?: ""
        val phone = sharedPreferences.getString("phone", "") ?: ""
        val email = sharedPreferences.getString("email", "") ?: ""
        val notes = sharedPreferences.getString("notes", "") ?: ""
        return Customer.UserInfo(name, phone, email, notes)
    }

    fun saveUserInfo(userInfo: Customer.UserInfo) {
        sharedPreferences.edit().apply {
            putString("name", userInfo.name)
            putString("phone", userInfo.phone)
            putString("email", userInfo.email)
            putString("notes", userInfo.notes)
            apply()
        }
    }

    fun getSavedCreditCards(): List<Customer.CreditCard> {
        val savedCards = mutableListOf<Customer.CreditCard>()
        val cardsJson = sharedPreferences.getString("cards", null)
        if (cardsJson != null) {
            val jsonArray = JSONArray(cardsJson)
            for (i in 0 until jsonArray.length()) {
                val cardJson = jsonArray.getJSONObject(i)
                val card = Customer.CreditCard(
                    cardholderName = cardJson.getString("cardholderName"),
                    lastFourDigits = cardJson.getString("lastFourDigits"),
                    expirationDate = cardJson.getString("expiryDate"),
                    cardToken = cardJson.optString("cardToken")
                )
                savedCards.add(card)
            }
        }
        return savedCards
    }
}
