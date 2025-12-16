package com.imkaem.android.svarc.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.imkaem.android.svarc.core.data.database.SvarcDatabase
import com.imkaem.android.svarc.core.data.database.migration_1_2
import com.imkaem.android.svarc.expenses.data.database.CategoriesDao
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DiDatabaseModule {

    @Provides
    fun provideExpensesDao(database: SvarcDatabase): ExpensesDao {
        return database.expensesDao
    }

    @Provides
    fun provideCategoriesDao(database: SvarcDatabase): CategoriesDao {
        return database.categoriesDao
    }


    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ): SvarcDatabase {


        val database = Room.databaseBuilder(
            appContext,
            SvarcDatabase::class.java,
            "svarc_database",
        )
            /* we dont want to drop, for now at least */
//            .fallbackToDestructiveMigration(
//                dropAllTables = true,
//            )
            .addMigrations(
                /* no migrations as of yet */
                migration_1_2,
            )
            /* TODO testing this */
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    db.execSQL(
                        /* raw sqls is executed syncrnously */
                        "INSERT OR IGNORE INTO categories (name) VALUES ('Default')"
                    )

                    /* we could do this as well - but it does not trigger */
//                    runBlocking {
//                        val category = CategoryLocalEntity(name = "Default")
//                        val id = database.categoriesDao.add(category)
//                        Log.d("SvarcDatabaseInstance", "Inserted default category with id: $id" )
//                    }

                    /* TODO if this is used, category is inserted asyncronously, so it will not be visible first tiem the app starts when all categories are retrieved */
//                    CoroutineScope(Dispatchers.IO).launch {
//
//                        val category = CategoryLocalEntity(name = "Default")
//
//                        database.categoriesDao.add(
////                            CategoryLocalEntity("Default")
//                            category
//                        )
//
//
//                        Log.d("SvarcDatabaseInstance", "Inserted default category with id: ${category.id}" )
//                    }
                }
            })
            .build()


        return database;
    }
}