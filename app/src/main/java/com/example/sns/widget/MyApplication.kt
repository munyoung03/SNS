package com.example.sns.widget

import android.app.Application
import com.example.sns.di.MyModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    companion object { lateinit var prefs: PreferenceUtil }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(MyModule)
        }
    }
}
