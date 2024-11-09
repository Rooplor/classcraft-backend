package com.rooplor.classcraftbackend.configuration

import com.rooplor.classcraftbackend.filters.DevJwtRequestFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@Profile("dev")
class DevSecurityConfig {
    @Autowired
    private lateinit var devJwtRequestFilter: DevJwtRequestFilter

    @Throws(Exception::class)
    @Bean
    fun configureDevelopment(http: HttpSecurity): DefaultSecurityFilterChain {
        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeRequests { it.anyRequest().permitAll() }
            .addFilterBefore(devJwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
