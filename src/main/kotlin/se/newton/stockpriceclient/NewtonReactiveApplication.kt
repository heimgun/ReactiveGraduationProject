package se.newton.stockpriceclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewtonReactiveApplication

fun main(args: Array<String>) {
	runApplication<NewtonReactiveApplication>(*args)
}