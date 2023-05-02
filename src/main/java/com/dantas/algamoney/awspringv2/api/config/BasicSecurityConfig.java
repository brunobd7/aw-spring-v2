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

//@Configuration
//@EnableWebSecurity
public class BasicSecurityConfig {

    //ENABLE ALL ANNOTATIONS TO USE BASIC HTTP AUTHORIZATION

    /** Using a SecurityFilterChain instead override configure method when extends WebSecurityConfigurerAdapter.
     *  Then we can config ROUTES , MATCHER AND AUTHENTICATION TYPE.
     *
     */
//    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/categories").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .build();
    }

    /** Defining inMemory user to do an AUTHENTICATION IN MEMORY*/
//    @Bean
    public InMemoryUserDetailsManager userDetailsManagerService(){

        UserDetails userDetails = User.withUsername("admin").password("{noop}admin").roles("USER").build();

        return new InMemoryUserDetailsManager(userDetails);
    }


}
