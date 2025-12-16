package com.imkaem.android.svarc.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.imkaem.android.svarc.expenses.data.database.CategoriesDao
import com.imkaem.android.svarc.expenses.data.database.ExpensesDao
import com.imkaem.android.svarc.expenses.data.entities.local.CategoryLocalEntity
import com.imkaem.android.svarc.expenses.data.entities.local.DailyBudgetEntity
import com.imkaem.android.svarc.expenses.data.entities.local.ExpenseLocalEntity

@Database(
    entities = [
        ExpenseLocalEntity::class,
        CategoryLocalEntity::class,
        DailyBudgetEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class SvarcDatabase : RoomDatabase() {
    abstract val expensesDao: ExpensesDao
    abstract val categoriesDao: CategoriesDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE daily_budgets (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "amount INTEGER NOT NULL, " +
                    "year INTEGER NOT NULL, " +
                    "month INTEGER NOT NULL, " +
                    "UNIQUE(year, month)" +
                    ");"
        )
    }
}

/* THIS MIGRATION IS NOT NECESSARY RIGHT NOW */
//val migration_1_2 = object : Migration(1, 2) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        /* as per https://stackoverflow.com/questions/15497985/how-to-add-unique-constraint-to-existing-table-in-sqlite*/
//        db.execSQL(
//            "CREATE UNIQUE INDEX index_category_name ON categories(name)"
//
//        )
//    }
//}