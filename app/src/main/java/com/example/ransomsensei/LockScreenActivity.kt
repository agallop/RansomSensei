package com.example.ransomsensei

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LockScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_screen)

        val unlockButton: Button = findViewById(R.id.unlock_button)
        val response : TextInputEditText = findViewById(R.id.response)

        unlockButton.setOnClickListener {
            if (response.text.contentEquals("Good morning")) {

                val launcher = Intent(Intent.ACTION_MAIN)
                launcher.addCategory(Intent.CATEGORY_HOME)

                val chooser = Intent.createChooser(launcher, null)
                startActivity(chooser)
                finish()
            }
        }
    }
}