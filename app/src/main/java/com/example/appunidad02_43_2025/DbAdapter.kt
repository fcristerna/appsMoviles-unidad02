package com.example.appunidad02_43_2025

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appunidad02_43_2025.database.Alumno
import java.util.Locale

class DbAdapter(
    private var listaAlumno: ArrayList<Alumno>,
    private val contexto: Context
) : RecyclerView.Adapter<DbAdapter.ViewHolder>(),
    View.OnClickListener, Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(contexto)
    private var listener: View.OnClickListener? = null
    private var listaAlumnoOriginal: ArrayList<Alumno> = ArrayList(listaAlumno)

    // Clase interna ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtAlumnoNombre)
        val txtMatricula: TextView = itemView.findViewById(R.id.txtMatricula)
        val txtCarrera: TextView = itemView.findViewById(R.id.txtCarrera)
        val idImagen: ImageView = itemView.findViewById(R.id.foto)    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = inflater.inflate(R.layout.alumno_item, parent, false)
        view.setOnClickListener(this)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val alumno = listaAlumno[position]
        holder.txtMatricula.text = alumno.matricula
        holder.txtNombre.text = alumno.nombre
        holder.txtCarrera.text = alumno.especialidad

        Glide.with(contexto)
            .load(alumno.foto)
            .error(R.drawable.alumno)
            .apply(RequestOptions().override(100, 100))
            .into(holder.idImagen)
    }
    fun getItem(position: Int): Alumno {
        return listaAlumno[position]
    }

    override fun getItemCount(): Int {
        return listaAlumno.size
    }

    override fun onClick(v: View?) {
        listener?.onClick(v)
    }

    fun actualizarLista(lista: ArrayList<Alumno>) {
        listaAlumno = lista
        listaAlumnoOriginal = ArrayList(lista)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Alumno>()
                val charSearch = constraint?.toString()?.lowercase(Locale.ROOT) ?: ""

                if (charSearch.isEmpty()) {
                    filteredList.addAll(listaAlumnoOriginal)
                } else {
                    for (alumno in listaAlumnoOriginal) {
                        if (alumno.nombre.lowercase(Locale.ROOT).contains(charSearch) ||
                            alumno.matricula.lowercase(Locale.ROOT).contains(charSearch)
                        ) {
                            filteredList.add(alumno)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaAlumno.clear()
                if (results?.values != null) {
                    listaAlumno.addAll(results.values as ArrayList<Alumno>)
                }
                notifyDataSetChanged()
            }
        }
    }
}