package com.example.loracafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/assets/**", "/Vista/**", "/img/**").permitAll()

                .requestMatchers("/", "/inicio", "/sobrenosotros", "/carta", "/login", "/registro").permitAll()
                
                .requestMatchers("/api/client/products/**", "/api/client/promotions/**").permitAll()

                .requestMatchers("/dashboard/**", "/api/dashboard/**").hasRole("ADMIN")
                
                .requestMatchers(
                        "/carrito",
                        "/mi-cuenta/**",
                        "/api/client/orders/**",
                        "/api/client/account/**",
                        "/api/client/messages"
                ).authenticated()
                
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/inicio", true) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/acceso-denegado")
            );

        return http.build();
    }
}