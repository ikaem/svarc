package com.imkaem.android.svarc.costs.utils.values

import androidx.compose.ui.text.font.FontWeight

/* TODO maybe this should be a model, not sure */
data class GraphRowValue(
    val label: String,
    val value: Int,
    val currency: String,
    val valueRowWeight: Float,
)
