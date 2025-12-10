package com.example.appunidad02_43_2025

import android.graphics.Bitmap
import com.example.appunidad02_43_2025.database.Alumno
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object QrUtils {

    fun alumnoToJson(alumno: Alumno): String {
        val gson = Gson()
        val mapa = mapOf(
            "matricula" to alumno.matricula,
            "nombre" to alumno.nombre,
            "carrera" to alumno.especialidad,
            "domicilio" to alumno.domicilio,
            "foto" to alumno.foto
        )
        return gson.toJson(mapa)
    }

    fun generarQrBitmap(texto: String, size: Int = 600): Bitmap {
        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(texto, BarcodeFormat.QR_CODE, size, size, null)

        val ancho = bitMatrix.width
        val alto = bitMatrix.height
        val bmp = Bitmap.createBitmap(ancho, alto, Bitmap.Config.RGB_565)

        for (x in 0 until ancho) {
            for (y in 0 until alto) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) -0x1000000 else -0x1)
            }
        }
        return bmp
    }
}