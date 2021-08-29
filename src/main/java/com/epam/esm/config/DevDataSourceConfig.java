package com.epam.esm.config;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@Profile("dev")
public class DevDataSourceConfig implements DataSourceConfig{

    @Override
    public DataSource setUp() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("test-data.sql").build();
    }

}
