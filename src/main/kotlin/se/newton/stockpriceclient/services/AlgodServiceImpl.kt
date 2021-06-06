package se.newton.stockpriceclient.services

import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.Account
import com.algorand.algosdk.v2.client.model.BlockResponse
import com.algorand.algosdk.v2.client.model.NodeStatusResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import se.newton.stockpriceclient.controller.rest.models.ShortBlockSummary
import se.newton.stockpriceclient.utils.extractOrFail

@Service
class AlgodServiceImpl(
	val algod: AlgodClient
) : AlgodService {
	private val lastResponseFlux: Flux<NodeStatusResponse> =
		algod.GetStatus()
			.execute()
			.extractOrFail()
			.let { Mono.justOrEmpty(it) }
			.flatMapMany { nodeStatusResponse ->
				val startRound = nodeStatusResponse.lastRound - 1
				val firstBlock = algod.WaitForBlock(startRound).execute().extractOrFail()
				return@flatMapMany generateSequence(firstBlock) {
					algod.WaitForBlock(it.lastRound).execute().extractOrFail()
				}.toFlux()
			}.cache(1)
			.share()

	private val lastBlockFlux: Flux<BlockResponse> =
		lastResponseFlux.map { algod.GetBlock(it.lastRound).execute().extractOrFail() }

	override fun getStatusResponseFlux(): Flux<NodeStatusResponse> = lastResponseFlux
	override fun getBlockResponseFlux(): Flux<BlockResponse> = lastBlockFlux
	override fun getBlockNumberFlux(): Flux<Long> = lastResponseFlux.map { it.lastRound }

	override fun getAccountInformation(wallet: String): Mono<Account> =
		algod.AccountInformation(Address(wallet))
			.execute()
			.extractOrFail()
			.let { Mono.justOrEmpty(it) }

	final override fun getStatus(): Mono<NodeStatusResponse> =
		algod.GetStatus()
			.execute()
			.extractOrFail()
			.let { Mono.justOrEmpty(it) }

	override fun getLatestBlock(): Mono<BlockResponse> = lastBlockFlux.last()

	override fun getLatestBlockNumber(): Mono<Long> = lastResponseFlux.take(2).last().map { it.lastRound }

	override fun getNextBlock(): Mono<BlockResponse> =
		getStatus()
			.map {
				val nextBlock = it.lastRound + 1
				val newRound = algod.WaitForBlock(nextBlock).execute().extractOrFail().lastRound
				return@map algod.GetBlock(newRound).execute().extractOrFail()
			}

	override fun getShortBlockSummaryFlux(): Flux<ShortBlockSummary> =
		lastBlockFlux.map {
			val transactions = (it.block["txns"] ?: listOf<Any>()) as List<*>
			val netName = (it.block["gen"] ?: "unknown") as String

			return@map ShortBlockSummary(
				net = netName,
				transactions = transactions.size,
				round = it.block["rnd"] as Int)
		}
}