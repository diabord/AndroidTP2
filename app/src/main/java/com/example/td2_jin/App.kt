package com.example.td2_jin

import android.app.Application
import com.example.td2_jin.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}