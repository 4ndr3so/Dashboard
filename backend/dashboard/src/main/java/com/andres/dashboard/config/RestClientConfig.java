package com.andres.dashboard.config;

// RestClientConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient ckanRestClient() {
        var f = new DefaultUriBuilderFactory("https://ckan0.cf.opendata.inter.prod-toronto.ca");
        f.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return RestClient.builder().uriBuilderFactory(f).build();
    }
}