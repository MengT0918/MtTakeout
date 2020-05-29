package com.mt.takeout.utils

import java.text.NumberFormat

object PriceFormat {
    fun format(countPrice: Float): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2
        return format.format(countPrice.toDouble())
    }
}