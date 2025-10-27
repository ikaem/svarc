package com.imkaem.android.svarc.expenses.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imkaem.android.svarc.expenses.data.entities.local.CategoryLocalEntity

@Dao
interface CategoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(category: CategoryLocalEntity): Long

    @Query("SELECT * FROM categories ORDER BY name ASC")
    suspend fun getAll(): List<CategoryLocalEntity>
}

/* this seems to be some solution
*
* @Database(entities = [YourEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun yourDao(): YourDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insert default data
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.yourDao()?.insert(
                                    YourEntity(id = 1, /* other fields */)
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
*
*
* */