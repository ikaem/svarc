package com.imkaem.android.svarc.expenses.data.entities.local

import androidx.room.Embedded
import androidx.room.Relation

data class ExpenseWithCategoryPojo(
    @Embedded val expense: ExpenseLocalEntity,
    @Relation(
        /* this is the column in expense - the main class */
        parentColumn = "category_id",
        /* this is the column in category - the joined class */
        entityColumn = "id"
    )
    val category: CategoryLocalEntity
)