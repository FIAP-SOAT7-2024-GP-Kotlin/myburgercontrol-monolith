import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.liquibase.gradle.LiquibaseTask
import java.io.IOException
import java.util.Properties

// from gradle.properties
val testContainerVersion: String by ext
val javaVersion = JavaVersion.VERSION_21

val props = Properties()
try {
    props.load(file("$projectDir/.env").inputStream())
} catch (e: IOException) {
    println(e.message)
}

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("plugin.allopen") version "1.9.24"
    jacoco

    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.liquibase.gradle") version "2.2.2"
    id("org.barfuin.gradle.jacocolog") version "3.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "io.github.soat7"
version = "0.0.1-SNAPSHOT"

if (!javaVersion.isCompatibleWith(JavaVersion.current())) {
    error(
        """
        =======================================================
        RUN WITH JAVA $javaVersion
        =======================================================
        """.trimIndent(),
    )
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.testcontainers:testcontainers-bom:$testContainerVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.github.oshai:kotlin-logging-jvm:6.+")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.postgresql:postgresql:42.7.+")
    implementation("org.liquibase:liquibase-core:4.+")
    implementation("com.google.guava:guava:33.2.1-jre")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.+")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.ninja-squad:springmockk:4.+")
    testImplementation("io.mockk:mockk:1.+")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:mockserver")
    testImplementation("org.mock-server:mockserver-client-java:5.13.2")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.awaitility:awaitility-kotlin:4.+")
    testImplementation("org.springframework.security:spring-security-test")

    // Liquibase
    liquibaseRuntime("info.picocli:picocli:4.+")
    liquibaseRuntime("org.liquibase:liquibase-core:4.+")
    liquibaseRuntime("org.postgresql:postgresql:42.7.+")
    liquibaseRuntime("org.liquibase.ext:liquibase-hibernate6:4.+")
    liquibaseRuntime("org.springframework.boot:spring-boot-starter-data-jpa")
    liquibaseRuntime(sourceSets.main.get().runtimeClasspath)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = javaVersion.toString()
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
//    forkEvery = 0
    environment.putAll(
        props.entries.associate { it.key.toString() to it.value.toString() },
    )
    testLogging {
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco/test/html")
    }
    dependsOn(tasks.test)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = 0.85.toBigDecimal()
            }
        }
    }
}

tasks.registering(JavaCompile::class) {
    sourceCompatibility = javaVersion.toString()
    targetCompatibility = javaVersion.toString()
    options.encoding = "UTF-8"
    options.compilerArgs = listOf("-Xlint:unchecked", "-Xlint:deprecation")
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "changelogFile" to "/config/liquibase/master.xml",
            "classpath" to sourceSets.main.get().output.resourcesDir?.absolutePath,
            "url" to "jdbc:${props["DATABASE_URL"]}",
            "username" to props["DATABASE_USER"],
            "password" to props["DATABASE_PASSWORD"],
        )
    }
    activities.register("rollback") {
        this.arguments = mapOf(
            "changelogFile" to "/config/liquibase/master.xml",
            "classpath" to sourceSets.main.get().output.resourcesDir?.absolutePath,
            "url" to "jdbc:${props["DATABASE_URL"]}",
            "username" to props["DATABASE_USER"],
            "password" to props["DATABASE_PASSWORD"],
            "count" to 1,
        )
    }
    activities.register("diffLog") {
        this.arguments = mapOf(
            "changelogFile" to "${layout.buildDirectory.get()}/tmp/diff-changelog.xml",
            "classpath" to sourceSets.main.get().output.resourcesDir?.absolutePath,
            "url" to "jdbc:${props["DATABASE_URL"]}",
            "username" to props["DATABASE_USER"],
            "password" to props["DATABASE_PASSWORD"],
            "referenceUrl" to "hibernate:spring:io.github.soat7.myburguercontrol.infrastructure.persistence?" +
                "dialect=org.hibernate.dialect.PostgreSQLDialect&" +
                "hibernate.physical_naming_strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy&" +
                "hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
            "defaultSchemaName" to "myburguer",
            // "logLevel" to "debug",
        )
    }
}

ktlint {
    this.coloredOutput.set(true)
    this.outputToConsole.set(true)
}

tasks.named<LiquibaseTask>("update") {
    doFirst {
        liquibase.setProperty("runList", "rollback")
    }
}

tasks.named<LiquibaseTask>("updateSql") {
    doFirst {
        liquibase.setProperty("runList", "rollback")
    }
}

tasks.named<LiquibaseTask>("rollbackCount") {
    doFirst {
        liquibase.setProperty("runList", "rollback")
    }
}

tasks.named<LiquibaseTask>("rollbackCountSql") {
    doFirst {
        liquibase.setProperty("runList", "rollback")
    }
}

tasks.named<LiquibaseTask>("diffChangelog") {
    doFirst {
        liquibase.setProperty("runList", "diffLog")
    }
}
