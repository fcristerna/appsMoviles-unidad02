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

class ListaFragment : Fragment() {
    private lateinit var adapter: DbAdapter
    private lateinit var rcvLista: RecyclerView
    private lateinit var btnNuevof: FloatingActionButton
    private lateinit var listaAlumnos: ArrayList<Alumno>
    private lateinit var db: AlumnoDB
    private var srv: SearchView? = null   // igual que en el código bien (nullable)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // En onCreateView, solo inflamos la vista
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // onViewCreated es el lugar para inicializar vistas y cargar datos
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
        // FAB nuevo alumno → usa cambiarFragment()
        btnNuevof.setOnClickListener {
            cambiarFragment()
        }

        // Click en item del Recycler (igual que código bien)
        adapter.setOnClickListener(View.OnClickListener { view ->
            val pos: Int = rcvLista.getChildAdapterPosition(view)
            val alumno = (rcvLista.adapter as DbAdapter).getItem(pos)

            val bundle = Bundle().apply {
                putSerializable("miAlumno", alumno)
            }

            val alumnoFragment = AlumnosFragment()
            alumnoFragment.arguments = bundle

            // Marcar el botón de alumnos en el BottomNavigation
            val bottom =
                requireActivity().findViewById<BottomNavigationView>(R.id.btnNavegador)
            bottom.menu.findItem(R.id.btnAlumnos).isChecked = true

            // Abrir el fragment de detalle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frmContenedor, alumnoFragment)
                .addToBackStack(null)
                .commit()
        })

        // Buscador
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

        // Aquí respetamos tu MainActivity con setSelectedTab
        (activity as? MainActivity)?.setSelectedTab(R.id.btnAlumnos)
    }
}