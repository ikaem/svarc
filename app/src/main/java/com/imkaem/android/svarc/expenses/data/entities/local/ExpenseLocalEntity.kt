package com.imkaem.android.svarc.expenses.data.entities.local

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
    val id: Long = 0,
    val amount: Long,
    val currency: String,
    @ColumnInfo(name = "date_time_millis")
    val dateTimeMillis: Long,
    val description: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
) {

    /* NOTE: as per this https://stackoverflow.com/a/64518658/9661910 */
//    @PrimaryKey(autoGenerate = true)
//    var id: Long? = null
}


/* ok, what do we need in the expense entity
* - id
* - amount
* - description
* - datetime in millis
* - category id i guess
* - currency
*
* */