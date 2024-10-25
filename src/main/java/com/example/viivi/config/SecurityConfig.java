package com.example.viivi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/users/register", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()  // Allow public access to these paths
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Restrict admin endpoints
                        .requestMatchers("/users/preferences").authenticated()  // Ensure authenticated users can access preferences
                        .anyRequest().authenticated()  // Authenticate other requests
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .usernameParameter("email")  // Custom username parameter
                        .passwordParameter("password")  // Custom password parameter
                        .permitAll()
                        .defaultSuccessUrl("/", true)  // Redirect after login
                        .successHandler((request, response, authentication) -> {
                            if (authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/users/login?logout")  // Redirect to login page on logout
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/activity/save-activity")  // Disable CSRF for activity logging endpoint
                )
                .anonymous(Customizer.withDefaults());  // Allow anonymous access for unauthenticated users

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
