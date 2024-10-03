package com.example.viivi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/", "/home", "/users/register", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                                .loginPage("/users/login")
                                .usernameParameter("email")  // Set "email" as the username parameter
                                .passwordParameter("password")  // Set "password" as the password parameter
                                .permitAll()
                                .defaultSuccessUrl("/", true) // Redirect to home by default after login
                                .successHandler((request, response, authentication) -> {
                                    // Custom success handler: redirect based on role
                                    if (authentication.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                        response.sendRedirect("/admin/dashboard");
                                    } else {
                                        response.sendRedirect("/");
                                    }
                                })
                )
                .logout((logout) -> logout
                                .logoutUrl("/logout") // Custom logout URL
                                .logoutSuccessUrl("/users/login?logout") // Redirect to login page after logout
                                .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity, but only do this if you're sure it's safe

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt for password hashing
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
