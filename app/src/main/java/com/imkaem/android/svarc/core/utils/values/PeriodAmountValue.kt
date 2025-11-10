package com.imkaem.android.svarc.core.utils.values

import java.time.Instant

/* NOTE: this is used in UI actually, to render some amounts */
data class PeriodAmountValue (
    /* this is both inclusive */
    val startDate: Instant,
    val endDate: Instant,
    val amount: Long,
)