package com.example.td2_jin.network

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.td2_jin.R


/**
 * A simple [Fragment] subclass.
 * Use the [AuthenticationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthenticationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.loginAuthentification)?.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }

        view.findViewById<Button>(R.id.signupAuthentification)?.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }
    }

}