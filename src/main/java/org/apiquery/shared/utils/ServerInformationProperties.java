package org.apiquery.shared.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:env.properties", "classpath:env.${spring.profiles.active}.properties" })
public class ServerInformationProperties {
    @Value("${host.baseUrl}")
    private String webBaseUrl;

    public String getWebBaseUrl() {
        return this.webBaseUrl;
    }

    public void setWebBaseUrl(String webBaseUrl) {
        this.webBaseUrl = webBaseUrl;
    }

}