package com.example.taparking

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.taparking.BookingActivity
import com.example.taparking.HistoryActivity
import com.example.taparking.LocationActivity
import com.example.taparking.PayActivity
import com.example.taparking.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser

class DashboardActivity : AppCompatActivity() {
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val txt1: TextView = findViewById(R.id.d_txt1)
        val payImageView: ImageView = findViewById(R.id.d_pay)
        val locationImageView: ImageView = findViewById(R.id.d_location)
        val bookingImageView: ImageView = findViewById(R.id.d_booking)
        val historyImageView: ImageView = findViewById(R.id.d_history)
        val walletImageView: ImageView = findViewById(R.id.d_wallet)
        val mobilImageView: ImageView = findViewById(R.id.d_mobil)

        if (currentUser != null) {
            val email = currentUser.email
            val userName = email?.substringBefore("@") ?: "User"
            txt1.text = "Hi, $userName"
        }
        payImageView.setOnClickListener {
            val intent = Intent(this, PayActivity::class.java)
            startActivity(intent)
        }

        locationImageView.setOnClickListener {
            val intent = Intent(this, LayoutParkiranActivity::class.java)
            startActivity(intent)
        }

        bookingImageView.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        historyImageView.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        walletImageView.setOnClickListener {
            val intent = Intent(this, wallet::class.java)
            startActivity(intent)
        }
        mobilImageView.setOnClickListener {
            val intent = Intent(this, selectvehicle::class.java)
            startActivity(intent)
        }
    }
}
