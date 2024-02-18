package ru.mfp.starter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class], scanBasePackages = ["ru.mfp"])
@EnableJpaRepositories("ru.mfp")
@EntityScan("ru.mfp")
class MfpServerApplication

fun main(args: Array<String>) {
    runApplication<MfpServerApplication>(*args)
}
