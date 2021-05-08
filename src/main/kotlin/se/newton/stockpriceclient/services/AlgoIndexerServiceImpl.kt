package se.newton.stockpriceclient.services

import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.v2.client.common.IndexerClient
import com.algorand.algosdk.v2.client.model.Transaction
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import se.newton.stockpriceclient.utils.extractOrFail
import java.util.*

@Service
class AlgoIndexerServiceImpl(
	val indexer: IndexerClient
) : AlgoIndexerService {
	override fun getWalletTransactionHistory(
		address: String,
		limit: Long?,
		afterTime: Date?,
		beforeTime: Date?,
	): Flux<Transaction> {
		val lookup = indexer.lookupAccountTransactions(Address(address))
		limit?.let { lookup.limit(it) }
		afterTime?.let { lookup.afterTime(it) }
		beforeTime?.let { lookup.beforeTime(it) }

		return lookup.execute()
			.extractOrFail()
			.transactions
			.toFlux()
	}
}