package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ogdenscleaners.ogdenscleanersapp.adapters.CreditCardAdapter
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityAccountInfoBinding
import com.ogdenscleaners.ogdenscleanersapp.models.AppCustomer // Renamed for clarity and removed the conflicting import
import com.ogdenscleaners.ogdenscleanersapp.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountInfoBinding
    private val accountViewModel: AccountViewModel by viewModels()
    private lateinit var cardAdapter: CreditCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the adapter for the RecyclerView
        cardAdapter = CreditCardAdapter(mutableListOf())
        binding.savedCardsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.savedCardsRecyclerView.adapter = cardAdapter

        // Observing user info and saved cards
        accountViewModel.userInfo.observe(this, Observer { userInfo: AppCustomer? ->
            userInfo?.let {
                binding.editTextName.setText(it.name)
                binding.editTextPhone.setText(it.phone)
                binding.editTextEmail.setText(it.email)
                binding.editTextAddress.setText(it.address)
                binding.editTextNotes.setText(it.notes)
            }
        })

        accountViewModel.savedCards.observe(this, Observer { cards: List<AppCustomer.CreditCard> ->
            cardAdapter.updateCards(cards)
        })

        // Load user info and saved cards initially
        accountViewModel.loadUserInfo()
        accountViewModel.loadSavedCreditCards()

        // Saving user information when save button is clicked
        binding.buttonSave.setOnClickListener {
            val userInfo = AppCustomer(
                name = binding.editTextName.text.toString().trim(),
                phone = binding.editTextPhone.text.toString().trim(),
                email = binding.editTextEmail.text.toString().trim(),
                address = binding.editTextAddress.text.toString().trim(),
                notes = binding.editTextNotes.text.toString().trim()
            )
            accountViewModel.saveUserInfo(userInfo)
            Toast.makeText(this, "User info saved", Toast.LENGTH_SHORT).show()
        }

        // Navigate to CreditCardActivity for handling saved cards
        binding.chipAddCard.setOnClickListener {
            // TODO: Implement CreditCardActivity for handling saved cards
            Toast.makeText(this, "Navigating to Add Card Screen (Not yet implemented)", Toast.LENGTH_SHORT).show()
        }
    }
}
