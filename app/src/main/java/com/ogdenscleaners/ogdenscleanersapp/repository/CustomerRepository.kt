package com.ogdenscleaners.ogdenscleanersapp.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import com.ogdenscleaners.ogdenscleanersapp.models.AppCustomer
import com.ogdenscleaners.ogdenscleanersapp.models.AppCustomer.CreditCard

@Singleton
class CustomerRepository @Inject constructor() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val customersRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("customers")

    fun addCreditCardToCustomer(customerId: String, creditCard: CreditCard) {
        customersRef.child(customerId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val customer = task.result.getValue(AppCustomer::class.java)
                if (customer != null) {
                    val creditCards = customer.creditCards
                    creditCards.add(creditCard)
                    customer.creditCards = creditCards

                    customersRef.child(customerId).setValue(customer)
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {

                        }
                }
            }

        }
    }
}


