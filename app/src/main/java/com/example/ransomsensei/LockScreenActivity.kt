package com.example.ransomsensei

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.ransomsensei.data.RansomSenseiDatabase
import com.example.ransomsensei.data.entity.Card
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class LockScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)

        val db = Room.databaseBuilder(applicationContext,
            RansomSenseiDatabase::class.java, "ransom-sensei").build()

        val cardDao = db.cardDao()

        val unlockButton: Button = findViewById(R.id.unlock_button)
        val response : TextInputEditText = findViewById(R.id.response)

        unlockButton.setOnClickListener {
            if (response.text.contentEquals("Good morning")) {

                val launcher = Intent(Intent.ACTION_MAIN)
                launcher.addCategory(Intent.CATEGORY_LAUNCHER)
                // launcher.setPackage("com.android.settings")

                val packageManager = packageManager
                val resolveInfos: List<ResolveInfo> = packageManager.queryIntentActivities(launcher, PackageManager.MATCH_ALL)


                if (resolveInfos.isNotEmpty()) {
                    for (resolveInfo in resolveInfos) {
                        println(resolveInfo.activityInfo.packageName)
                        // Use the package name as needed
                    }
                } else {
                    // Handle the case where no activity matches the intent
                }
                // val chooser = Intent.createChooser(launcher, null)
                startActivity(launcher)
                finish()
            } else {

            }
        }
    }
}