package com.example.td2_jin.network

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.td2_jin.LoginForm
import com.example.td2_jin.R
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.td2_jin.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val userWebService = Api.INSTANCE.userWebService

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this

        binding.loginForm = LoginForm()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loginForm = binding.loginForm!!
        binding.LoginButton.setOnClickListener {
            if(TextUtils.isEmpty(loginForm.email)){
                binding.emailLogin.error = "Ce champ doit être rempli"
            }else if (TextUtils.isEmpty(loginForm.password)){
                binding.passwordLogin.error = "Ce champ doit être rempli."
            }else{
                lifecycleScope.launch {
                    val response = userWebService.login(loginForm)
                    if(response.isSuccessful){
                        Api.INSTANCE.setToken(response.body()?.token!!)
                        findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)
                    }else{
                        Toast.makeText(context, "Email ou mot de passe invalide", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

}