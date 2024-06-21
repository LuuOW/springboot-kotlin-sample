package com.cropyback.cropyback.authentication

import com.cropyback.cropyback.authentication.filter.JWTAuthenticationFilter
import com.cropyback.cropyback.authentication.service.JwtTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import IpFilter
import FakeIpFilter
import com.cropyback.cropyback.database.sql.service.UserJpaService


@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "security", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { webSecurity ->
            webSecurity
            webSecurity.ignoring().
            requestMatchers(
                "/api/sms/request",
                "/api/sms/verify",
                "/api/email/request",
                "/api/email/verify",
                "/api/login",
                "/api/signup",
                "/api/password",
                "/api/ui"
            )
        }
    }

    @Autowired
    private lateinit var jwtService: JwtTokenService
    @Autowired
    private lateinit var service: UserJpaService

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/**")
                    .authenticated()
            }
            .addFilterBefore(fakeIPFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(JWTAuthenticationFilter(jwtService, service), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(ipFilterBean(), JWTAuthenticationFilter::class.java)
        //.addFilterBefore(AuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

        @Bean
    fun ipFilterBean(): IpFilter {
        return IpFilter()
    }

    @Bean
    fun fakeIPFilter(): FakeIpFilter {
        return FakeIpFilter()
    }


}
