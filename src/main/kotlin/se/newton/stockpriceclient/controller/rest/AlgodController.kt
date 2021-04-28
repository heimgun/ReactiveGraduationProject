package se.newton.stockpriceclient.controller.rest

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.newton.stockpriceclient.services.AlgodServiceImpl

/**
 * Normal REST controller for testing out the AlgodClient.
 * Useful mostly as reference.
 */
@RestController
@RequestMapping("/algo")
class AlgodController(
	val algod: AlgodServiceImpl
) {
	@GetMapping("/wallet/{address}")
	fun getWalletData(
		@PathVariable("address") walletAddress: String
	) = algod.getAccountInformation(walletAddress)

	@GetMapping("/status")
	fun getStatus() = algod.getStatus()

	@GetMapping("/block/latest")
	fun getLastBlock() = algod.getLatestBlock()

	@GetMapping("/block/next")
	fun getNextBlock() = algod.getNextBlock()

	@GetMapping(
		value = ["/block/number-flux"],
		produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
	fun getBlockNumberFlux() = algod.getBlockNumberFlux()

	@GetMapping(
		value = ["/block/summary-flux"],
		produces = [MediaType.APPLICATION_NDJSON_VALUE])
	fun getTxFlux() = algod.getShortBlockSummaryFlux()
}