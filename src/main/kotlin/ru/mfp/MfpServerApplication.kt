package ru.mfp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
class MfpServerApplication

fun main(args: Array<String>) {
    runApplication<MfpServerApplication>(*args)
}
