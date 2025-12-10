package com.example.appunidad02_43_2025

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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
                    mostrarDialogoValidacion(alumno)
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

    private fun mostrarDialogoValidacion(alumno: Alumno) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.validacion, null)

        val imgPreview = dialogView.findViewById<ImageView>(R.id.imgPreviewQR)
        val lblMatricula = dialogView.findViewById<TextView>(R.id.lblValMatricula)
        val lblNombre = dialogView.findViewById<TextView>(R.id.lblValNombre)
        val lblCarrera = dialogView.findViewById<TextView>(R.id.lblValCarrera)
        val lblDomicilio = dialogView.findViewById<TextView>(R.id.lblValDomicilio)
        val lblUrl = dialogView.findViewById<TextView>(R.id.lblValFotoUrl)
        val btnValidar = dialogView.findViewById<Button>(R.id.btnValidarDatos)
        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelarValidacion)

        lblMatricula.text = "Matrícula: ${alumno.matricula}"
        lblNombre.text = "Nombre: ${alumno.nombre}"
        lblCarrera.text = "Carrera: ${alumno.especialidad}"
        lblDomicilio.text = "Domicilio: ${alumno.domicilio}"
        lblUrl.text = "Foto (URL): ${alumno.foto}"

        if (!alumno.foto.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(alumno.foto)
                .into(imgPreview)
        } else {
            imgPreview.setImageResource(R.drawable.alumno)
        }

        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()

        btnValidar.setOnClickListener {
            irAAlumnosConDatos(alumno)
            dialog.dismiss()
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun irAAlumnosConDatos(alumno: Alumno) {
        val bundle = Bundle().apply {
            putSerializable("miAlumno", alumno)
        }

        val fragAlumno = AlumnosFragment()
        fragAlumno.arguments = bundle

        val bottom =
            requireActivity().findViewById<BottomNavigationView>(R.id.btnNavegador)
        bottom.menu.findItem(R.id.btnAlumnos).isChecked = true

        parentFragmentManager.beginTransaction()
            .replace(R.id.frmContenedor, fragAlumno)
            .addToBackStack(null)
            .commit()
    }
}