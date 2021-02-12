package com.example.person

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonProjectApplication

fun main(args: Array<String>) {
	runApplication<PersonProjectApplication>(*args)
}
