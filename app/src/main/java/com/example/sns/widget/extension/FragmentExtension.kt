package com.example.sns.widget.extension

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.startActivity(activity: Class<*>) {
    startActivity(Intent(context!!.applicationContext, activity).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
}

fun Fragment.toast(message: String?) {
    Toast.makeText(context!!.applicationContext, message, Toast.LENGTH_SHORT).show()
}