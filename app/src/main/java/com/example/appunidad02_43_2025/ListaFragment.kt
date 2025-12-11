package com.example.appunidad02_43_2025

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appunidad02_43_2025.database.Alumno
import com.example.appunidad02_43_2025.database.AlumnoDB
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class ListaFragment : Fragment() {
    private lateinit var adapter: DbAdapter
    private lateinit var rcvLista: RecyclerView
    private lateinit var btnNuevof: FloatingActionButton
    private lateinit var listaAlumnos: ArrayList<Alumno>
    private lateinit var db: AlumnoDB
    private var srv: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }
    private fun sincronizarFirebaseConLocal() {
        val ctx = context ?: return

        val dbLocal = AlumnoDB(ctx)
        dbLocal.openDataBase()
        val alumnosLocales = dbLocal.leerTodos()
        dbLocal.close()

        val ref = FirebaseDatabase.getInstance().getReference("alumnos")

        for (alumno in alumnosLocales) {
            if (alumno.pendienteSincronizacion) {
                if (alumno.firebaseId.isNullOrEmpty()) {
                    val key = ref.push().key ?: continue
                    alumno.firebaseId = key

                    val dbUpdate = AlumnoDB(ctx)
                    dbUpdate.openDataBase()
                    dbUpdate.actualizarAlumno(alumno, alumno.id)
                    dbUpdate.close()
                }

                val key = alumno.firebaseId ?: continue

                ref.child(key).setValue(alumno)
                    .addOnSuccessListener {
                        val ctxInner = context ?: return@addOnSuccessListener
                        alumno.pendienteSincronizacion = false
                        val dbUpdate = AlumnoDB(ctxInner)
                        dbUpdate.openDataBase()
                        dbUpdate.actualizarAlumno(alumno, alumno.id)
                        dbUpdate.close()
                    }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        sincronizarFirebaseConLocal()
        cargarAlumnos()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarComponentes(view)
        eventosClic()
        cargarAlumnos()
    }

    private fun iniciarComponentes(view: View) {
        btnNuevof = view.findViewById(R.id.btnAgregarAlumno)
        srv = view.findViewById(R.id.srvAlumnos)
        rcvLista = view.findViewById(R.id.recId)

        rcvLista.layoutManager = LinearLayoutManager(requireContext())
        listaAlumnos = ArrayList()
        adapter = DbAdapter(listaAlumnos, requireContext())
        db = AlumnoDB(requireContext())
        rcvLista.adapter = adapter
    }

    private fun eventosClic() {
        btnNuevof.setOnClickListener {
            cambiarFragment()
        }

        adapter.setOnClickListener(View.OnClickListener { view ->
            val pos: Int = rcvLista.getChildAdapterPosition(view)
            val alumno = (rcvLista.adapter as DbAdapter).getItem(pos)

            val bundle = Bundle().apply {
                putSerializable("miAlumno", alumno)
            }

            val alumnoFragment = AlumnosFragment()
            alumnoFragment.arguments = bundle

            val bottom =
                requireActivity().findViewById<BottomNavigationView>(R.id.btnNavegador)
            bottom.menu.findItem(R.id.btnAlumnos).isChecked = true

            parentFragmentManager.beginTransaction()
                .replace(R.id.frmContenedor, alumnoFragment)
                .addToBackStack(null)
                .commit()
        })

        srv?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun cargarAlumnos() {
        try {
            db.openDataBase()
            val nuevos = db.leerTodos()
            db.close()

            adapter.actualizarLista(nuevos)
            Toast.makeText(
                requireContext(),
                "Alumnos registrados: ${nuevos.size}",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Error al cargar alumnos: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun cambiarFragment() {
        val bottom =
            requireActivity().findViewById<BottomNavigationView>(R.id.btnNavegador)
        bottom.menu.findItem(R.id.btnAlumnos).isChecked = true

        (activity as? MainActivity)?.setSelectedTab(R.id.btnAlumnos)
    }
}