package com.ibm.restconf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration("WebSecurityConfiguration")
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean("securityConfigSecurityFilterChain")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(
                (requests) -> requests.requestMatchers("/**").hasRole("USER")
                .requestMatchers("/management/**").hasRole("USER")
                .anyRequest().denyAll())
                .httpBasic(Customizer.withDefaults())
                .build();

    }

    @Bean("securityConfigWebSecurityCustomizer")
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/**", "/management/health", "/management/info");
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("user_with_no_roles").password("password").roles("NONE").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("locked_user").password("password").roles("USER").accountLocked(true).build());
        return manager;
    }
}