package com.example.konumkullanimi

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ActionMenuView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.konumkullanimi.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    private lateinit var tasarim: ActivityMainBinding
    private var izinKontrol = 0
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasarim = ActivityMainBinding.inflate(layoutInflater)
        setContentView(tasarim.root)
    flpc = LocationServices.getFusedLocationProviderClient(this)
        tasarim.buttonKonumAl.setOnClickListener {
            izinKontrol = ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (izinKontrol != PackageManager.PERMISSION_GRANTED) {
                //Daha önce izin verilmediyse veya izin onaylanmamışsa
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            } else {
                //İzin onayı verildiyse

                locationTask = flpc.lastLocation
                konumBilgisiAl()
            }
        }
    }
fun konumBilgisiAl(){
    locationTask.addOnSuccessListener {
        if (it!=null){
            tasarim.textViewEnlem.text = "Enlem : ${it.latitude}"
            tasarim.textViewBoylam.text = "Boylam : ${it.longitude}"
        }else{
            tasarim.textViewEnlem.text = "Enlem : Alınamadı"
            tasarim.textViewBoylam.text = "Boylam : Alınamadı"
        }
    }
}
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        izinKontrol = ContextCompat.checkSelfPermission(
            this@MainActivity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "İzin Kabul Edildi", Toast.LENGTH_LONG).show()
                locationTask = flpc.lastLocation
                konumBilgisiAl()
            } else {
                Toast.makeText(applicationContext, "İzin Red Edildi", Toast.LENGTH_LONG).show()

            }
        }

    }
}