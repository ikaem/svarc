package com.imkaem.android.svarc.expenses.data.entities.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_budgets",
    indices = [
        Index(value = ["year", "month"], unique = true)
    ]
)
data class DailyBudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /* cents, or whatever smallest currency denomination */
    val amount: Long,
    val year: Int,
    val month: Int,
    /* i guess this is all we need */
)