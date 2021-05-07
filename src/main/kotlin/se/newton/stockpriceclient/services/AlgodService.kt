package se.newton.stockpriceclient.services

import com.algorand.algosdk.v2.client.model.Account
import com.algorand.algosdk.v2.client.model.BlockResponse
import com.algorand.algosdk.v2.client.model.NodeStatusResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import se.newton.stockpriceclient.controller.rest.models.ShortBlockSummary

interface AlgodService {
	fun getStatusResponseFlux(): Flux<NodeStatusResponse>
	fun getAccountInformation(wallet: String): Mono<Account>
	fun getStatus(): Mono<NodeStatusResponse>
	fun getLatestBlock(): Mono<BlockResponse>
	fun getLatestBlockNumber(): Mono<Long>
	fun getNextBlock(): Mono<BlockResponse>
	fun getBlockNumberFlux(): Flux<Long>
	fun getShortBlockSummaryFlux(): Flux<ShortBlockSummary>
	fun getBlockResponseFlux(): Flux<BlockResponse>
}