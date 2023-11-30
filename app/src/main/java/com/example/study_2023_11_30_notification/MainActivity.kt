package com.example.study_2023_11_30_notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.study_2023_11_30_notification.databinding.ActivityMainBinding


import com.gun0912.tedpermission.PermissionBuilder
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import android.Manifest
import android.widget.Toast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.nfc.Tag
import android.os.Build
import android.os.Message
import android.util.Log
import android.util.LogPrinter
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel_ID = "one-channel"
            val channel_Name = "My Channel One"
            val channel = NotificationChannel(
                channel_ID,
                channel_Name,
                NotificationManager.IMPORTANCE_HIGH
            )
            var channel_description = "my channel one description"
            channel.setShowBadge(true)
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, channel_ID)
        } else {
            builder = NotificationCompat.Builder(this)
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            requestPermission {
                todo()
            }
        }
        binding.button2.setOnClickListener {
            builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
            builder.setWhen(System.currentTimeMillis())
            builder.setContentTitle("Content")
            builder.setContentText("Text")
            manager.notify(12, builder.build())
        }



    }


    private fun todo(){
        Toast.makeText(this@MainActivity, "OK", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission(logic : () -> Unit) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    logic()
                    // 이미 권한이 있거나, 권한 요청이 허가됨 -> logic() 실행
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@MainActivity, "권한을 허가해주세요", Toast.LENGTH_SHORT).show()
                    // 권한이 거부됨 -> 위 함수 실행함.
                }
            })
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
            .check()
    }
}