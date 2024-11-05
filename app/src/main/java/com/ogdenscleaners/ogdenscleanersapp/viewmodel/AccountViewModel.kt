package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.Customer
import com.ogdenscleaners.ogdenscleanersapp.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _userInfo = MutableLiveData<Customer.UserInfo>()
    val userInfo: LiveData<Customer.UserInfo> get() = _userInfo

    private val _savedCards = MutableLiveData<List<Customer.CreditCard>>()
    val savedCards: LiveData<List<Customer.CreditCard>> get() = _savedCards

    fun loadUserInfo() {
        viewModelScope.launch {
            _userInfo.value = accountRepository.getUserInfo()
        }
    }

    fun saveUserInfo(userInfo: Customer.UserInfo) {
        viewModelScope.launch {
            accountRepository.saveUserInfo(userInfo)
            _userInfo.value = userInfo
        }
    }

    fun loadSavedCreditCards() {
        viewModelScope.launch {
            _savedCards.value = accountRepository.getSavedCreditCards()
        }
    }
}
