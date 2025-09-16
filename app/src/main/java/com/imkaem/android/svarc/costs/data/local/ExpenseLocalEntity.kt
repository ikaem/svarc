package com.imkaem.android.svarc.costs.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = CategoryLocalEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE,
            /* NOTE not really sure what onUpdate CASCADE will do here */
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class ExpenseLocalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Long,
    val currency: String,
    @ColumnInfo(name = "date_time_millis")
    val dateTimeMillis: Long,
    val description: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
)


/* ok, what do we need in the expense entity
* - id
* - amount
* - description
* - datetime in millis
* - category id i guess
* - currency
*
* */