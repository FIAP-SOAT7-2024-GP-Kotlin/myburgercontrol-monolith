import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.liquibase.gradle.LiquibaseTask
import java.io.IOException
import java.util.Properties

val props = Properties()
try {
    props.load(file("$projectDir/.env").inputStream())
} catch (e: IOException) {
    println(e.message)
}

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("plugin.allopen") version "1.9.23"
    jacoco

    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.liquibase.gradle") version "2.2.1"
    id("org.barfuin.gradle.jacocolog") version "3.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

group = "io.github.soat7"
version = "0.0.1-SNAPSHOT"

if (JavaVersion.current() != JavaVersion.VERSION_21) {
    error(
        """
        =======================================================
        RUN WITH JAVA 21
        =======================================================
        """.trimIndent()
    )
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
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
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.github.microutils:kotlin-logging-jvm:3.+")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.postgresql:postgresql:42.7.+")
    implementation("org.liquibase:liquibase-core:4.+")
    implementation("com.google.guava:guava:33.1.0-jre")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.+")

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
    testImplementation("org.testcontainers:postgresql:1.19.+")
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
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
//    forkEvery = 0
    environment.putAll(
        props.entries.associate { it.key.toString() to it.value.toString() }
    )
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
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
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
            "password" to props["DATABASE_PASSWORD"]
        )
    }
    activities.register("rollback") {
        this.arguments = mapOf(
            "changelogFile" to "/config/liquibase/master.xml",
            "classpath" to sourceSets.main.get().output.resourcesDir?.absolutePath,
            "url" to "jdbc:${props["DATABASE_URL"]}",
            "username" to props["DATABASE_USER"],
            "password" to props["DATABASE_PASSWORD"],
            "count" to 1
        )
    }
    activities.register("diffLog") {
        this.arguments = mapOf(
            "changelogFile" to "${layout.buildDirectory.get()}/tmp/diff-changelog.xml",
            "classpath" to sourceSets.main.get().output.resourcesDir?.absolutePath,
            "url" to "jdbc:${props["DATABASE_URL"]}",
            "username" to props["DATABASE_USER"],
            "password" to props["DATABASE_PASSWORD"],
            "referenceUrl" to "hibernate:spring:io.github.soat7.myburguercontrol.entities?" +
                "dialect=org.hibernate.dialect.PostgreSQLDialect&" +
                "hibernate.physical_naming_strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy&" +
                "hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
            "defaultSchemaName" to "myburguer"
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
