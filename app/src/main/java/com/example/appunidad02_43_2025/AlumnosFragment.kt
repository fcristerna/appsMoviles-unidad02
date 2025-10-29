package com.example.appunidad02_43_2025

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
    private lateinit var txtFoto : TextView


    private lateinit var imgFoto : ImageView

    private lateinit var db : AlumnoDB

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

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
        txtFoto = view.findViewById(R.id.txtFoto)
        imgFoto = view.findViewById(R.id.imgAlumno)
    }


    fun eventosClic(){
        btnGuardar.setOnClickListener{

            if (txtMatricula.text.isEmpty() ||txtNombre.text.isEmpty() ||txtDomicilio.text.isEmpty() ||txtEspecialidad.text.isEmpty() )
            {
                Toast.makeText(requireContext(), "Faltó Información", Toast.LENGTH_SHORT).show()
            }

            else {

                val db = AlumnoDB(requireContext())
                db.openDataBase()
                val matricula = txtMatricula.text.toString()
                val nombre = txtNombre.text.toString()
                val domicilio = txtDomicilio.text.toString()
                val especialidad = txtEspecialidad.text.toString()
                val foto = imgFoto.tag?.toString() ?: ""
                txtFoto.text = foto

                // Generar el objeto alumno y asignar los datos
                val dataAlumno = Alumno().apply {
                    this.matricula = matricula
                    this.nombre = nombre
                    this.domicilio = domicilio
                    this.especialidad = especialidad
                    this.foto = foto
                }
                val alumno : Alumno = db.getAlumno(txtMatricula.text.toString())
                if (alumno.id!=0){
                    val builder = AlertDialog.Builder(requireContext())

                    // preguntar si quiere actualizar datos
                    builder.setTitle("CRUD Alumnos")
                    builder.setMessage("El alumno ya existe ¿Deseas Actualizar datos?")

                    builder.setPositiveButton("Aceptar") { dialog, which ->
                        val id: Int = db.actualizarAlumno(dataAlumno, alumno.id)
                        // validar si Actualizo
                        if (id > 0) {
                            Toast.makeText(
                                requireContext(),
                                "Se Actualizo con exito, ID = $id",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No fue posible Actualizar alumno",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                    builder.setNegativeButton("Cancelar") { dialog, which ->
                        Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_SHORT).show()
                    }

                    builder.setNeutralButton("Quizá") { dialog, which ->
                        Toast.makeText(requireContext(), "Quizá", Toast.LENGTH_SHORT).show()
                    }
                    builder.show()
                } else
                // si el alumno no existe  insertar nuevo alumno
                {
                    // insert alumno en tabla
                    val id : Long = db.insertarAlumno(dataAlumno)
                    // validar si agrego
                    if (id>0)
                    {
                        Toast.makeText(requireContext(), "Se agregó con exito, ID = $id", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "No fue posible agregar alumno", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

        btnBuscar.setOnClickListener{
            // Validar
            if (txtMatricula.text.isEmpty()) Toast.makeText(requireContext(),
                "Falto capturar Matricula", Toast.LENGTH_SHORT
            ).show()
            else
            {
                db = AlumnoDB(requireContext())
                db.openDataBase()
                val alumno: Alumno = db.getAlumno(txtMatricula.text.toString())
                if (alumno.id != 0) {
                    txtNombre.setText(alumno.nombre)
                    txtDomicilio.setText(alumno.domicilio)
                    txtEspecialidad.setText(alumno.especialidad)

                    Glide.with(requireContext())
                        .load(Uri.parse(alumno.foto))
                        .apply(RequestOptions().override(100,100))
                        .into(imgFoto)

                    imgFoto.tag = alumno.foto
                    txtFoto.text = imgFoto.tag?.toString()


                } else {
                    Toast.makeText(
                        requireContext(),
                        "No se encotro al alumno",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                db.close()
            }



        }
        btnBorrar.setOnClickListener {
            if (txtMatricula.text.isEmpty()) Toast.makeText(requireContext(),
                "Falto capturar Matricula", Toast.LENGTH_SHORT
            ).show()
            else {

                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("CRUD Alumnos")
                builder.setMessage("¿Deseas Eliminar datos del alumno permanentemente?")

                builder.setPositiveButton("Aceptar") { dialog, which ->
                    db = AlumnoDB(requireContext())
                    db.openDataBase()
                    val alumno: Alumno = db.getAlumno(txtMatricula.text.toString())
                    if (alumno.id != 0) {
                        val id: Int = db.borrarAlumno(alumno.id)
                        if (id > 0) {
                            Toast.makeText(
                                requireContext(),
                                "Borrado con éxito, ID = $id",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No fue posible borrar alumno", Toast.LENGTH_SHORT).show()

                        }
                    }
                }

                builder.setNegativeButton("Cancelar") { dialog, which ->
                    Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_SHORT).show()
                }

                builder.setNeutralButton("Quizá") { dialog, which ->
                    Toast.makeText(requireContext(), "Quizá", Toast.LENGTH_SHORT).show()
                }
                builder.show()

            }
        }
        btnLimpiar.setOnClickListener {
            txtNombre.setText("")
            txtMatricula.setText("")
            txtDomicilio.setText("")
            txtEspecialidad.setText("")
            imgFoto.setImageResource(R.drawable.alumno)
            imgFoto.tag=null
            txtFoto.setText("")
        }

        imgFoto.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            val uri : Uri? = data.data
            imgFoto.setImageURI((uri))
            imgFoto.tag = uri.toString()
            txtFoto.text = imgFoto.tag?.toString()
        }
    }
}





