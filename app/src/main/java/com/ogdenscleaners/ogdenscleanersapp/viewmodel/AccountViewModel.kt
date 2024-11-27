package com.ogdenscleaners.ogdenscleanersapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogdenscleaners.ogdenscleanersapp.models.AppCustomer
import com.ogdenscleaners.ogdenscleanersapp.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _userInfo = MutableLiveData<AppCustomer>()
    val userInfo: LiveData<AppCustomer> get() = _userInfo

    private val _savedCards = MutableLiveData<List<AppCustomer.CreditCard>>()
    val savedCards: LiveData<List<AppCustomer.CreditCard>> get() = _savedCards

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadUserInfo() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val userInfoResult = accountRepository.getUserInfo()
                _userInfo.postValue(userInfoResult)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load user info: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveUserInfo(userInfo: AppCustomer) {
        viewModelScope.launch {
            _loading.value = true
            try {
                accountRepository.saveUserInfo(userInfo)
                _userInfo.postValue(userInfo)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to save user info: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadSavedCreditCards() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val cardsResult = accountRepository.getSavedCreditCards()
                _savedCards.postValue(cardsResult)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load saved credit cards: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}
