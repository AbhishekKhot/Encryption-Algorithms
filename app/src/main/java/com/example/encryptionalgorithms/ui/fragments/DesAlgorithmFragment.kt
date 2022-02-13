package com.example.encryptionalgorithms.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.encryptionalgorithms.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_des_algorithm.*
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.lang.StringBuilder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.*
import javax.crypto.spec.DESedeKeySpec

class DesAlgorithmFragment : Fragment() {
    private val UNICODE_FORMAT: String = "UTF-8"
    private val DES_ENCRYPTION_SCHEME = "DES"
    private lateinit var myKeySpec: KeySpec
    private lateinit var cipher: Cipher
    private lateinit var KeyAsBytes: ByteArray
    private var mySecretKeyFactory: SecretKeyFactory? = null
    private var key: SecretKey? = null
    private var myEncKey = "This is Key"
    private var ans = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_des_algorithm, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myEncryptionKey = myEncKey
        val getMyEncryptionScheme: String = DES_ENCRYPTION_SCHEME

        try {
            KeyAsBytes =
                myEncryptionKey.toByteArray(charset(UNICODE_FORMAT))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        try {
            myKeySpec = DESedeKeySpec(KeyAsBytes)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }

        try {
            mySecretKeyFactory = SecretKeyFactory.getInstance(getMyEncryptionScheme)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        try {
            cipher = Cipher.getInstance(getMyEncryptionScheme)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }

        try {
            key = mySecretKeyFactory?.generateSecret(myKeySpec)
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }

        getSpeechInputDES.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivityForResult(intent, 1001)
            } else {
                Snackbar.make(view,
                    "your device does not support this feature",
                    Snackbar.LENGTH_SHORT).show()
            }
        }

        encryptDES.setOnClickListener {
            val temp = inputTextDES.text.toString()
            ans = encryptMessage(temp)
            outputTextDES.text = ans
        }

        decryptDES.setOnClickListener {
            val temp = inputTextDES.text.toString()
            ans = decryptMessage(temp)
            outputTextDES.text = ans
        }

        buttonSendDES.setOnClickListener {
            if (ans.isNotEmpty()) {
                val intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, ans)
                    .setType("text/plain")
                startActivity(intent)
            } else {
                Snackbar.make(view, "Please enter message", Snackbar.LENGTH_SHORT).show()
            }
        }

        buttonClearDES.setOnClickListener {
            try {
                inputTextDES.text = ""
                outputTextDES.text = ""
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

    }

    private fun decryptMessage(temp: String): String {
        var decryptedText: String? = null
        try {
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedText: ByteArray = Base64.decode(temp, Base64.DEFAULT)
            val plainText = cipher.doFinal(encryptedText)
            decryptedText = bytes2String(plainText)
        } catch (e: InvalidKeyException) {
        } catch (e: IllegalBlockSizeException) {
        } catch (e: BadPaddingException) {
        }
        return decryptedText!!
    }

    private fun bytes2String(plainText: ByteArray?): String? {
        val stringBuffer = StringBuilder()
        for (i in plainText!!.indices) {
            stringBuffer.append(plainText.get(i) as Char)
        }
        return stringBuffer.toString()
    }

    private fun encryptMessage(temp: String): String {
        var encryptedMessage: String? = null
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val plainText: ByteArray = temp.toByteArray(charset(UNICODE_FORMAT))
            var encryptedText = cipher.doFinal(plainText)
            encryptedMessage = Base64.encodeToString(encryptedText, Base64.DEFAULT)
        } catch (exception: InvalidKeyException) {
            exception.printStackTrace()
        }
        return encryptedMessage.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1001 -> {
                if (resultCode == RESULT_OK && data != null) {
                    val res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    inputTextDES.setText(res!![0])
                }
            }
        }
    }

}