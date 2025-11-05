package com.example.appunidad02_43_2025

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appunidad02_43_2025.database.Alumno
import com.example.appunidad02_43_2025.database.AlumnoDB
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class ListaFragment : Fragment() {
    private lateinit var adapter : DbAdapter
    private lateinit var rcvLista : RecyclerView
    private lateinit var btnNuevof : FloatingActionButton
    private lateinit var listaAlumnos : ArrayList<Alumno>
    private lateinit var db : AlumnoDB
    private lateinit var srv : SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista, container, false)
        iniciarComponentes(view)
        eventosClic()
        cargarAlumnos()
        return view
    }


    fun iniciarComponentes(view: View){
        btnNuevof = view.findViewById(R.id.btnAgregarAlumno)
        srv = view.findViewById(R.id.srvAlumnos)
        rcvLista = view.findViewById(R.id.recId)
        rcvLista.layoutManager = LinearLayoutManager(requireContext())
        listaAlumnos = ArrayList()
        adapter = DbAdapter(listaAlumnos,requireContext())
        db = AlumnoDB(requireContext())
        rcvLista.adapter = adapter

    }
    public fun cambiarFragment(){
        parentFragmentManager.beginTransaction().replace(R.id.frmContenedor,
            AlumnosFragment()).addToBackStack(null).commit()


        (activity as? MainActivity)?.setSelectedTab(R.id.btnAlumnos)


    }
    fun eventosClic(){
        btnNuevof.setOnClickListener(View.OnClickListener{
            cambiarFragment()
        })
    }


    private fun cargarAlumnos() {
        db.openDataBase()
        val alumnosDB = db.leerTodos()
        db.close()

        listaAlumnos.clear()
        listaAlumnos.addAll(alumnosDB)

        adapter.actualizarLista(listaAlumnos)

        Toast.makeText(requireContext(), "Lista actualizada", Toast.LENGTH_SHORT).show()
    }


}