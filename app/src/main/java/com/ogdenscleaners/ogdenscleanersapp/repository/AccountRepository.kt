package com.ogdenscleaners.ogdenscleanersapp.repository

import android.content.SharedPreferences
import com.ogdenscleaners.ogdenscleanersapp.models.AppCustomer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getUserInfo(): AppCustomer {
        return withContext(Dispatchers.IO) {
            val customerId = sharedPreferences.getString("customerId", null)
            val name = sharedPreferences.getString("name", null)
            val phone = sharedPreferences.getString("phone", null)
            val email = sharedPreferences.getString("email", null)
            val notes = sharedPreferences.getString("notes", null)
            val creditCardsJson = sharedPreferences.getString("creditCards", null)
            val creditCards = if (!creditCardsJson.isNullOrEmpty()) {
                deserializeCreditCards(creditCardsJson)
            } else {
                mutableListOf()
            }
            AppCustomer(customerId, name, phone, email, notes, creditCardsJson, creditCards)
        }
    }

    suspend fun saveUserInfo(userInfo: AppCustomer) {
        withContext(Dispatchers.IO) {
            val creditCardsJson = serializeCreditCards(userInfo.creditCards)
            sharedPreferences.edit().apply {
                putString("customerId", userInfo.customerId)
                putString("name", userInfo.name)
                putString("phone", userInfo.phone)
                putString("email", userInfo.email)
                putString("notes", userInfo.notes)
                putString("creditCards", creditCardsJson)
                apply()
            }
        }
    }

    suspend fun getSavedCreditCards(): List<AppCustomer.CreditCard> {
        return withContext(Dispatchers.IO) {
            val savedCards = mutableListOf<AppCustomer.CreditCard>()
            val cardsJson = sharedPreferences.getString("cards", null)
            if (cardsJson != null) {
                val jsonArray = JSONArray(cardsJson)
                for (i in 0 until jsonArray.length()) {
                    val cardJson = jsonArray.getJSONObject(i)
                    val card = AppCustomer.CreditCard(
                        cardholderName = cardJson.getString("cardholderName"),
                        lastFourDigits = cardJson.getString("lastFourDigits"),
                        expirationDate = cardJson.getString("expiryDate"),
                        cardToken = cardJson.optString("cardToken")
                    )
                    savedCards.add(card)
                }
            }
            savedCards
        }
    }

    suspend fun saveCreditCards(cards: List<AppCustomer.CreditCard>) {
        withContext(Dispatchers.IO) {
            val jsonArray = JSONArray()
            cards.forEach { card ->
                val cardJson = JSONObject().apply {
                    put("cardholderName", card.cardholderName)
                    put("lastFourDigits", card.lastFourDigits)
                    put("expiryDate", card.expirationDate)
                    put("cardToken", card.cardToken)
                }
                jsonArray.put(cardJson)
            }
            sharedPreferences.edit().putString("cards", jsonArray.toString()).apply()
        }
    }

    private fun deserializeCreditCards(json: String): MutableList<AppCustomer.CreditCard> {
        val creditCards = mutableListOf<AppCustomer.CreditCard>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val card = AppCustomer.CreditCard(
                cardholderName = jsonObject.getString("cardholderName"),
                lastFourDigits = jsonObject.getString("lastFourDigits"),
                expirationDate = jsonObject.getString("expiryDate"),
                cardToken = jsonObject.optString("cardToken") // Optional field
            )
            creditCards.add(card)
        }
        return creditCards
    }

}

    private fun serializeCreditCards(creditCards: List<AppCustomer.CreditCard>): String {
        val jsonArray = JSONArray()
        creditCards.forEach { card ->
            val jsonObject = JSONObject().apply {
                put("cardholderName", card.cardholderName)
                put("lastFourDigits", card.lastFourDigits)
                put("expiryDate", card.expirationDate)
                put("cardToken", card.cardToken)
            }
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

