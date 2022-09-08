package com.project.encrypt.configuration;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PortConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
     
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
    	final String port = System.getenv("PORT");
    	int portInt = 8080;
    	if (StringUtils.hasText(port)) {
    		portInt = Integer.valueOf(port);
    	}
        factory.setPort(portInt);
    }
}
