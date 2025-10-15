package com.imkaem.android.svarc.expenses.utils.values

data class CreateExpenseValue (
    val amount: Long,
    /* TODO we should probably make some kind of enum for currency */
    /* TODO currency should be set globaly or something - as default somewhere in db */
    val currency: String,
    /* TODO we need to calculate this from date milliseconds and */
    val dateTimeMillis: Long,
    val description: String,
    val categoryId: Int,
)