package com.imkaem.android.svarc

import android.app.Application
import android.content.Context

class SvarcApplication : Application() {

    init {
        app = this
    }

    companion object {
        /* TODO this is just temp - will introduce hilt */

        private lateinit var app: SvarcApplication

        fun getApplicationContext(): Context = app.applicationContext
    }
}