package com.nullpointercats.sys.adopta.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

/**
 * CorsConfig is a configuration class that enables and customizes
 * Cross-Origin Resource Sharing (CORS) for the application.
 *
 * This configuration allows the frontend application (running on a different origin)
 * to communicate with the backend by specifying permitted origins, HTTP methods,
 * and headers.
 * */
@Configuration
class CorsConfig (private val authInterceptor: AuthInterceptor) : WebMvcConfigurer {

    /**
     * Configures CORS mappings for all endpoints in the application.
     *
     * Allows requests from the specified origin and enables common HTTP methods
     * such as GET, POST, PUT, and DELETE. All headers are permitted.
     * */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            /** Allowed frontend origin */
            .allowedOrigins("http://localhost:5173")
            /** Allowed HTTP methods */
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            /** Allowed request headers */
            .allowedHeaders("*")
    }

    /**
     * Registers interceptors for the application.
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/users/login", "/users/register")
    }
}