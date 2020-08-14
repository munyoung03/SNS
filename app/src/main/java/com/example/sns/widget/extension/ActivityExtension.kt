package com.example.sns.widget.extension

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.lightStatusBar() {
    val view = window.decorView
    view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

fun AppCompatActivity.startActivity(activity: Class<*>) {
    val intent = Intent(this, activity)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
    finish()
}

fun AppCompatActivity.toast(text : String) {
    android.widget.Toast.makeText(applicationContext, text, android.widget.Toast.LENGTH_SHORT).show()
}