package com.example.appunidad02_43_2025

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.appunidad02_43_2025.database.Alumno
import com.example.appunidad02_43_2025.database.AlumnoDB

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlumnosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlumnosFragment : Fragment() {

    private lateinit var btnGuardar : Button
    private lateinit var btnBuscar : Button
    private lateinit var btnBorrar : Button
    private lateinit var btnLimpiar : Button

    private lateinit var txtMatricula : EditText
    private lateinit var txtNombre : EditText
    private lateinit var txtDomicilio : EditText
    private lateinit var txtEspecialidad : EditText

    private lateinit var imgFoto : ImageView

    private lateinit var db : AlumnoDB


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_alumnos, container, false)
        iniciarComponentes(view)
        eventosClic()
        return view
    }

    fun iniciarComponentes(view : View){
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnBuscar = view.findViewById(R.id.btnBuscar)
        btnBorrar= view.findViewById(R.id.btnBorrar)
        btnLimpiar= view.findViewById(R.id.btnLimpiar)

        txtNombre = view.findViewById(R.id.txtNombre)
        txtMatricula = view.findViewById(R.id.txtMatricula)
        txtDomicilio = view.findViewById(R.id.txtDomicilio)
        txtEspecialidad = view.findViewById(R.id.txtEspecialidad)
        txtMatricula = view.findViewById(R.id.txtMatricula)
        imgFoto = view.findViewById(R.id.imgAlumno)
    }


    fun eventosClic(){
        btnGuardar.setOnClickListener(View.OnClickListener {

            // validad
            if (txtMatricula.text.isEmpty() || txtEspecialidad.text.isEmpty() || txtNombre.text.isEmpty() || txtDomicilio.text.isEmpty()){
                Toast.makeText(requireContext(), "Falta información",Toast.LENGTH_SHORT).show()
            }

            else {
                //generar objeto db
                val db = AlumnoDB(requireContext())
                val matricula = txtMatricula.text.toString()
                val nombre = txtNombre.text.toString()
                val domicilio = txtDomicilio.text.toString()
                val especialidad = txtEspecialidad.text.toString()


                //generar objeto alumno y asignar datos

                val nuevoAlumno = Alumno().apply {
                    this.matricula = matricula
                    this.nombre = nombre
                    this.domicilio = domicilio
                    this.especialidad = especialidad
                    this.foto = ""
                }

                db.openDataBase()

                // insertar alumno en tabla
                val id : Long = db.insertarAlumno(nuevoAlumno)
                //validar si agrego
                if(id>0) Toast.makeText(requireContext(),"se agregó con éxito. ID = $id", Toast.LENGTH_SHORT).show()
                else Toast.makeText(requireContext(),"No fue posible agregar al alumno", Toast.LENGTH_SHORT).show()
            }
        })

        btnBuscar.setOnClickListener(View.OnClickListener {
            // validar
            if(txtMatricula.text.isEmpty())Toast.makeText(requireContext(),
                "Faltó capturar Matricula", Toast.LENGTH_SHORT).show()
            else {

                db = AlumnoDB(requireContext())
                db.openDataBase()
                val alumno : Alumno = db.getAlumno(txtMatricula.text.toString())
                if(alumno.id!=0){
                    txtNombre.setText(alumno.nombre)
                    txtDomicilio.setText(alumno.domicilio)
                    txtEspecialidad.setText(alumno.especialidad)
                }
                else {
                    Toast.makeText(requireContext(), "No se encontro", Toast.LENGTH_SHORT).show()
                }
                db.close()
            }


        })

        btnLimpiar.setOnClickListener(View.OnClickListener {
            if(txtMatricula.text.isEmpty() || txtNombre.text.isEmpty() || txtDomicilio.text.isEmpty() || txtEspecialidad.text.isEmpty()){

                Toast.makeText(context, "Limpio", Toast.LENGTH_SHORT).show()
            } else {
                txtMatricula.setText("")
                txtNombre.setText("")
                txtDomicilio.setText("")
                txtEspecialidad.setText("")
            }
        })
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlumnosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlumnosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}