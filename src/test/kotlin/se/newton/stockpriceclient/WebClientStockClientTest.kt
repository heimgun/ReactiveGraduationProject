package se.newton.stockpriceclient

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient

internal class WebClientStockClientTest {
  private val webClient = WebClient.builder().build()
  @Test
  fun retrieveStockPricesFromTheService() {
    val webClientStockClient = WebClientStockClient(webClient)
    val prices = webClientStockClient.pricesFor("SYMBOL")
    Assertions.assertNotNull(prices)
    val fivePrices = prices.take(5)
    Assertions.assertEquals(5, fivePrices.count().block())
    Assertions.assertEquals("SYMBOL", fivePrices.blockFirst().symbol)
  }
}