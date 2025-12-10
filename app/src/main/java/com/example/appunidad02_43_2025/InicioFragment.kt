package com.example.appunidad02_43_2025

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appunidad02_43_2025.database.Alumno
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator

class InicioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        val btnEscanear = view.findViewById<Button>(R.id.btnEscanearInicio)
        btnEscanear.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setCaptureActivity(CaptureActivity::class.java)
            integrator.setOrientationLocked(false)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt("Escanea el Código QR del Alumno")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(true)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (resultCode == Activity.RESULT_OK && result.contents != null) {
                try {
                    val gson = Gson()
                    val alumno = gson.fromJson(result.contents, Alumno::class.java)

                    val bundle = Bundle().apply {
                        putSerializable("alumno", alumno)
                    }

                    val fragAlumno = AlumnosFragment()
                    fragAlumno.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.frmContenedor, fragAlumno)
                        .addToBackStack(null)
                        .commit()

                    val bottom =
                        requireActivity().findViewById<BottomNavigationView>(R.id.btnNavegador)
                    bottom.menu.findItem(R.id.btnAlumnos).isChecked = true

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "QR inválido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}