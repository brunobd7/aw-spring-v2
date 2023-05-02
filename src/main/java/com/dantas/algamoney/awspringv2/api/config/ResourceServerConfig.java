package com.dantas.algamoney.awspringv2.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/categories/").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .oauth2ResourceServer().opaqueToken();

        return httpSecurity.build();
    }


    /**
     * Defining inMemory user to do an AUTHENTICATION IN MEMORY
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsManagerService() {

        UserDetails userDetails = User.withUsername("admin").password("{noop}admin").roles("USER").build();

        return new InMemoryUserDetailsManager(userDetails);
    }


}
