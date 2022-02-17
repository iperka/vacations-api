package com.iperka.vacations.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Audit configuration for all models. Required for populating audit
 * related fields.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    /**
     * Creates auditorProvider bean used by Spring.
     * 
     * @since 1.0.0
     * @return AuditorAware bean.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}