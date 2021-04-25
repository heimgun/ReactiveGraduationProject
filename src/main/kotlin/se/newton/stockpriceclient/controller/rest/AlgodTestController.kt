package se.newton.stockpriceclient.controller.rest

import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.Account
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Normal REST controller for testing out the AlgodClient.
 * Useful mostly as reference.
 */
@RestController
@RequestMapping("/algod-test")
class AlgodTestController(
	val algod: AlgodClient
) {
	@GetMapping("/wallet/{address}")
	fun getWalletData(
		@PathVariable("address") walletAddress: String
	) : Account {
		val response = algod.AccountInformation(Address(walletAddress)).execute()
		if (!response.isSuccessful) {
			throw Exception("Lookup failed!")
		}
		return response.body() as Account
	}
}