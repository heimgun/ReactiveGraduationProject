package utils

import org.apache.logging.log4j.LogManager
import se.newton.stockpriceclient.WebClientStockClient

inline fun <reified T> getLogger() = LogManager.getLogger(T::class.java)