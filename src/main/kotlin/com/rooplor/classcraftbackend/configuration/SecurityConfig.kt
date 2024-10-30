package com.rooplor.classcraftbackend.configuration

import com.rooplor.classcraftbackend.filters.DevJwtRequestFilter
import com.rooplor.classcraftbackend.filters.JwtRequestFilter
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {
    @Autowired
    private lateinit var jwtRequestFilter: JwtRequestFilter

    @Autowired
    private lateinit var devJwtRequestFilter: DevJwtRequestFilter

    @Throws(Exception::class)
    @Bean
    @Profile("prod")
    fun configure(http: HttpSecurity): DefaultSecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                    ).permitAll()
                it.anyRequest().authenticated()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Throws(Exception::class)
    @Bean
    @Profile("dev")
    fun configureDevelopment(http: HttpSecurity): DefaultSecurityFilterChain {
        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeRequests { it.anyRequest().permitAll() }
            .addFilterBefore(devJwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
