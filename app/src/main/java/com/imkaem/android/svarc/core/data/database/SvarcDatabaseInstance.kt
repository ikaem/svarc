package com.imkaem.android.svarc.core.data.database

import android.content.Context
import androidx.room.Room
import com.imkaem.android.svarc.SvarcApplication
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao

/* TODO this is temp - will be removed once Hilt is introduced */
object SvarcDatabaseInstance {

    private const val DATABASE_NAME = "svarc_database"

    private val database: SvarcDatabase by lazy {
        Room.databaseBuilder(
            SvarcApplication.getApplicationContext(),
            SvarcDatabase::class.java,
            DATABASE_NAME,
        )
            .addMigrations()
            .build()
    }

    fun expensesDao(): ExpensesDao {
        return database.expensesDao
    }
}