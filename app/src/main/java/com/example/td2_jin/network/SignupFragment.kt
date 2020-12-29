package com.example.td2_jin.network

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.td2_jin.R
import com.example.td2_jin.SignUpForm
import com.example.td2_jin.databinding.FragmentSignupBinding
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {
    private val userWebService = Api.INSTANCE.userWebService

    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_signup, container, false)

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this

        binding.signUpForm = SignUpForm()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val signUpForm = binding.signUpForm!!
        binding.signupRegister.setOnClickListener() {

            if(TextUtils.isEmpty(signUpForm.email)){
                binding.emailRegister.error = "Ce champ doit être rempli"
            }else if (TextUtils.isEmpty(signUpForm.password)){
                binding.passwordRegister.error = "Ce champ doit être rempli."
            }else if (TextUtils.isEmpty(signUpForm.password_confirmation)){
                binding.passwordConfirmationRegister.error = ("Ce champ doit être rempli.")
            }else if (signUpForm.password != signUpForm.password_confirmation){
                binding.passwordConfirmationRegister.error = "Les mots de passe doivent être identiques."
            }else {
                lifecycleScope.launch {
                    val response = userWebService.signup(signUpForm)
                    if(response.isSuccessful){
                        Api.INSTANCE.setToken(response.body()?.token!!)
                        findNavController().navigate(R.id.action_signupFragment_to_taskListFragment)
                    }else{
                        Toast.makeText(context, response.errorBody()!!.string(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}