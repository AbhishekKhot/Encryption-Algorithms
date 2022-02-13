package com.example.encryptionalgorithms.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.encryptionalgorithms.other.Constants.OLD_PASSWORD
import com.example.encryptionalgorithms.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_password_matching.*


class PasswordMatchingFragment : Fragment() {
    var oldPassword: String? = null
    var newPassword:String? = null
    var confirmPassword:String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_password_matching, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oldPassword = old.text.toString()
        newPassword = newp.text.toString()
        confirmPassword = cnfpwd.text.toString()

        if(OLD_PASSWORD.equals(oldPassword)){
            if(newPassword.equals(confirmPassword)){
                OLD_PASSWORD = newPassword as String
                Snackbar.make(view,"Password changed successfully",Snackbar.LENGTH_SHORT).show()
            }
            else{
                Snackbar.make(view,"Password doesn't matching",Snackbar.LENGTH_SHORT).show()
            }
        }
        else{
            Snackbar.make(view,"Please enter the valid password",Snackbar.LENGTH_SHORT).show()
        }
    }
}