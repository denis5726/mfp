package ru.mfp.common.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import ru.mfp.common.config.filter.JwtTokenFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration {

    @Bean
    fun getSecurityFilterChain(
        httpSecurity: HttpSecurity,
        securityConfigurer: SecurityConfigurer
    ): SecurityFilterChain {
        httpSecurity
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.POST, "/auth/signUp", "auth/signIn").permitAll()
                    .requestMatchers("/stub/**").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf {
                it
                    .disable()
            }
            .sessionManagement {
                it
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .apply<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>>(securityConfigurer)
        return httpSecurity.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(12)

    @Bean
    @ConfigurationProperties
    fun tokenProvider(properties: JwtProperties) = TokenProvider(properties)

    @Bean
    fun jwtTokenFilter(tokenProvider: TokenProvider) = JwtTokenFilter(tokenProvider)

    @Bean
    fun securityConfigurer(jwtTokenFilter: JwtTokenFilter) = SecurityConfigurer(jwtTokenFilter)
}
