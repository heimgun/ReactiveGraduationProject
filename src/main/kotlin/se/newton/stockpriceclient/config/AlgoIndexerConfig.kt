package se.newton.stockpriceclient.config

import com.algorand.algosdk.v2.client.common.IndexerClient
import com.algorand.algosdk.v2.client.model.HealthCheck
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import se.newton.stockpriceclient.utils.getLogger

@Configuration
class AlgoIndexerConfig(
	@Value("\${algod.indexer.url}") val indexerAddress: String,
	@Value("\${algod.indexer.port}") val indexerPort: Int,
) {
	val logger: Logger = getLogger<AlgoIndexerConfig>()

	@Bean
	fun createIndexerClient(): IndexerClient =
		IndexerClient(indexerAddress, indexerPort).apply {
			val status = makeHealthCheck().execute()
			if (!status.isSuccessful) {
				throw BeanInitializationException("AlgoIndexerClient status check returned ${status.message()}")
			}

			val healthCheckResponse = status.body() as HealthCheck
			logger.info("AlgoIndexer initialized! Health check response: ${healthCheckResponse.message}")
		}
}