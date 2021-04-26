package se.newton.stockpriceclient.services

import com.algorand.algosdk.v2.client.model.Account
import com.algorand.algosdk.v2.client.model.BlockResponse
import com.algorand.algosdk.v2.client.model.NodeStatusResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AlgodService {
	fun getAccountInformation(wallet: String): Mono<Account>
	fun getStatus(): Mono<NodeStatusResponse>
	fun getLatestBlock(): Mono<BlockResponse>
	fun getLatestBlockNumber(): Mono<Long>
	fun getNextBlock(): Mono<BlockResponse>
	fun getBlockNumberFlux(): Flux<Long>
}