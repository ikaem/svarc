package com.imkaem.android.svarc.core.data.database
//
//import android.content.Context
//import android.util.Log
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.imkaem.android.svarc.SvarcApplication
//import com.imkaem.android.svarc.expenses.data.database.CategoriesDao
//import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
//import com.imkaem.android.svarc.expenses.data.entities.local.CategoryLocalEntity
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//
///* TODO this is temp - will be removed once Hilt is introduced */
//object SvarcDatabaseInstance {
//
//    private const val DATABASE_NAME = "svarc_database"
//
//    /* TODO lets leave here, because of lazy */
//    private val database: SvarcDatabase by lazy {
//        Room.databaseBuilder(
//            SvarcApplication.getApplicationContext(),
//            SvarcDatabase::class.java,
//            DATABASE_NAME,
//        )
//            .addMigrations(
//                migration_1_2
//            )
//            /* TODO testing this */
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//
//                    db.execSQL(
//                        /* raw sqls is executed syncrnously */
//                        "INSERT OR IGNORE INTO categories (name) VALUES ('Default')"
//                    )
//
//                    /* we could do this as well - but it does not trigger */
////                    runBlocking {
////                        val category = CategoryLocalEntity(name = "Default")
////                        val id = database.categoriesDao.add(category)
////                        Log.d("SvarcDatabaseInstance", "Inserted default category with id: $id" )
////                    }
//
//                    /* TODO if this is used, category is inserted asyncronously, so it will not be visible first tiem the app starts when all categories are retrieved */
////                    CoroutineScope(Dispatchers.IO).launch {
////
////                        val category = CategoryLocalEntity(name = "Default")
////
////                        database.categoriesDao.add(
//////                            CategoryLocalEntity("Default")
////                            category
////                        )
////
////
////                        Log.d("SvarcDatabaseInstance", "Inserted default category with id: ${category.id}" )
////                    }
//                }
//            })
//            .build()
//    }
//
//    fun expensesDao(): ExpensesDao {
//        return database.expensesDao
//    }
//
//    fun categoriesDao(): CategoriesDao {
//        return database.categoriesDao
//    }
//}