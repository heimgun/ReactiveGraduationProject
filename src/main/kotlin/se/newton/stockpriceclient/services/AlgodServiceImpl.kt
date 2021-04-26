package se.newton.stockpriceclient.services

import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.Account
import com.algorand.algosdk.v2.client.model.Block
import com.algorand.algosdk.v2.client.model.BlockResponse
import com.algorand.algosdk.v2.client.model.NodeStatusResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import se.newton.stockpriceclient.utils.extractOrFail

@Service
class AlgodServiceImpl(
	val algod: AlgodClient
) : AlgodService {
	override fun getAccountInformation(wallet: String): Mono<Account> {
		val response = algod.AccountInformation(Address(wallet)).execute()
		return Mono.justOrEmpty(response.extractOrFail())
	}

	override fun getStatus(): Mono<NodeStatusResponse> {
		val response = algod.GetStatus().execute()
		return Mono.justOrEmpty(response.extractOrFail())
	}

	override fun getLatestBlock(): Mono<BlockResponse> {
		return getStatus()
			.map { it.lastRound }
			.map { algod.GetBlock(it).execute().extractOrFail() }
	}

	override fun getNextBlock(): Mono<BlockResponse> {
		return getStatus()
			.map { algod.WaitForBlock(it.lastRound + 1).execute().extractOrFail().lastRound }
			.map { algod.GetBlock(it).execute().extractOrFail() }
	}
}