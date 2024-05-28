package com.example.diplomafirst

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.diplomafirst.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signIn.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_SignUpFragment_to_SignInFragment)
        }
        binding.signUp.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_SignUpFragment_to_WorkFragment)
        }
    }
}