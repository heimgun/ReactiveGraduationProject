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
import se.newton.stockpriceclient.controller.rest.models.SimpleTransactionBlock
import se.newton.stockpriceclient.utils.extractOrFail
import java.time.Duration

@Service
class AlgodServiceImpl(
	val algod: AlgodClient
) : AlgodService {
	private fun getBlockNumbersStartingNow(): Flux<Long> =
		getStatus()
			.flatMapMany { nodeStatusResponse ->
				val startRound = nodeStatusResponse.lastRound - 1
				val sequence = generateSequence(startRound) { it + 1 }
				return@flatMapMany sequence.toFlux()
			}

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

	override fun getSimpleTransactionFlux(): Flux<SimpleTransactionBlock> {
		return getBlockNumbersStartingNow()
			.map { nextRound ->
				algod.WaitForBlock(nextRound).execute()
				val nextBlock = algod.GetBlock(nextRound).execute().extractOrFail().block
				val transactions = (nextBlock["txns"] ?: listOf<Any>()) as List<*>

				return@map SimpleTransactionBlock(
					proposer = "Kalle Anka", // TODO find out how to extract proposer, seems to be under cert => prop somewhere
					transactions = transactions.size,
					round = nextRound
				)
			}
	}
}