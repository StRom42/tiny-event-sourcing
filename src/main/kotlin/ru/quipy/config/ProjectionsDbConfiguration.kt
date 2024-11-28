package ru.quipy.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.impl.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
class ProjectionsDbConfiguration {

//    @Bean
//    fun connectionProvider(dataSource: DataSource): DataSourceConnectionProvider? {
//        return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))
//    }
    @Bean
    @ConfigurationProperties(prefix = "spring.main-datasource.hikari")
    fun mainHikariConfig(): HikariConfig? {
        return HikariConfig()
    }

    @Bean
    @Primary
    fun mainHikariDataSource(): DataSource? {
        return HikariDataSource(mainHikariConfig())
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.projections-data-source.hikari")
    fun projectionsHikariConfig(): HikariConfig? {
        return HikariConfig()
    }

    @Bean("projectionsHikariDataSource")
    fun projectionsHikariDataSource(): DataSource? {
        return HikariDataSource(projectionsHikariConfig())
    }

    @Bean
    fun dsl(@Qualifier("projectionsHikariDataSource") projectionsHikariDataSource: DataSource): DefaultDSLContext? {
        return DefaultDSLContext(projectionsHikariDataSource, org.jooq.SQLDialect.POSTGRES);
    }

//    @Bean
//    fun configuration(connectionProvider: DataSourceConnectionProvider): DefaultConfiguration? {
//        val jooqConfiguration = DefaultConfiguration()
//        jooqConfiguration.set(connectionProvider)
//        jooqConfiguration.set(org.jooq.SQLDialect.POSTGRES)
//        return jooqConfiguration
//    }
}