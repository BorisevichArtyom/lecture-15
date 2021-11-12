package by.itacademy.javaenterprise.spring;

import by.itacademy.javaenterprise.Main;
import by.itacademy.javaenterprise.command.Command;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@ComponentScan("by.itacademy.javaenterprise")
@PropertySource("classpath:database.properties")
public class SpringConfig {
    @Autowired
    private HikariConfig hikariConfig;

    @Bean
    public HikariConfig hikariConfig(@Value("${datasource.jdbcUrl}") String url,
                                     @Value("${dataSource.user}") String userName,
                                     @Value("${dataSource.password}") String password,
                                     @Value("${dataSource.maxPoolSize}") Integer maxPoolSize,
                                     @Value("${dataSource.cachePrepStmts}") String cachePrepStmts,
                                     @Value("${dataSource.prepStmtCacheSize}") String prepStmtCacheSize,
                                     @Value("${dataSource.prepStmtCacheSqlLimit}") String prepStmtCacheSqlLimit) {
        HikariConfig hconfig = new HikariConfig();
        hconfig.setJdbcUrl(url);
        hconfig.setUsername(userName);
        hconfig.setPassword(password);
        hconfig.setMaximumPoolSize(maxPoolSize);
        hconfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        hconfig.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        hconfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        return hconfig;
    }

    @Bean
    public HikariDataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcNamedTemplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate());
    }

    @Bean
    public MapSqlParameterSource mapSqlParameterSource() {
        return new MapSqlParameterSource();
    }

    @Bean
    public Main qualifierExample(@Qualifier("UpdateTraining") Command<Integer> command) {
        return new Main(command);
    }


}
