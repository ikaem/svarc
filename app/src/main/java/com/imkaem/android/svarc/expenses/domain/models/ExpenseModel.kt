package com.imkaem.android.svarc.expenses.domain.models

import com.imkaem.android.svarc.core.utils.helpers.DateHelpers
import java.time.Instant

data class ExpenseModel(
    val id: Long,
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

    val formattedDateTime: String
        get() {
            /* TODO i think we have some date utils*/
//            return "Temp until resolve from Instant"

            return DateHelpers.instantToLocalDateFormattedString(dateTime)
        }
}


