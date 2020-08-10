package com.claudinei.navigation.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.claudinei.navigation.R
import com.claudinei.navigation.data.repository.UserRepository

class RegistrationViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    //proteção de acesso ao liveData
    private val _registrationStateEvent =
        MutableLiveData<RegistrationState>(RegistrationState.CollectProfileData)
    val registrationStateEvent: LiveData<RegistrationState>
        get() = _registrationStateEvent //backProperts

    var authToken = ""
        private set

    // Codigo que será utilizado pela ProfileDataFragment ---------------------------------------
    fun collectProfileData(name: String, bio: String) {
        if (isValidProfileData(name, bio)) {
            // Persist data -> "salvar em banco"
            _registrationStateEvent.value = RegistrationState.CollectCredentials
        }
    }

    private fun isValidProfileData(name: String, bio: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        if (name.isEmpty()) {
            invalidFields.add(INPUT_NAME)
        }

        if (bio.isEmpty()) {
            invalidFields.add(INPUT_BIO)
        }

        if (invalidFields.isNotEmpty()) {
            _registrationStateEvent.value = RegistrationState.InvalidProfileData(invalidFields)
            return false
        }

        return true
    }
//-------------------------------------ProfileDataFragment------------------------------------

    // Codigo que será utilizado pela ChooseCredentialsFragment ----------------------------------
    fun createCredentials(username: String, password: String) {
        if (isValidCredentials(username, password)) {
            // ... create account
            // ... authenticate
            this.authToken = "token"
            _registrationStateEvent.value = RegistrationState.RegistrationCompleted
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        if (username.isEmpty()) {
            invalidFields.add(INPUT_USERNAME)
        }

        if (password.isEmpty()) {
            invalidFields.add(INPUT_PASSWORD)
        }

        if (invalidFields.isNotEmpty()) {
            _registrationStateEvent.value = RegistrationState.InvalidCredentials(invalidFields)
            return false
        }

        return true
    }
//-------------------------------ChooseCredentialsFragment-------------------------------------

    //----Limpar o estado do registro. Garantia que irá sair do fluxo-----------------------------
    fun userCancelledRegistration(): Boolean {
        authToken = ""
        _registrationStateEvent.value = RegistrationState.CollectProfileData
        return true
    }
    //--------------------------------------------------------------------------------------------
    //Representa o estado do viewModel "Dados do Registro"
    sealed class RegistrationState {
        object CollectProfileData : RegistrationState()
        object CollectCredentials : RegistrationState()
        object RegistrationCompleted : RegistrationState()
        class InvalidProfileData(val fields: List<Pair<String, Int>>) : RegistrationState()
        class InvalidCredentials(val fields: List<Pair<String, Int>>) : RegistrationState()
    }

    class RegistrationViewModelFactory(private val userRepository: UserRepository):
        ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RegistrationViewModel(userRepository) as T
        }

    }


    companion object {
        val INPUT_NAME = "INPUT_NAME" to R.string.profile_data_input_layout_error_invalid_name
        val INPUT_BIO = "INPUT_BIO" to R.string.profile_data_input_layout_error_invalid_bio
        val INPUT_USERNAME =
            "INPUT_USERNAME" to R.string.choose_credentials_input_layout_error_invalid_username
        val INPUT_PASSWORD =
            "INPUT_PASSWORD" to R.string.choose_credentials_input_layout_error_invalid_password
    }
}