package com.example.appunidad02_43_2025.database

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class AlumnoDB(private val context: Context) {
    private val dbHelper : AlumnoDbHelper = AlumnoDbHelper(context)
    private lateinit var db : SQLiteDatabase
    private val leerRegistro = arrayOf(
        DefinirTabla.Alumnos.ID,
        DefinirTabla.Alumnos.MATRICULA,
        DefinirTabla.Alumnos.NOMBRE,
        DefinirTabla.Alumnos.DOMICILIO,
        DefinirTabla.Alumnos.ESPECIALIDAD,
        DefinirTabla.Alumnos.FOTO
    )

    fun openDataBase() {
        db = dbHelper.writableDatabase
    }

    fun insertarAlumno(alumno: Alumno): Long{
        val value = ContentValues().apply {
            put(DefinirTabla.Alumnos.MATRICULA,alumno.matricula)
            put(DefinirTabla.Alumnos.NOMBRE,alumno.nombre)
            put(DefinirTabla.Alumnos.DOMICILIO,alumno.domicilio)
            put(DefinirTabla.Alumnos.ESPECIALIDAD,alumno.especialidad)
            put(DefinirTabla.Alumnos.FOTO,alumno.foto)

        }

        return db.insert(DefinirTabla.Alumnos.TABLA,null,value)

    }

    fun actualizarAlumno(alumno: Alumno, id: Int): Int {
        val value = ContentValues().apply {
            put(DefinirTabla.Alumnos.MATRICULA,alumno.matricula)
            put(DefinirTabla.Alumnos.NOMBRE,alumno.nombre)
            put(DefinirTabla.Alumnos.DOMICILIO,alumno.domicilio)
            put(DefinirTabla.Alumnos.ESPECIALIDAD,alumno.especialidad)
            put(DefinirTabla.Alumnos.FOTO,alumno.foto)

        }

        return db.update(DefinirTabla.Alumnos.TABLA, value,"${ DefinirTabla.Alumnos.ID } = ?",
            arrayOf(id.toString())
            )
    }

    fun borrarAlumno(id:Int): Int{

        return db.delete(DefinirTabla.Alumnos.TABLA, "${DefinirTabla.Alumnos.ID}= ?", arrayOf(id.toString()))
    }

    fun mostrarAlumno(cursor: Cursor): Alumno {

        if(!cursor.isAfterLast){
            return Alumno().apply {
                id = cursor.getInt(0)
                matricula = cursor.getString(1)
                nombre = cursor.getString(2)
                domicilio = cursor.getString(3)
                especialidad = cursor.getString(4)
                foto = cursor.getString(5)


            }
        }

        return Alumno()
    }

}