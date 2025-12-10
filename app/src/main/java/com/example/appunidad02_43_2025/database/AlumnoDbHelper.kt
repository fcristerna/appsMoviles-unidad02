package com.example.appunidad02_43_2025.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlumnoDbHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "sistemas"
        private const val DATABASE_VERSION = 6
        private const val INTEGER_TYPE = " INTEGER"
        private const val TEXT_TYPE = " TEXT"
        private const val COMMA = " ,"
        private const val SQL_CREATE_ALUMNO =
            "CREATE TABLE ${DefinirTabla.Alumnos.TABLA} (" +
                    "${DefinirTabla.Alumnos.ID} INTEGER PRIMARY KEY AUTOINCREMENT $COMMA " +
                    "${DefinirTabla.Alumnos.MATRICULA} $TEXT_TYPE $COMMA " +
                    "${DefinirTabla.Alumnos.NOMBRE} $TEXT_TYPE $COMMA " +
                    "${DefinirTabla.Alumnos.DOMICILIO} $TEXT_TYPE $COMMA " +
                    "${DefinirTabla.Alumnos.ESPECIALIDAD} $TEXT_TYPE $COMMA " +
                    "${DefinirTabla.Alumnos.FOTO} $TEXT_TYPE $COMMA " +
                    "${DefinirTabla.Alumnos.FIREBASE_ID} $TEXT_TYPE $COMMA " +
                    "${DefinirTabla.Alumnos.PENDIENTE} $INTEGER_TYPE DEFAULT 0" +
                    ")"

        private const val SQL_DELETE_ALUMNO =
            "DROP TABLE IF EXISTS ${DefinirTabla.Alumnos.TABLA}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ALUMNO)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ALUMNO)
        db?.execSQL(SQL_CREATE_ALUMNO)
    }

}