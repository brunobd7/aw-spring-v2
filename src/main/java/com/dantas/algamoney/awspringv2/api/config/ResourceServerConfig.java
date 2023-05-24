package com.dantas.algamoney.awspringv2.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {


    /*This filter define wich request destination are authenticaded or allow without authentication,
     * another definitions we can do here are types of token oauth2 uses to authenticate like OPAQUE and JWT Token */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/categories/").permitAll()
                .anyRequest().authenticated()
                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//REMOVE TO USE AuthorizationGrantType CLIENT_CREDENTIALS
//                .and()
                .csrf().disable()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());

        return httpSecurity.build();
    }

     private JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			List<String> userRoleAuthorities = jwt.getClaimAsStringList("authorities");

			if (userRoleAuthorities == null) {
				userRoleAuthorities = Collections.emptyList();
			}

			JwtGrantedAuthoritiesConverter scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> scopeAuthorities = scopesAuthoritiesConverter.convert(jwt);

			scopeAuthorities.addAll(userRoleAuthorities.stream()
					.map(SimpleGrantedAuthority::new)
					.toList());

			return scopeAuthorities;
		});

		return jwtAuthenticationConverter;
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
