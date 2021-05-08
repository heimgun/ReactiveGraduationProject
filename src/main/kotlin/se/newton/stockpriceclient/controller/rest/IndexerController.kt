package se.newton.stockpriceclient.controller.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import se.newton.stockpriceclient.services.AlgoIndexerService

/**
 * Normal REST controller for testing out the AlgodClient.
 * Useful mostly as reference.
 */
@RestController
@RequestMapping("/algoidx")
class IndexerController(
	val indexerService: AlgoIndexerService
) {
	@GetMapping("/wallet/{address}")
	fun getWalletData(
		@PathVariable("address") walletAddress: String
	) = indexerService.getWalletTransactionHistory(walletAddress)
}