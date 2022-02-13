package com.example.encryptionalgorithms.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.encryptionalgorithms.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_md5.*
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and

class Md5Fragment : Fragment() {
    var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_md5, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonClearMD5.setOnClickListener {
            password = inputTextMD5.text.toString()
            if(password!=null){
                inputTextMD5.text=hashPassword(password)
            }
            else{
                Snackbar.make(view,"Please enter the message",Snackbar.LENGTH_SHORT).show()
            }
        }

        getSpeechInputMD5.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivityForResult(intent, 1001)
            } else {
                Snackbar.make(view,"Your device does not support this features",Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    private fun hashPassword(password: String): String? {
        val passwordToHash = password
        var generatePassword:String? = null
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(passwordToHash.toByteArray())
            val bytes = md.digest()
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(Integer.toString((bytes[i] and 0xff.toByte()) + 0x100, 32).substring(1))
            }
            generatePassword = sb.toString()
            return generatePassword
        } catch (exception:NoSuchAlgorithmException){
            exception.printStackTrace()
            return null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1001 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    inputTextMD5.setText(res!![0])
                }
            }
        }
    }
}