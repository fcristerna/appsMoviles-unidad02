package com.example.appunidad02_43_2025.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class AlumnoDB(private val context: Context) {

    private val dbHelper: AlumnoDbHelper = AlumnoDbHelper(context)
    private lateinit var db: SQLiteDatabase

    private val leerRegistro = arrayOf(
        DefinirTabla.Alumnos.ID,
        DefinirTabla.Alumnos.MATRICULA,
        DefinirTabla.Alumnos.NOMBRE,
        DefinirTabla.Alumnos.DOMICILIO,
        DefinirTabla.Alumnos.ESPECIALIDAD,
        DefinirTabla.Alumnos.FOTO,
        DefinirTabla.Alumnos.FIREBASE_ID,
        DefinirTabla.Alumnos.PENDIENTE
    )

    fun openDataBase() {
        db = dbHelper.writableDatabase
    }

    fun close() {
        if (this::db.isInitialized && db.isOpen) {
            db.close()
        }
    }

    fun insertarAlumno(alumno: Alumno): Long {
        val value = ContentValues().apply {
            put(DefinirTabla.Alumnos.MATRICULA, alumno.matricula)
            put(DefinirTabla.Alumnos.NOMBRE, alumno.nombre)
            put(DefinirTabla.Alumnos.DOMICILIO, alumno.domicilio)
            put(DefinirTabla.Alumnos.ESPECIALIDAD, alumno.especialidad)
            put(DefinirTabla.Alumnos.FOTO, alumno.foto)
            put(DefinirTabla.Alumnos.FIREBASE_ID, alumno.firebaseId)
            put(
                DefinirTabla.Alumnos.PENDIENTE,
                if (alumno.pendienteSincronizacion) 1 else 0
            )
        }
        return db.insert(DefinirTabla.Alumnos.TABLA, null, value)
    }

    fun actualizarAlumno(alumno: Alumno, id: Int): Int {
        val value = ContentValues().apply {
            put(DefinirTabla.Alumnos.MATRICULA, alumno.matricula)
            put(DefinirTabla.Alumnos.NOMBRE, alumno.nombre)
            put(DefinirTabla.Alumnos.DOMICILIO, alumno.domicilio)
            put(DefinirTabla.Alumnos.ESPECIALIDAD, alumno.especialidad)
            put(DefinirTabla.Alumnos.FOTO, alumno.foto)
            put(DefinirTabla.Alumnos.FIREBASE_ID, alumno.firebaseId)
            put(
                DefinirTabla.Alumnos.PENDIENTE,
                if (alumno.pendienteSincronizacion) 1 else 0
            )
        }

        return db.update(
            DefinirTabla.Alumnos.TABLA,
            value,
            "${DefinirTabla.Alumnos.ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun borrarAlumno(id: Int): Int {
        return db.delete(
            DefinirTabla.Alumnos.TABLA,
            "${DefinirTabla.Alumnos.ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun mostrarAlumno(cursor: Cursor): Alumno {
        return Alumno().apply {
            id = cursor.getInt(0)
            matricula = cursor.getString(1)
            nombre = cursor.getString(2)
            domicilio = cursor.getString(3)
            especialidad = cursor.getString(4)
            foto = cursor.getString(5)
            firebaseId = cursor.getString(6)
            pendienteSincronizacion = cursor.getInt(7) == 1
        }
    }

    fun getAlumno(matricula: String): Alumno {
        val dbRead = dbHelper.readableDatabase
        val cursor = dbRead.query(
            DefinirTabla.Alumnos.TABLA,
            leerRegistro,
            "${DefinirTabla.Alumnos.MATRICULA} = ?",
            arrayOf(matricula),
            null,
            null,
            null
        )

        var alumno = Alumno()
        if (cursor.moveToFirst()) {
            alumno = mostrarAlumno(cursor)
        }

        cursor.close()
        //dbRead.close() //
        return alumno
    }

    fun leerTodos(): ArrayList<Alumno> {
        val dbRead = dbHelper.readableDatabase
        val cursor = dbRead.query(
            DefinirTabla.Alumnos.TABLA,
            leerRegistro,
            null,
            null,
            null,
            null,
            null
        )
        val listAlumno = ArrayList<Alumno>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val alumno = mostrarAlumno(cursor)
                listAlumno.add(alumno)
                cursor.moveToNext()
            }
        }
        cursor.close()
        //dbRead.close()
        return listAlumno
    }

    fun getAlumnosPendientes(): ArrayList<Alumno> {
        val dbRead = dbHelper.readableDatabase
        val cursor = dbRead.query(
            DefinirTabla.Alumnos.TABLA,
            leerRegistro,
            "${DefinirTabla.Alumnos.PENDIENTE} = ?",
            arrayOf("1"),
            null,
            null,
            null
        )

        val lista = ArrayList<Alumno>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val alumno = mostrarAlumno(cursor)
                lista.add(alumno)
                cursor.moveToNext()
            }
        }
        cursor.close()
        // dbRead.close()
        return lista
    }

    fun buscarAlumnoPorNombre(nombre: String): ArrayList<Alumno> {
        val dbRead = dbHelper.readableDatabase
        val cursor = dbRead.query(
            DefinirTabla.Alumnos.TABLA,
            leerRegistro,
            "${DefinirTabla.Alumnos.NOMBRE} LIKE ?",
            arrayOf("%$nombre%"),
            null,
            null,
            null
        )

        val listAlumno = ArrayList<Alumno>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val alumno = mostrarAlumno(cursor)
                listAlumno.add(alumno)
                cursor.moveToNext()
            }
        }
        cursor.close()
        // dbRead.close()
        return listAlumno
    }

    fun buscarAlumnoPorMatricula(matricula: String): ArrayList<Alumno> {
        val dbRead = dbHelper.readableDatabase
        val cursor = dbRead.query(
            DefinirTabla.Alumnos.TABLA,
            leerRegistro,
            "${DefinirTabla.Alumnos.MATRICULA} LIKE ?",
            arrayOf("%$matricula%"),
            null,
            null,
            null
        )

        val listAlumno = ArrayList<Alumno>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val alumno = mostrarAlumno(cursor)
                listAlumno.add(alumno)
                cursor.moveToNext()
            }
        }
        cursor.close()
        // dbRead.close()
        return listAlumno
    }

    fun buscarAlumno(query: String): ArrayList<Alumno> {
        val dbRead = dbHelper.readableDatabase
        val cursor = dbRead.query(
            DefinirTabla.Alumnos.TABLA,
            leerRegistro,
            "${DefinirTabla.Alumnos.NOMBRE} LIKE ? OR ${DefinirTabla.Alumnos.MATRICULA} LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null,
            null,
            null
        )

        val listAlumno = ArrayList<Alumno>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val alumno = mostrarAlumno(cursor)
                listAlumno.add(alumno)
                cursor.moveToNext()
            }
        }
        cursor.close()
        // dbRead.close()
        return listAlumno
    }
}