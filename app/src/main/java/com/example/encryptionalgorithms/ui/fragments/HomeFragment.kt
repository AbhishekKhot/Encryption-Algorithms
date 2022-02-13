package com.example.encryptionalgorithms.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.encryptionalgorithms.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(requireActivity(), permissions,0)
        } else {
            Snackbar.make(view,"Without permission some features won't work",Snackbar.LENGTH_SHORT).show()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAES.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aesAlgorithmFragment)
        }

        buttonDES.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_desAlgorithmFragment)
        }

        buttonRSA.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rsaAlgorithmsFragment)
        }

        buttonMD5.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_md5Fragment)
        }

        buttonPassword.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_passwordMatchingFragment)
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(requireActivity(), permissions,0)
        }
    }

}