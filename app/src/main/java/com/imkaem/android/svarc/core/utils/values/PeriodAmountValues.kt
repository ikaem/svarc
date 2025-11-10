package com.imkaem.android.svarc.core.utils.values

/*
* TODO
* these clases hold values to be rendered in UI for different periods
* not sure
* */
sealed class PeriodAmountValues()


data class TodayPeriodAmountValues(
    val spent: PeriodAmountValue,
    val remainder: PeriodAmountValue,
    val accumulatedRemainder: PeriodAmountValue,
) : PeriodAmountValues()


data class ThisMonthPeriodAmountValues(
    val spent: PeriodAmountValue,
    val budget: PeriodAmountValue,
    val remainder: PeriodAmountValue,
): PeriodAmountValues()