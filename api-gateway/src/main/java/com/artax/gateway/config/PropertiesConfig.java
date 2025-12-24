package com.artax.gateway.config;



import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)})
@Getter
public class PropertiesConfig {


    @Value("${application.token-url}")
    private String tokenUrl;

    @Value("${application.client-secret}")
    private String ClientSecret;

    @Value("${application.client-id}")
    private String Clientid;

}
