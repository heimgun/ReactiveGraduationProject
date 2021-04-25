package se.newton.stockpriceclient.utils

import org.apache.logging.log4j.LogManager

inline fun <reified T> getLogger() = LogManager.getLogger(T::class.java)