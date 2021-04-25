package se.newton.stockpriceclient

import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import utils.getLogger
import java.io.IOException
import java.time.Duration

class WebClientStockClient(private val webClient: WebClient) {
	private val logger = getLogger<WebClientStockClient>()

	fun pricesFor(symbol: String?): Flux<StockPrice> {
		return webClient.get()
			.uri("http://localhost:8080/stocks/{symbol}", symbol)
			.retrieve()
			.bodyToFlux(StockPrice::class.java)
			.retryWhen(
				Retry.backoff(5, Duration.ofSeconds(1))
					.maxBackoff(Duration.ofSeconds(20)))
			.doOnError(IOException::class.java) { logger.error(it.message) }
	}
}