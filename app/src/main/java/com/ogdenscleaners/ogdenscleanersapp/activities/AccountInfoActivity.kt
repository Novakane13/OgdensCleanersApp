package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ogdenscleaners.ogdenscleanersapp.adapters.CreditCardAdapter
import com.ogdenscleaners.ogdenscleanersapp.databinding.ActivityAccountInfoBinding
import com.ogdenscleaners.ogdenscleanersapp.models.Customer
import com.ogdenscleaners.ogdenscleanersapp.models.User
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

        cardAdapter = CreditCardAdapter(mutableListOf())
        binding.savedCardsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.savedCardsRecyclerView.adapter = cardAdapter

        accountViewModel.userInfo.observe(this, Observer { userInfo ->
            binding.editTextName.setText(userInfo.name)
            binding.editTextPhone.setText(userInfo.phone)
            binding.editTextEmail.setText(userInfo.email)
            binding.editTextNotes.setText(userInfo.notes)
        })

        accountViewModel.savedCards.observe(this, Observer { cards ->
            cardAdapter.updateCards(cards)
        })

        accountViewModel.loadUserInfo()
        accountViewModel.loadSavedCreditCards()

        binding.buttonSave.setOnClickListener {
            val userInfo = Customer.User(
                binding.editTextName.text.toString().trim(),
                binding.editTextPhone.text.toString().trim(),
                binding.editTextEmail.text.toString().trim(),
                binding.editTextNotes.text.toString().trim()
            )
            accountViewModel.saveUserInfo(userInfo)
            Toast.makeText(this, "User info saved", Toast.LENGTH_SHORT).show()
        }

        binding.chipAddCard.setOnClickListener {
            // Navigate to CreditCardActivity
        }
    }
}
