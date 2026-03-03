package com.nullpointercats.sys.adopta

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AdoptaApplication

fun main(args: Array<String>) {
	dotenv().entries().forEach {
		System.setProperty(it.key, it.value)
	}
	runApplication<AdoptaApplication>(*args)
}
