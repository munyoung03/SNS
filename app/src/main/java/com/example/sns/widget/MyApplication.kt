package com.example.sns.widget

import android.app.Application
import com.example.sns.di.MyModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(MyModule)
        }
    }
}
