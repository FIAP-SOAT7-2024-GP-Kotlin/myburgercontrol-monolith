package io.github.soat7.myburguercontrol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(scanBasePackages = ["io.github.soat7"])
@ComponentScan("io.github.soat7")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
