package com.imkaem.android.svarc

import android.app.Application
import android.content.Context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SvarcApplication : Application() {

    init {
        app = this
    }

//    override fun onCreate() {
//        super.onCreate()
//
////        CoroutineScope(Dispatchers.IO).launch {
////            /* todo THIS IS CLAUDE TALK TO INSERT STUFF ON APP START, OR IGNORE
////            *
////            * https://claude.ai/chat/1e49de60-b7f4-432d-b5a1-0d707831efce*/
////            CATEGORIES_DAO
////        }
//
//    }

    companion object {
        /* TODO this is just temp - will introduce hilt */

        private lateinit var app: SvarcApplication

        fun getApplicationContext(): Context = app.applicationContext
    }
}