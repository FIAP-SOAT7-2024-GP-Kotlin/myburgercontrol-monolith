package io.github.soat7.myburguercontrol.config

import io.github.oshai.kotlinlogging.KotlinLogging
import liquibase.change.DatabaseChange
import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import javax.sql.DataSource

private val logger = KotlinLogging.logger {}

@Configuration
@ConditionalOnClass(SpringLiquibase::class, DatabaseChange::class)
@ConditionalOnProperty(prefix = "spring.liquibase", name = ["enabled"], matchIfMissing = true)
@AutoConfigureAfter(DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class)
@Import(SchemaInit.SpringLiquibaseDependsOnPostProcessor::class)
class SchemaInit {

    @Component
    class SchemaInitBean @Autowired constructor(
        private val dataSource: DataSource,
        @Value("\${spring.datasource.schema}") private val schemaName: String,
    ) : InitializingBean {
        override fun afterPropertiesSet() {
            dataSource.connection.use { conn ->
                conn.createStatement().use { stmt ->
                    logger.info { "CREATING SCHEMA $schemaName" }
                    stmt.execute("create schema if not exists $schemaName")
                }
            }
        }
    }

    @ConditionalOnBean(SchemaInitBean::class)
    class SpringLiquibaseDependsOnPostProcessor :
        AbstractDependsOnBeanFactoryPostProcessor(SpringLiquibase::class.java, SchemaInitBean::class.java)
}
