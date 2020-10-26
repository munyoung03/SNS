package com.example.sns.widget.extension

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun Fragment.startActivity(moveActivity: Class<*>) {
    startActivity(
        Intent(
            context!!.applicationContext,
            moveActivity
        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    )
    activity!!.finish()
}

fun Fragment.noFinishStartActivity(moveActivity: Class<*>) {
    startActivity(
        Intent(
            context!!.applicationContext,
            moveActivity
        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    )
}

fun Fragment.toast(message: String?) {
    Toast.makeText(context!!.applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
    var ft: FragmentTransaction = fragmentManager.beginTransaction()
    ft.detach(fragment).attach(fragment).commit()
}