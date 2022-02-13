package com.example.encryptionalgorithms.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.encryptionalgorithms.other.Constants.OLD_PASSWORD
import com.example.encryptionalgorithms.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_aes_algorithm.*
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AesAlgorithmFragment : Fragment() {
    var outputMessage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_aes_algorithm, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getSpeechInputAES.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivityForResult(intent, 1001)
            } else {
                Toast.makeText(requireContext(),
                    "your device does not support this feature",
                    Toast.LENGTH_SHORT).show()
            }
        }

        encryptAES.setOnClickListener {
            try {
                outputMessage = encryptMessage(inputTextAES.text.toString(),OLD_PASSWORD)
                outputTextAES.text = outputMessage
                Snackbar.make(view,"Message Encrypted successfully",Snackbar.LENGTH_SHORT).show()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        decryptAES.setOnClickListener {
            try {
                outputMessage = decryptMessage(inputTextAES.text.toString(),OLD_PASSWORD)
                outputTextAES.text = outputMessage
                Snackbar.make(view,"Message Decrypted successfully",Snackbar.LENGTH_SHORT).show()
            } catch (exception:Exception) {
                exception.printStackTrace()
            }
        }

        buttonClearAES.setOnClickListener {
            inputTextAES.text=null
            outputTextAES.text=null
            inputTextAES.hint = "Type your message here"
        }

        buttonSendAES.setOnClickListener {
            if(outputTextAES.text.isNotEmpty()){
                val intent=Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT,outputTextAES.toString())
                    .setType("text/plain")
                startActivity(intent)
            }else{
                Snackbar.make(view,"Please enter a message",Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun decryptMessage(data: String, password: String): String? {
        val key: SecretKeySpec = generateKey(outputMessage!!)
        val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
        c.init(Cipher.DECRYPT_MODE, key)
        val decodedvalue: ByteArray = Base64.decode(data, Base64.DEFAULT)
        val decvalue = c.doFinal(decodedvalue)
        return String(decvalue, charset("UTF-8"))
    }



    private fun generateKey(passwordText: Any): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = OLD_PASSWORD.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }

    private fun encryptMessage(data: String, password: String): String? {
        val key: SecretKeySpec = generateKey(outputMessage!!)
        val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(data.toByteArray(charset("UTF-8")))
        return Base64.encodeToString(encVal, Base64.DEFAULT)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            1001 -> {
                if(resultCode == RESULT_OK && data!=null){
                    val result: java.util.ArrayList<String>? = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val textString: String = result?.get(0).toString()
                    inputTextAES.text = textString
                }
            }
        }
    }


}
