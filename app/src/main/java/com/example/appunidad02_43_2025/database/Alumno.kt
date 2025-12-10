package com.example.appunidad02_43_2025.database

import java.io.Serializable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
data class Alumno(

    var id: Int = 0,
    var firebaseId: String? = null,
    var matricula: String = "",
    var nombre: String = "",

    @SerializedName("carrera")
    var especialidad: String = "",

    var domicilio: String = "",
    var foto: String = "",

    var pendienteSincronizacion: Boolean = false): Serializable