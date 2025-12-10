package com.example.appunidad02_43_2025

import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appunidad02_43_2025.database.Alumno
import com.example.appunidad02_43_2025.database.AlumnoDB
import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

    private lateinit var btnGuardar: Button
    private lateinit var btnBuscar: Button
    private lateinit var btnBorrar: Button
    private lateinit var btnLimpiar: Button

    private lateinit var txtMatricula: EditText
    private lateinit var txtNombre: EditText
    private lateinit var txtDomicilio: EditText
    private lateinit var txtEspecialidad: EditText
    private lateinit var txtFoto: EditText

    private lateinit var imgFoto: ImageView
    private lateinit var db: AlumnoDB
    private lateinit var databaseRef: DatabaseReference

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alumnos, container, false)

        iniciarComponentes(view)

        databaseRef = FirebaseDatabase.getInstance().getReference("alumnos")

        eventosClic()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AlumnoFromListaFragment()
    }

    fun iniciarComponentes(view: View) {
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnBuscar = view.findViewById(R.id.btnBuscar)
        btnBorrar = view.findViewById(R.id.btnBorrar)
        btnLimpiar = view.findViewById(R.id.btnLimpiar)

        txtMatricula = view.findViewById(R.id.txtMatricula)
        txtNombre = view.findViewById(R.id.txtNombre)
        txtDomicilio = view.findViewById(R.id.txtDomicilio)
        txtEspecialidad = view.findViewById(R.id.txtEspecialidad)
        txtFoto = view.findViewById(R.id.txtFoto)

        imgFoto = view.findViewById(R.id.imgAlumno)
    }

    @SuppressLint("CheckResult")
    fun eventosClic() {

        btnGuardar.setOnClickListener {

            val matricula = txtMatricula.text.toString().trim()
            val nombre = txtNombre.text.toString().trim()
            val domicilio = txtDomicilio.text.toString().trim()
            val especialidad = txtEspecialidad.text.toString().trim()
            val foto = imgFoto.tag?.toString() ?: ""

            if (matricula.isEmpty() || nombre.isEmpty() || domicilio.isEmpty() || especialidad.isEmpty()) {
                Toast.makeText(requireContext(), "Faltó información", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    txtFoto.setText(foto)

                    val dataAlumno = Alumno().apply {
                        this.matricula = matricula
                        this.nombre = nombre
                        this.domicilio = domicilio
                        this.especialidad = especialidad
                        this.foto = foto
                    }

                    val dbCheck = AlumnoDB(requireContext())
                    dbCheck.openDataBase()
                    val alumnoExistente: Alumno = dbCheck.getAlumno(matricula)
                    dbCheck.close()

                    if (alumnoExistente.id != 0) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("CRUD Alumnos")
                        builder.setMessage("El alumno ya existe. ¿Deseas actualizar datos?")

                        builder.setPositiveButton("Aceptar") { _, _ ->
                            val dbUpdate = AlumnoDB(requireContext())
                            dbUpdate.openDataBase()

                            var key = alumnoExistente.firebaseId
                            if (key.isNullOrEmpty()) {
                                key = databaseRef.push().key
                            }

                            if (!key.isNullOrEmpty()) {
                                dataAlumno.firebaseId = key
                                dataAlumno.pendienteSincronizacion = false
                                dbUpdate.actualizarAlumno(dataAlumno, alumnoExistente.id)
                                databaseRef.child(key).setValue(dataAlumno)
                                Toast.makeText(requireContext(), "Alumno actualizado", Toast.LENGTH_SHORT).show()
                            } else {
                                dataAlumno.pendienteSincronizacion = true
                                dbUpdate.actualizarAlumno(dataAlumno, alumnoExistente.id)
                                Toast.makeText(requireContext(), "Actualizado solo local", Toast.LENGTH_SHORT).show()
                            }

                            dbUpdate.close()
                        }

                        builder.setNegativeButton("Cancelar") { _, _ ->
                            Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_SHORT).show()
                        }

                        builder.show()

                    } else {
                        val dbInsert = AlumnoDB(requireContext())
                        dbInsert.openDataBase()

                        val key = databaseRef.push().key
                        if (!key.isNullOrEmpty()) {
                            dataAlumno.firebaseId = key
                            dataAlumno.pendienteSincronizacion = false
                            val idLocal: Long = dbInsert.insertarAlumno(dataAlumno)
                            databaseRef.child(key).setValue(dataAlumno)
                            Toast.makeText(requireContext(), "Alumno guardado. ID local = $idLocal", Toast.LENGTH_SHORT).show()
                        } else {
                            dataAlumno.pendienteSincronizacion = true
                            val idLocal: Long = dbInsert.insertarAlumno(dataAlumno)
                            Toast.makeText(requireContext(), "Guardado solo local. ID = $idLocal", Toast.LENGTH_SHORT).show()
                        }

                        dbInsert.close()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "ERROR: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnBuscar.setOnClickListener {
            val matricula = txtMatricula.text.toString().trim()

            if (matricula.isEmpty()) {
                Toast.makeText(requireContext(), "Capture matrícula", Toast.LENGTH_SHORT).show()
            } else {
                db = AlumnoDB(requireContext())
                db.openDataBase()

                try {
                    val alumno: Alumno = db.getAlumno(matricula)

                    if (alumno.id != 0) {
                        txtMatricula.setText(alumno.matricula)
                        txtNombre.setText(alumno.nombre)
                        txtDomicilio.setText(alumno.domicilio)
                        txtEspecialidad.setText(alumno.especialidad)

                        Glide.with(requireContext())
                            .load(alumno.foto)
                            .error(R.drawable.alumno)
                            .apply(RequestOptions().override(100, 100))
                            .into(imgFoto)

                        imgFoto.tag = alumno.foto
                        txtFoto.setText(imgFoto.tag?.toString())
                    } else {
                        Toast.makeText(requireContext(), "No se encontró al alumno", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "ERROR DB: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    db.close()
                }
            }
        }

        btnBorrar.setOnClickListener {
            if (txtMatricula.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Faltó capturar Matrícula",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("CRUD Alumnos")
                builder.setMessage("¿Deseas eliminar datos del alumno permanentemente?")

                builder.setPositiveButton("Aceptar") { _, _ ->
                    db = AlumnoDB(requireContext())
                    db.openDataBase()
                    val alumno: Alumno = db.getAlumno(txtMatricula.text.toString())
                    if (alumno.id != 0) {
                        val firebaseId = alumno.firebaseId
                        if (!firebaseId.isNullOrEmpty()) {
                            databaseRef.child(firebaseId).removeValue()
                        }

                        val id: Int = db.borrarAlumno(alumno.id)
                        if (id > 0) {
                            Toast.makeText(
                                requireContext(),
                                "Borrado con éxito",
                                Toast.LENGTH_SHORT
                            ).show()

                            txtMatricula.setText("")
                            txtNombre.setText("")
                            txtDomicilio.setText("")
                            txtEspecialidad.setText("")
                            txtFoto.setText("")
                            imgFoto.setImageResource(R.drawable.alumno)
                            imgFoto.tag = null
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No fue posible borrar alumno",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Alumno no encontrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    db.close()
                }

                builder.setNegativeButton("Cancelar") { _, _ ->
                    Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_SHORT).show()
                }

                builder.show()
            }
        }

        btnLimpiar.setOnClickListener {
            txtMatricula.setText("")
            txtNombre.setText("")
            txtDomicilio.setText("")
            txtEspecialidad.setText("")
            txtFoto.setText("")
            imgFoto.setImageResource(R.drawable.alumno)
            imgFoto.tag = null
        }

        imgFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri? = data.data
            imgFoto.setImageURI(uri)
            imgFoto.tag = uri.toString()
            txtFoto.setText(imgFoto.tag?.toString())
        }
    }

    fun AlumnoFromListaFragment() {
        if (!isAdded) return

        val alumnoLista = arguments?.getSerializable("miAlumno") as? Alumno

        alumnoLista?.let { alumno ->
            Toast.makeText(
                requireContext(),
                alumno.nombre.toString(),
                Toast.LENGTH_SHORT
            ).show()

            txtMatricula.setText(alumno.matricula)
            txtNombre.setText(alumno.nombre)
            txtDomicilio.setText(alumno.domicilio)
            txtEspecialidad.setText(alumno.especialidad)
            txtFoto.setText(alumno.foto.toString())
            imgFoto.tag = alumno.foto.toString()

            Glide.with(requireContext())
                .load(alumno.foto)
                .error(R.drawable.alumno)
                .apply(RequestOptions().override(100, 100))
                .into(imgFoto)

            imgFoto.tag = alumno.foto
            txtFoto.setText(imgFoto.tag?.toString())
        }
    }
}