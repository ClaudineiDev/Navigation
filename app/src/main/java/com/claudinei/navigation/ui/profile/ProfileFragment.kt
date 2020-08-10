package com.claudinei.navigation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.claudinei.navigation.R
import com.claudinei.navigation.extensions.navigateWithAnimation
import com.claudinei.navigation.ui.login.LoginViewModel
import com.claudinei.navigation.ui.start.StartFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val navController: NavController by lazy {
        findNavController()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.authenticationStateEvent.observe(viewLifecycleOwner, Observer {authenticationState ->
            when (authenticationState){
                is LoginViewModel.AuthenticationState.Authenticated ->{
                    txt_profile_username.text = getString(R.string.profile_text_username, loginViewModel.username)
                }
                is LoginViewModel.AuthenticationState.Unauthenticated ->{
                    navController.navigateWithAnimation(R.id.loginFragment)
                }
            }
        })
    }
}