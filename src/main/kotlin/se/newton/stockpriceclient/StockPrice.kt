package se.newton.stockpriceclient

import java.time.LocalDateTime

// TODO Find some relevant place to put this
data class StockPrice(
	var symbol: String? = null,
	var price: Double? = null,
	var time: LocalDateTime? = null,
)