package com.claudinei.navigation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.claudinei.navigation.R
import com.claudinei.navigation.databinding.LoginFragmentBinding
import com.claudinei.navigation.extensions.dismissError
import com.claudinei.navigation.extensions.navigateWithAnimation
import com.google.android.material.textfield.TextInputLayout


class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding

    private val viewModel: LoginViewModel by activityViewModels()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val validationFields = initValidationFields()
        listenToAuthenticationStateEvent(validationFields)
        registerViewListeners()
        registerDeviceBackStackCallback()
    }

    private fun initValidationFields() = mapOf(
        LoginViewModel.INPUT_USERNAME.first to binding.inputLayoutLoginUsername,
        LoginViewModel.INPUT_PASSWORD.first to binding.inputLayoutLoginPassword
    )

    private fun listenToAuthenticationStateEvent(validationFields: Map<String, TextInputLayout>) {
        viewModel.authenticationStateEvent.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                is LoginViewModel.AuthenticationState.Authenticated -> {
                    navController.popBackStack()
                }
                is LoginViewModel.AuthenticationState.InvalidAuthentication -> {
                    authenticationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
            }
        })
    }

    private fun registerViewListeners() {
        binding.btnLoginSignIn.setOnClickListener {
            val username = binding.inputTextUsername.text.toString()
            val password = binding.inputTextPassword.text.toString()

            viewModel.authentication(username, password)
        }

        binding.btnLoginSignUp.setOnClickListener {
            navController.navigateWithAnimation(R.id.action_loginFragment_to_navigation)
        }

        binding.inputTextUsername.addTextChangedListener {
            binding.inputLayoutLoginUsername.dismissError()
        }

        binding.inputTextPassword.addTextChangedListener {
            binding.inputLayoutLoginPassword.dismissError()
        }
    }

    private fun registerDeviceBackStackCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelAuthentication()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelAuthentication()
        return true
    }

    private fun cancelAuthentication() {
        viewModel.refuseAuthentication()
        navController.popBackStack(R.id.startFragment, false)
    }
}