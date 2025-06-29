package chloe.movietalk

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class MovietalkApplication

fun main(args: Array<String>) {
    runApplication<MovietalkApplication>(*args)
}
