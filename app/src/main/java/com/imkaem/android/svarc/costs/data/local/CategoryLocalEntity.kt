package com.imkaem.android.svarc.costs.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class CategoryLocalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
)


/* TODO what do we need in the entity
*
*
*
* */
