package com.unionsystems.ums.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class CorsConfig extends CorsFilter {

    CorsConfig() {
        super(configurationSource());
    }

    private static UrlBasedCorsConfigurationSource configurationSource() {
        AtomicReference<CorsConfiguration> config = new AtomicReference<>(new CorsConfiguration());
        config.get().addAllowedOrigin("*");
        config.get().addAllowedHeader("*");
        config.get().setMaxAge(36000L);
        config.get().setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config.get());
        return source;
    }
}
