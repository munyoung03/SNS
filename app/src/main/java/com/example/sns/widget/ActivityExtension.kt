package com.example.sns.widget

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
}

fun AppCompatActivity.toast(text : Int) {
    android.widget.Toast.makeText(applicationContext, text, android.widget.Toast.LENGTH_SHORT).show()
}