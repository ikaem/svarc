package com.imkaem.android.svarc

import android.app.Application

class SvarcApplication : Application() {

    init {
        app = this
    }

    companion object {
        /* TODO this is just temp - will introduce hilt */

        private lateinit var app: SvarcApplication

        fun getApplicationContext() = app.applicationContext
    }
}