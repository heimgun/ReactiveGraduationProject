package se.newton.stockpriceclient.config

import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.NodeStatusResponse
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import se.newton.stockpriceclient.utils.getLogger

@Configuration
class AlgodConfig(
	@Value("\${algod.url}") val algodAddress: String,
	@Value("\${algod.port}") val algodPort: Int,
	@Value("\${algod.token}") val algodApiToken: String,
) {
	val logger: Logger = getLogger<AlgodConfig>()

	@Bean
	fun createAlgodClient(): AlgodClient =
		AlgodClient(algodAddress, algodPort, algodApiToken).apply {
			val status = GetStatus().execute()
			if (!status.isSuccessful) {
				throw BeanInitializationException("AlgodClient status check returned ${status.message()}")
			}

			val statusResponse = status.body() as NodeStatusResponse
			logger.info("AlgodClient initialized! " +
				"Last round: ${statusResponse.lastRound} " +
				"(${statusResponse.timeSinceLastRound / 1_000_000_000.0} seconds ago)")
		}
}