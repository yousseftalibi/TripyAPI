package com.isep.trippy.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DbConfig {
    @Value("${DATABASE_URL}")
    @Bean
    public JdbcTemplate jdbcTemplate(){

        //String url = "jdbc:jdbc:thin:@localhost:49161:xe";

        String url = "postgres://admin:RqrWKnc3gCJkTOnhO4PAk9Rt6BqQjdXV@dpg-cglesuu4dad69r7upa9g-a.frankfurt-postgres.render.com/tripy";
        String user = "system";
        String pass = "oracle";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);

        return new JdbcTemplate(dataSource);
    }
}
