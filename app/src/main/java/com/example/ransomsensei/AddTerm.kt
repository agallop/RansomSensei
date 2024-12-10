package com.example.ransomsensei

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddTerm : AppCompatActivity() {

    val editText = findViewById(R.id.editText) as EditText
    val editText2 = findViewById(R.id.editText2) as EditText
    val editText3 = findViewById(R.id.editText3) as EditText
    val radioButton2 = findViewById(R.id.radioButton2) as RadioButton
    val radioButton3 = findViewById(R.id.radioButton3) as RadioButton
    val radioButton4 = findViewById(R.id.radioButton4) as RadioButton
    val button = findViewById(R.id.button) as Button
    val button2 = findViewById(R.id.button2) as Button

    val kanji = editText.text.toString();
    val kana = editText2.text.toString();
    val english = editText3.text.toString();
    val difficulty = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_term)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}