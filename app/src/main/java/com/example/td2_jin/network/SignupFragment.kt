package com.example.td2_jin.network

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.td2_jin.LoginForm
import com.example.td2_jin.R
import com.example.td2_jin.SHARED_PREF_TOKEN_KEY
import com.example.td2_jin.SignUpForm
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    private val userWebService = Api.INSTANCE.userWebService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*var emailLogin: EditText
        var passwordLogin: EditText
        view.findViewById<Button>(R.id.Login).setOnClickListener {
            emailLogin = view.findViewById(R.id.emailLogin)
            passwordLogin = view.findViewById(R.id.passwordLogin)
            if(TextUtils.isEmpty(emailLogin.text)){
                emailLogin.setError("Ce champ doit être rempli")
            }else if (TextUtils.isEmpty(passwordLogin.text)){
                passwordLogin.setError("Ce champ doit être rempli.")
            }else{
                val loginForm = LoginForm(emailLogin.text.toString(),passwordLogin.text.toString())
                lifecycleScope.launch {
                    val response = userWebService.login(loginForm)
                    if(response.isSuccessful){
                        println(SHARED_PREF_TOKEN_KEY)
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY,response.body()?.token)
                        }
                        println(Api.INSTANCE.getToken())
                    }else{
                        Toast.makeText(context, "Email ou mot de passe invalide", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }*/

        var firstName : EditText
        var lastName : EditText
        var email : EditText
        var pwd : EditText
        var confirm_pwd : EditText

        view.findViewById<Button>(R.id.signupRegister).setOnClickListener() {
            firstName = view.findViewById(R.id.firstnameRegister)
            lastName = view.findViewById(R.id.lastnameRegister)
            email = view.findViewById(R.id.emailRegister)
            pwd = view.findViewById(R.id.passwordRegister)
            confirm_pwd = view.findViewById(R.id.passwordConfirmationRegister)
            if(TextUtils.isEmpty(email.text)){
                email.setError("Ce champ doit être rempli")
            }else if (TextUtils.isEmpty(pwd.text)){
                pwd.setError("Ce champ doit être rempli.")
            }else if (TextUtils.isEmpty(confirm_pwd.text)){
                confirm_pwd.setError("Ce champ doit être rempli.")
            }else if (pwd.text.toString() != confirm_pwd.text.toString()){
                confirm_pwd.setError("Les mots de passe doivent être identiques.")
            }else {
                val form = SignUpForm(firstName.text.toString(), lastName.text.toString(), email.text.toString(), pwd.text.toString(), confirm_pwd.text.toString())
                lifecycleScope.launch {
                    val response = userWebService.signup(form)
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