package com.example.taparking

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class resetpassword : AppCompatActivity() {
    private lateinit var etPassword: EditText
    private lateinit var resetBtn: ImageView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword)

        etPassword = findViewById(R.id.rp_et_1)
        resetBtn = findViewById(R.id.rp_img_2)
        auth = FirebaseAuth.getInstance()

        resetBtn.setOnClickListener {
            val sPassword = etPassword.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener {
                    // Jika sukses, tampilkan toast dan arahkan ke LoginActivity
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    // Jika gagal, tampilkan pesan error tanpa mengarahkan ke LoginActivity
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }
}
