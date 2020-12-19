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
import androidx.fragment.app.viewModels
import com.example.td2_jin.LoginForm
import com.example.td2_jin.R
import com.example.td2_jin.userinfo.UserInfoViewModel
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.example.td2_jin.LoginResponse
import com.example.td2_jin.SHARED_PREF_TOKEN_KEY
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val userWebService = Api.INSTANCE.userWebService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            var emailLogin: EditText
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
            }
        }

    
}