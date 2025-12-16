package com.imkaem.android.svarc.expenses.domain.models

data class DailyBudgetModel(
    val id: Long,
    val amount: Long,
    val year: Int,
    val month: Int,
)
