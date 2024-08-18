package com.example.taparking

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*

class LayoutParkiranActivity : AppCompatActivity() {
    private lateinit var lp1: ImageView
    private lateinit var lp2: ImageView
    private lateinit var lp3: ImageView
    private lateinit var lp4: ImageView
    private lateinit var lp5: ImageView
    private lateinit var lp6: ImageView
    private lateinit var lp7: ImageView
    private lateinit var lp8: ImageView
    private lateinit var lp9: ImageView
    private lateinit var lp10: ImageView
    private lateinit var database: DatabaseReference
    private lateinit var rekomendasiParkiran: TextView

    private val previousData = mutableMapOf<String, Int>()
    private val CHANNEL_ID = "parking_notifications"
    private var initialLoadCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_parkiran)

        val backButton: ImageView = findViewById(R.id.parkir_back)
        backButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        lp1 = findViewById(R.id.lp_1)
        lp2 = findViewById(R.id.lp_2)
        lp3 = findViewById(R.id.lp_3)
        lp4 = findViewById(R.id.lp_4)
        lp5 = findViewById(R.id.lp_5)
        lp6 = findViewById(R.id.lp_6)
        lp7 = findViewById(R.id.lp_7)
        lp8 = findViewById(R.id.lp_8)
        lp9 = findViewById(R.id.lp_9)
        lp10 = findViewById(R.id.lp_10)
        rekomendasiParkiran = findViewById(R.id.rekomendasi_parkiran)

        database = FirebaseDatabase.getInstance().reference

        // Create Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Parking Notifications"
            val descriptionText = "Notifications for parking status changes"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        database.child("sensor").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sensorData = mapOf(
                    "sensor1" to (snapshot.child("sensor1").getValue(Int::class.java) ?: 0),
                    "sensor2" to (snapshot.child("sensor2").getValue(Int::class.java) ?: 0),
                    "sensor3" to (snapshot.child("sensor3").getValue(Int::class.java) ?: 0),
                    "sensor4" to (snapshot.child("sensor4").getValue(Int::class.java) ?: 0),
                    "sensor5" to (snapshot.child("sensor5").getValue(Int::class.java) ?: 0),
                    "sensor6" to (snapshot.child("sensor6").getValue(Int::class.java) ?: 0),
                    "sensor7" to (snapshot.child("sensor7").getValue(Int::class.java) ?: 0),
                    "sensor8" to (snapshot.child("sensor8").getValue(Int::class.java) ?: 0),
                    "sensor9" to (snapshot.child("sensor9").getValue(Int::class.java) ?: 0),
                    "sensor10" to (snapshot.child("sensor10").getValue(Int::class.java) ?: 0)
                )

                // Log sensor data for debugging
                Log.d("LayoutParkiranActivity", "Sensor Data: $sensorData")

                if (initialLoadCompleted) {
                    // Check for changes and show notifications if needed
                    sensorData.forEach { (key, value) ->
                        val previousValue = previousData[key] ?: 0
                        if (previousValue == 0 && value == 1) {
                            // Sensor changed from 0 to 1, show notification
                            showNotification(key)
                        }
                        // Update previousData regardless of whether a notification was shown
                        previousData[key] = value
                    }
                } else {
                    // Initial load completed, update previousData and UI
                    previousData.putAll(sensorData)
                    initialLoadCompleted = true
                }

                // Update UI with confirmed data
                updateSlotImages(previousData)
                updateRecommendation(sensorData)
                updateSisaparkir(sensorData)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
                Log.e("LayoutParkiranActivity", "Database error: ${error.message}")
            }
        })
    }

    private fun updateSlotImages(sensorData: Map<String, Int>) {
        Log.d("LayoutParkiranActivity", "Updating Slot Images with Data: $sensorData")
        lp1.setBackgroundColor(getColorForStatus(sensorData["sensor1"] ?: 0))
        lp2.setBackgroundColor(getColorForStatus(sensorData["sensor2"] ?: 0))
        lp3.setBackgroundColor(getColorForStatus(sensorData["sensor3"] ?: 0))
        lp4.setBackgroundColor(getColorForStatus(sensorData["sensor4"] ?: 0))
        lp5.setBackgroundColor(getColorForStatus(sensorData["sensor5"] ?: 0))
        lp6.setBackgroundColor(getColorForStatus(sensorData["sensor10"] ?: 0))
        lp7.setBackgroundColor(getColorForStatus(sensorData["sensor9"] ?: 0))
        lp8.setBackgroundColor(getColorForStatus(sensorData["sensor8"] ?: 0))
        lp9.setBackgroundColor(getColorForStatus(sensorData["sensor7"] ?: 0))
        lp10.setBackgroundColor(getColorForStatus(sensorData["sensor6"] ?: 0))
    }

    private fun getColorForStatus(status: Int): Int {
        return if (status == 1) {
            ContextCompat.getColor(this, R.color.hijau)
        } else {
            ContextCompat.getColor(this, R.color.red)
        }
    }

    private fun showNotification(sensorKey: String) {
        // Map sensorKey to slot number
        val slotNumber = when (sensorKey) {
            "sensor1" -> 1
            "sensor2" -> 2
            "sensor3" -> 3
            "sensor4" -> 4
            "sensor5" -> 5
            "sensor6" -> 6
            "sensor7" -> 7
            "sensor8" -> 8
            "sensor9" -> 9
            "sensor10" -> 10
            else -> 0 // Default to 0 if sensorKey is not recognized
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Parkiran Berubah")
            .setContentText("Slot $slotNumber berubah status. Apakah Anda meninggalkan parkiran?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val intent = Intent(this, LayoutParkiranActivity::class.java)

        // Set FLAG_IMMUTABLE for Android 12 and above
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        builder.setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            notify(sensorKey.hashCode(), builder.build())
        }

        // Show confirmation dialog
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Slot $slotNumber berubah status menjadi kosong. Apakah Anda meninggalkan parkiran?")
            .setPositiveButton("Ya") { _, _ ->
                // Confirm and update UI
                updateSlotImages(previousData)
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateRecommendation(sensorData: Map<String, Int>) {
        val recommendedSlot = when {
            sensorData["sensor1"] == 1 -> "1A"
            sensorData["sensor10"] == 1 -> "1B"
            sensorData["sensor2"] == 1 -> "2A"
            sensorData["sensor9"] == 1 -> "2B"
            sensorData["sensor3"] == 1 -> "3A"
            sensorData["sensor8"] == 1 -> "3B"
            sensorData["sensor4"] == 1 -> "4A"
            sensorData["sensor7"] == 1 -> "4B"
            sensorData["sensor5"] == 1 -> "5A"
            sensorData["sensor6"] == 1 -> "5B"
            else -> null
        }

        val recommendationText = recommendedSlot?.let {
            "Parkir di slot $it."
        } ?: "Semua parkiran penuh."

        rekomendasiParkiran.text = recommendationText

        // Kirim data led_rekom ke Firebase
        val ledRekom = when (recommendedSlot) {
            "1A" -> 1
            "1B" -> 10
            "2A" -> 2
            "2B" -> 9
            "3A" -> 3
            "3B" -> 8
            "4A" -> 4
            "4B" -> 7
            "5A" -> 5
            "5B" -> 6
            else -> 0 // Default ke 0 jika semua parkiran penuh
        }

        database.child("led").setValue(ledRekom)
            .addOnSuccessListener {
                Log.d("LayoutParkiranActivity", "led_rekom updated to $ledRekom")
            }
            .addOnFailureListener { error ->
                Log.e("LayoutParkiranActivity", "Failed to update led_rekom: ${error.message}")
            }
    }

    private fun updateSisaparkir(sensorData: Map<String, Int>) {
        val emptySlots = sensorData.values.count { it == 0 }
        val totalSlots = sensorData.size
        val remainingSlots = totalSlots - emptySlots

        // Update Firebase with the number of remaining slots
        database.child("sisaparkir").setValue(remainingSlots)
            .addOnSuccessListener {
                Log.d("LayoutParkiranActivity", "sisaparkir updated to $remainingSlots")
            }
            .addOnFailureListener { error ->
                Log.e("LayoutParkiranActivity", "Failed to update sisaparkir: ${error.message}")
            }
    }
}
