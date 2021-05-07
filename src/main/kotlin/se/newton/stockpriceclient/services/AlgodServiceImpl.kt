package se.newton.stockpriceclient.services

import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.Account
import com.algorand.algosdk.v2.client.model.BlockResponse
import com.algorand.algosdk.v2.client.model.NodeStatusResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import se.newton.stockpriceclient.controller.rest.models.ShortBlockSummary
import se.newton.stockpriceclient.utils.extractOrFail

@Service
class AlgodServiceImpl(
	val algod: AlgodClient
) : AlgodService {
	private val lastResponseFlux: ConnectableFlux<NodeStatusResponse> =
		getBlockNumbersStartingNow()
			.map {
				val lol = algod.WaitForBlock(it).execute().extractOrFail()
				println(lol.lastRound)
				return@map lol
			}
			.cache(1)
			.publish()
			.also { println("SUP BRAH") }

	private fun getBlockNumbersStartingNow(): Flux<Long> =
		getStatus()
			.flatMapMany { nodeStatusResponse ->
				val startRound = nodeStatusResponse.lastRound - 1
				val sequence = generateSequence(startRound) { it + 1 }
				return@flatMapMany sequence.toFlux()
			}

	override fun getStatusResponseFlux(): Flux<NodeStatusResponse> =
		lastResponseFlux.autoConnect().cache(1)

	override fun getAccountInformation(wallet: String): Mono<Account> {
		val response = algod.AccountInformation(Address(wallet)).execute()
		return Mono.justOrEmpty(response.extractOrFail())
	}

	override fun getStatus(): Mono<NodeStatusResponse> {
		val response = algod.GetStatus().execute()
		return Mono.justOrEmpty(response.extractOrFail())
	}

	override fun getLatestBlock(): Mono<BlockResponse> =
		getStatus().map { algod.GetBlock(it.lastRound).execute().extractOrFail() }

	override fun getLatestBlockNumber(): Mono<Long> =
		getStatus().map { it.lastRound }

	override fun getNextBlock(): Mono<BlockResponse> =
		getStatus()
			.map {
				val nextBlock = it.lastRound + 1
				val newRound = algod.WaitForBlock(nextBlock).execute().extractOrFail().lastRound
				return@map algod.GetBlock(newRound).execute().extractOrFail()
			}

	override fun getBlockNumberFlux(): Flux<Long> =
		getBlockNumbersStartingNow()
			.map { nextRound ->
				algod.WaitForBlock(nextRound).execute().extractOrFail().lastRound
					.also { println(it) }
			}

	override fun getShortBlockSummaryFlux(): Flux<ShortBlockSummary> {
		return getBlockNumbersStartingNow()
			.map { nextRound ->
				algod.WaitForBlock(nextRound).execute()
				val nextBlock = algod.GetBlock(nextRound).execute().extractOrFail()
				val transactions = (nextBlock.block["txns"] ?: listOf<Any>()) as List<*>
				val netName = (nextBlock.block["gen"] ?: "unknown") as String

				return@map ShortBlockSummary(
					net = netName,
					transactions = transactions.size,
					round = nextRound)
			}
	}
}