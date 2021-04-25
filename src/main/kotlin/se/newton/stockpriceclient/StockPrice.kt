package se.newton.stockpriceclient

import java.time.LocalDateTime

data class StockPrice(
	var symbol: String? = null,
	var price: Double? = null,
	var time: LocalDateTime? = null,
)