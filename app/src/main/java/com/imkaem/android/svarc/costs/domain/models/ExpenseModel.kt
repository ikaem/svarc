package com.imkaem.android.svarc.costs.domain.models

import java.time.Instant

data class ExpenseModel(
    val id: Int,
    val amount: Long,
    val currency: String,
    val dateTime: Instant,
    val description: String,
    val category: CategoryModel,
) {

    val date: String
        get() {
            return "I am a temp date until we resolve from Instant"
        }

    val time: String
        get() {
            return "I am a temp time until we resolve from Instant"
        }
}


