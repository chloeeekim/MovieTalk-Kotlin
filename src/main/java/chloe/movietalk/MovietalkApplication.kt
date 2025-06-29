package chloe.movietalk

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
object MovietalkApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(MovietalkApplication::class.java, *args)
    }
}
