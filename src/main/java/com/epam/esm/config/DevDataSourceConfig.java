package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@Profile("dev")
@Configuration
public class DevDataSourceConfig {

    private static final String TEST_DATA = "test-data.sql";

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                   .setType(EmbeddedDatabaseType.H2)
                   .addScript(TEST_DATA).build();
    }


}
