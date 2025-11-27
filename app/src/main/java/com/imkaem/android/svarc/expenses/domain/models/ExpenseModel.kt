package com.imkaem.android.svarc.expenses.domain.models

import java.time.Instant

/* what makes this a Brief, is that it does not have full category - but only its id */
data class ExpenseModel(
    val id: Long,
    val amount: Long,
    val currency: String,
    val dateTime: Instant,
    val description: String,
    val categoryId: Long,
)
