package com.example.appunidad02_43_2025
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Filter
import android.widget.Filterable

import androidx.recyclerview.widget.RecyclerView
import com.example.appunidad02_43_2025.database.Alumno

class DbAdapter (
    private var listaAlumno : ArrayList<Alumno>,
    private val contexto : Context
) : RecyclerView.Adapter<DbAdapter.ViewHolder>(),
        View.OnClickListener, Filterable{
            private val inflater : LayoutInflater = LayoutInflater.from(contexto)
    private var listener : View.OnClickListener? = null
    private var listaAlumnoCompleta: ArrayList<Alumno> = ArrayList(listaAlumno)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.alumno_item,parent,false)
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

        // cargar imagen

        try {
            val uri = Uri.parse(alumno.foto)
            holder.idImagen.setImageURI(uri)
        } catch (e:Exception) {
            holder.idImagen.setImageResource(R.drawable.alumno)
        }

    }

    override fun getItemCount(): Int {
        return listaAlumno.size

    }

    override fun onClick(p0: View?) {
        listener?.onClick(p0)
    }

    fun actualizarLista(lista:ArrayList<Alumno>){
        listaAlumno = lista
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultados = FilterResults()

                if (constraint.isNullOrEmpty()) {
                    resultados.values = listaAlumnoCompleta
                    resultados.count = listaAlumnoCompleta.size
                } else {
                    val query = constraint.toString().lowercase().trim()
                    val listaFiltrada = ArrayList<Alumno>()

                    for (alumno in listaAlumnoCompleta) {
                        if (alumno.nombre.lowercase().contains(query) ||
                            alumno.matricula.lowercase().contains(query)) {
                            listaFiltrada.add(alumno)
                        }
                    }

                    resultados.values = listaFiltrada
                    resultados.count = listaFiltrada.size
                }

                return resultados
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.values != null) {
                    listaAlumno = results.values as ArrayList<Alumno>
                    notifyDataSetChanged()
                }
            }
        }
    }

    // declarar clase interna
    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        // los elementos del item alumno
        val txtNombre : TextView = itemView.findViewById(R.id.txtAlumnoNombre)
        val txtMatricula : TextView = itemView.findViewById(R.id.txtMatricula)
        val txtCarrera : TextView = itemView.findViewById(R.id.txtCarrera)
        val idImagen : ImageView = itemView.findViewById(R.id.foto)
    }
        }
