package se.newton.stockpriceclient.services

import com.algorand.algosdk.v2.client.model.Transaction
import reactor.core.publisher.Flux
import java.util.*

interface AlgoIndexerService {
	fun getWalletTransactionHistory(
		address: String,
		limit: Long? = null,
		afterTime: Date? = null,
		beforeTime: Date? = null
	): Flux<Transaction>
}