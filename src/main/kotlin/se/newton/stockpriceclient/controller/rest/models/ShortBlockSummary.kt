package se.newton.stockpriceclient.controller.rest.models

/**
 * Model somewhat resembling the flowing list of transactions on AlgoExplorer.io
 * Apparently proposer was removed from the v2 API call, and currently we only have a v2 Algod-client.
 * Perhaps if we add a v1 client as well we can use that to add more relevant data.
 */
data class ShortBlockSummary(
	val net: String,
	val round: Long,
	val transactions: Int,
) {
}