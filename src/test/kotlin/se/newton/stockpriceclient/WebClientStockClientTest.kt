package se.newton.stockpriceclient

import org.springframework.web.reactive.function.client.WebClient
import kotlin.test.assertEquals
import kotlin.test.Test

class WebClientStockClientTest {
  private val webClient = WebClient.builder().build()

  @Test
  fun `retrieve stock prices from the service`() {
		// Arrange
    val webClientStockClient = WebClientStockClient(webClient)

		// Act
    val prices = webClientStockClient.pricesFor("SYMBOL")
    val fivePrices = prices.take(5)

		// Assert
    assertEquals(5, fivePrices.count().block())
		assertEquals("SYMBOL", fivePrices.blockFirst()?.symbol)
  }
}