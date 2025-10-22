package com.example.appunidad02_43_2025.database

import android.provider.BaseColumns

class DefinirTabla {
    object Alumnos : BaseColumns{
        const val TABLA = "Alumnos"
        const val ID = "id"
        const val MATRICULA = "matricula"
        const val NOMBRE = "nombre"
        const val ESPECIALIDAD = "especialidad"
        const val FOTO = "foto"
        const val DOMICILIO = "domicilio"



    }
}