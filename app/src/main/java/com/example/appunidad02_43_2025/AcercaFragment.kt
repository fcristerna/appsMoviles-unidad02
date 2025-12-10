package com.example.appunidad02_43_2025

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.appunidad02_43_2025.database.Alumno

class AcercaFragment : Fragment() {

    private lateinit var lblMiMatricula: TextView
    private lateinit var lblMiNombre: TextView
    private lateinit var lblMiEspecialidad: TextView
    private lateinit var lblMiDomicilio: TextView
    private lateinit var imgMiFotoPerfil: ImageView
    private lateinit var imgQrPersonal: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_acerca, container, false)
        initComponent(view)
        mostrarMiInfo()
        return view
    }

    private fun initComponent(view: View) {
        lblMiMatricula = view.findViewById(R.id.lblMiMatricula)
        lblMiNombre = view.findViewById(R.id.lblMiNombre)
        lblMiEspecialidad = view.findViewById(R.id.lblMiEspecialidad)
        lblMiDomicilio = view.findViewById(R.id.lblMiDomicilio)
        imgMiFotoPerfil = view.findViewById(R.id.imgMiFotoPerfil)
        imgQrPersonal = view.findViewById(R.id.imgQrPersonal)
    }

    private fun mostrarMiInfo() {
        val miAlumno = Alumno().apply {
            matricula = "2024030374"
            nombre = "Luis Fernando Cristerna Arámburo"
            especialidad = "Tecnologías de la Información"
            domicilio = "Real del Valle 5025"
            foto = "https://siiaa.mx/assets/images/fotos/2024030374_bce0772919d433d4f60001d36806d034.png"
        }

        lblMiMatricula.text = "Matrícula: ${miAlumno.matricula}"
        lblMiNombre.text = "Nombre: ${miAlumno.nombre}"
        lblMiEspecialidad.text = "Especialidad: ${miAlumno.especialidad}"
        lblMiDomicilio.text = "Domicilio: ${miAlumno.domicilio}"

        imgMiFotoPerfil.setImageResource(R.mipmap.foto_fer)

        val json = QrUtils.alumnoToJson(miAlumno)
        val bmpQr = QrUtils.generarQrBitmap(json, 600)
        imgQrPersonal.setImageBitmap(bmpQr)
    }
}