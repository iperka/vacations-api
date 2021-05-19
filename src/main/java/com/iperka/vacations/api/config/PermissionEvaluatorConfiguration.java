package com.iperka.vacations.api.config;

import com.iperka.vacations.api.security.PermissionEvaluator;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * The default {@link SecurityExpressionHandler} delegates
 * <code>hasPermission</code> invocations to a unique
 * {@link PermissionEvaluator} bean, if configured. If no
 * {@link PermissionEvaluator} bean is provided, it will fallback to the
 * {@link DenyAllPermissionEvaluator} to prevent method invocations. <br/>
 * You can only provide a single {@link PermissionEvaluator} bean, so if you
 * want to support several target types, you’ll have to handle that within a
 * single instance. <br/>
 * the <code>@PreAuthorize</code> annotations need to be activated through
 * <code>@EnableGlobalMethodSecurity(prePostEnabled = true)</code>, which is
 * done in {@link PermissionEvaluatorConfiguration}.
 * 
 * @author Michael Beutler
 * @version 1.0.1
 * @since 2021-05-19
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PermissionEvaluatorConfiguration {
    // Annotations only
}