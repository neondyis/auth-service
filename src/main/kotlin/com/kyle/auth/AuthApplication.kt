package com.kyle.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}
