package com.lyhin.cuan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private var coins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvCoins = findViewById<TextView>(R.id.tvCoins)
        val btnMerge = findViewById<Button>(R.id.btnMerge)
        val etNomor = findViewById<EditText>(R.id.etNomorDena)
        val btnWithdraw = findViewById<Button>(R.id.btnWithdraw)

        val database = FirebaseDatabase.getInstance().reference

        btnMerge.setOnClickListener {
            coins += 10
            tvCoins.text = "Koin: $coins"
        }

        btnWithdraw.setOnClickListener {
            val nomor = etNomor.text.toString().trim()
            if (nomor.isEmpty()) {
                Toast.makeText(this, "Isi nomor DANA/OVO dulu!", Toast.LENGTH_SHORT).show()
            } else if (coins < 100) {
                Toast.makeText(this, "Koin minimal 100 untuk penarikan!", Toast.LENGTH_SHORT).show()
            } else {
                val wdData = mapOf(
                    "nomor" to nomor,
                    "jumlah_koin" to coins,
                    "status" to "pending"
                )
                database.child("penarikan").push().setValue(wdData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Permintaan Penarikan Terkirim!", Toast.LENGTH_LONG).show()
                        coins = 0
                        tvCoins.text = "Koin: $coins"
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal mengirim: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}

