package com.imkaem.android.svarc.expenses.data.entities.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "categories",
    indices = [
        /* as per https://code.luasoftware.com/tutorials/android/android-room-add-unique-contraint-to-table*/
        Index(value = ["name"], unique = true)
    ]
)
data class CategoryLocalEntity(
    /* if id is 0, Room will autogenerate it (if int or Long) */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)


/* TODO what do we need in the entity
*
*
*
* */
