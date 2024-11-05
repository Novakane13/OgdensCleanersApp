package com.ogdenscleaners.ogdenscleanersapp.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ogdenscleaners.ogdenscleanersapp.models.Customer
import com.ogdenscleaners.ogdenscleanersapp.models.Customer.CreditCard

class CustomerRepository {
    private val customersRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("customers")

    fun addCreditCardToCustomer(customerId: String, creditCard: CreditCard) {
        customersRef.child(customerId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val customer = task.result.getValue(Customer::class.java)
                if (customer != null) {
                    val creditCards = customer.creditCards ?: mutableListOf()
                    creditCards.add(creditCard)
                    customer.creditCards = creditCards
                    customersRef.child(customerId).setValue(customer)
                        .addOnSuccessListener {
                            // Successfully added card
                        }
                        .addOnFailureListener {
                            // Handle the error
                        }
                }
            } else {
                // Handle the error
            }
        }
    }
}
