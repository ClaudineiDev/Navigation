package com.claudinei.navigation.ui.registration.profiledata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.claudinei.navigation.R
import com.claudinei.navigation.data.db.AppDatabase
import com.claudinei.navigation.data.repository.UserDbDataSource
import com.claudinei.navigation.ui.registration.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_profile_data.*

class ProfileDataFragment : Fragment() {

    private val registrationViewModel: RegistrationViewModel by activityViewModels(
        factoryProducer = {
            val dataBase = AppDatabase.getDatabase(requireContext())
            RegistrationViewModel.RegistrationViewModelFactory(
                userRepository = UserDbDataSource(dataBase.userDao())
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val validationFields = initValidationFields()
        listenToRegistrationStateEvent(validationFields)
        buttonProfileDataNext.setOnClickListener {
            val name = inputProfileDataName.text.toString()
            val bio = inputProfileDataBio.text.toString()

            registrationViewModel.collectProfileData(name, bio)
        }
    }
    private fun initValidationFields() = mapOf(
        RegistrationViewModel.INPUT_NAME.first to inputLayoutProfileDataName,
        RegistrationViewModel.INPUT_BIO.first to inputLayoutProfileDataBio
    )

    private fun listenToRegistrationStateEvent(validationFields: Map<String, TextInputLayout>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            when (registrationState) {
                is RegistrationViewModel.RegistrationState.CollectCredentials -> {
                    val name = inputProfileDataName.text.toString()
                    val directions = ProfileDataFragmentDirections
                        .actionProfileDataFragmentToChooseCredentialsFragment(name)

                    findNavController().navigate(directions)
                }
                is RegistrationViewModel.RegistrationState.InvalidProfileData -> {
                    registrationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }
}