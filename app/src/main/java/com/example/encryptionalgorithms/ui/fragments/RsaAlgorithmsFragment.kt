package com.example.encryptionalgorithms.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.encryptionalgorithms.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rsa_algorithms.*
import java.lang.Exception
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

class RsaAlgorithmsFragment : Fragment() {
    var temp: String? = null
    var tosend = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rsa_algorithms, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kp: KeyPair = getKeyPair()!!

        val publicKey = kp.public
        val publicKeyBytes = publicKey.encoded
        val publicKeyBytesBase64 = String(Base64.encode(publicKeyBytes, Base64.DEFAULT))

        val privateKey = kp.private
        val privateKeyBytes = privateKey.encoded
        val privateKeyBytesBase64 = String(Base64.encode(privateKeyBytes, Base64.DEFAULT))


        encryptRSA.setOnClickListener {
            temp = inputTextRSA.getText().toString()
            val encrypted: String = encryptRSAToString(temp!!, publicKeyBytesBase64)!!
            outputTextRSA.setText(encrypted)
            Snackbar.make(view, "Message encrypted successfully", Snackbar.LENGTH_SHORT).show()
            tosend = encrypted
        }

        decryptRSA.setOnClickListener {
            temp = inputTextRSA.getText().toString()
            val decrypted: String = decryptRSAToString(temp, privateKeyBytesBase64)!!
            outputTextRSA.setText(decrypted)
            Snackbar.make(view, "Message decrypted successfully", Snackbar.LENGTH_SHORT).show()
            tosend = decrypted
        }

        buttonClearRSA.setOnClickListener {
            try {
                inputTextRSA.setText(" ")
                outputTextRSA.setText("")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        buttonSendRSA.setOnClickListener {
            if (tosend.length > 0) {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, tosend)
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            } else {
                Snackbar.make(view, "Please enter the message", Snackbar.LENGTH_SHORT).show()
            }
        }

        getSpeechInputRSA.setOnClickListener {
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
    }

    fun getKeyPair(): KeyPair? {
        var kp: KeyPair? = null
        try {
            val kpg = KeyPairGenerator.getInstance("RSA")
            kpg.initialize(2048)
            kp = kpg.generateKeyPair()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return kp
    }

    fun encryptRSAToString(clearText: String, publicKey: String): String? {
        var encryptedBase64 = ""
        try {
            val keyFac = KeyFactory.getInstance("RSA")
            val keySpec: KeySpec = X509EncodedKeySpec(Base64.decode(publicKey.trim { it <= ' ' }
                .toByteArray(), Base64.DEFAULT))
            val key: Key = keyFac.generatePublic(keySpec)
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encryptedBytes = cipher.doFinal(clearText.toByteArray(charset("UTF-8")))
            encryptedBase64 = String(Base64.encode(encryptedBytes, Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encryptedBase64.replace("(\\r|\\n)".toRegex(), "")
    }

    fun decryptRSAToString(encryptedBase64: String?, privateKey: String): String? {
        var decryptedString: String? = ""
        try {
            val keyFac = KeyFactory.getInstance("RSA")
            val keySpec: KeySpec = PKCS8EncodedKeySpec(Base64.decode(privateKey.trim { it <= ' ' }
                .toByteArray(), Base64.DEFAULT))
            val key: Key = keyFac.generatePrivate(keySpec)
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            decryptedString = String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return decryptedString
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1001 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                inputTextRSA.setText(res!![0])
            }
        }
    }
}