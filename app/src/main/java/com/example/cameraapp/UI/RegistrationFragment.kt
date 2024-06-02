package com.example.cameraapp.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.cameraapp.data.AuthenticationStatus
import com.example.cameraapp.data.Constants.APP_MIN_PASSWORD_LENGTH
import com.example.cameraapp.data.Constants.APP_TOAST_PASSWORDS_DONT_MATCH
import com.example.cameraapp.data.Constants.APP_TOAST_PASSWORD_TOO_SHORT
import com.example.cameraapp.data.Constants.APP_TOAST_SIGNUP_FAILED
import com.example.cameraapp.data.Constants.APP_TOAST_SIGNUP_SUCCESS
import com.example.cameraapp.databinding.FragmentRegistrationBinding
import com.example.cameraapp.utils.Utils.getBlankStringsChecker
import com.example.cameraapp.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var setButtonVisibility: ()->Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            view.findNavController()
                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }

        setButtonVisibility = {
            if (viewModel.authState.value != AuthenticationStatus.Progress) {
                binding.registerButton.isEnabled =
                    !(binding.userEmail.text.toString().isBlank() || binding.userPassword1.text.toString().isBlank() || binding.userPassword2.text.toString().isBlank())
            }
        }

        binding.userEmail.addTextChangedListener(getBlankStringsChecker(binding.userEmail, setButtonVisibility))
        binding.userPassword1.addTextChangedListener(getBlankStringsChecker(binding.userPassword1, setButtonVisibility))
        binding.userPassword2.addTextChangedListener(getBlankStringsChecker(binding.userPassword2, setButtonVisibility))

        binding.registerButton.setOnClickListener {
            signUp()
        }
        initObservers()
    }

    private fun signUp() {
        if (binding.userPassword1.text.toString().count() < APP_MIN_PASSWORD_LENGTH) {
            Toast.makeText(activity, APP_TOAST_PASSWORD_TOO_SHORT, Toast.LENGTH_SHORT).show()
        } else if (!binding.userPassword1.text.toString().equals(binding.userPassword2.text.toString())) {
            Toast.makeText(activity, APP_TOAST_PASSWORDS_DONT_MATCH, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.signIn(binding.userEmail.text.toString(), binding.userPassword1.text.toString(), true)
        }
    }

    private fun initObservers() {
        viewModel.resetAuthState()
        viewModel.authState.observe(viewLifecycleOwner) {authStatus->
            when (authStatus) {
                is AuthenticationStatus.Success -> {
                    setButtonVisibility()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "$APP_TOAST_SIGNUP_SUCCESS.", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Successful registration")

//                    viewModel.editPreferences(APP_PREFERENCES_GROUP + "_" + binding.userEmail.text.toString(), viewModel.getPreference(APP_PREFERENCES_GROUP_REGISTER, ""))
//                    viewModel.editPreferences(APP_PREFERENCES_GROUP_REGISTER, null)
                    requireView().findNavController()
                        .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                }
                is AuthenticationStatus.Error -> {
                    setButtonVisibility()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "$APP_TOAST_SIGNUP_FAILED: ${authStatus.message}", Toast.LENGTH_LONG).show()
                    Log.d("TAG", authStatus.message)
                }
                is AuthenticationStatus.Progress -> {
                    binding.registerButton.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}