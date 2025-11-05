package com.example.appunidad02_43_2025

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.btnNavegador)

        if(savedInstanceState==null){
            cambiarFragment(InicioFragment())
        }

        bottomNavigationView.setOnItemSelectedListener {
            menuItem ->
            when(menuItem.itemId){
                R.id.btnInicio -> {
                    cambiarFragment(InicioFragment())
                    true
                }

                R.id.btnAlumnos -> {
                    cambiarFragment(AlumnosFragment())
                    true
                }
                R.id.btnAcercade -> {
                    cambiarFragment(AcercaFragment())
                    true
                }
                R.id.btnLista -> {
                    cambiarFragment(ListaFragment())
                    true
                }
                R.id.btnSalir -> {
                    cambiarFragment(SalirFragment())
                    true
                }
                else -> false


            }
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    public fun cambiarFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frmContenedor,fragment).commit()
    }

    fun setSelectedTab(tabId : Int){
        bottomNavigationView.selectedItemId = tabId
    }

}