package com.example.petconnectt

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class VaccinationActivity : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var btnSetReminder: Button
    private lateinit var selectedDateTimeText: TextView

    private val NOTIFICATION_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination)

        datePicker = findViewById(R.id.datePicker)
        timePicker = findViewById(R.id.timePicker)
        btnSetReminder = findViewById(R.id.btnSetReminder)
        selectedDateTimeText = findViewById(R.id.selectedDateTimeText)

        // ✅ Set time picker to 24-hour mode
        timePicker.setIs24HourView(true)

        // ✅ Request permission if needed
        requestNotificationPermission()

        btnSetReminder.setOnClickListener {
            val calendar = Calendar.getInstance()

            val hour = if (Build.VERSION.SDK_INT >= 23) timePicker.hour else timePicker.currentHour
            val minute = if (Build.VERSION.SDK_INT >= 23) timePicker.minute else timePicker.currentMinute

            calendar.set(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth,
                hour,
                minute,
                0
            )

            // ✅ Don't allow past time
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                Toast.makeText(this, "Please select a future time.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            selectedDateTimeText.text = "Reminder set for: ${calendar.time}"

            val intent = Intent(this, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            Toast.makeText(this, "Vaccination reminder set!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    // Optional: Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denied. Notifications may not work.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
