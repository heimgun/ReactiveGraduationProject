package se.newton.stockpriceclient.controller.rest.models

import com.algorand.algosdk.v2.client.model.BlockResponse

/**
 * Model resembling the flowing list of transactions on AlgoExplorer.io
 */
data class SimpleTransactionBlock(
	val proposer: String,
	val round: Long,
	val transactions: Int,
) {
}