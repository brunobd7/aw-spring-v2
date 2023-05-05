package com.dantas.algamoney.awspringv2.api.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.UUID;

@Configuration
//@EnableWebSecurity
//@Import(OAuth2AuthorizationServerConfiguration.class) //SET TOKEN INTROSPECTION ENDPOINTS AND DEFAULT CONFIGURATION
public class AuthorizationServer {

    /**
     * Annotation @Order define priority of this filter, we must config this order because many times spring run
     * mutiples Security Filters.
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain defaultFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        return httpSecurity.formLogin(Customizer.withDefaults()).build();// NOW LOGIN FORM IS HANDLE BY OUR AUTHORIZATION SERVER
    }

    /**
     * One more Security Filter to assume other role as authorization security filter.
     * This filter define wich request destination are authenticaded or allow without authentication,
     * another definitions we can do here are types of token oauth2 uses to authenticate like OPAQUE and JWT Token  */
    @Bean
    public SecurityFilterChain authFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/categories").permitAll() // ALLOW A RESOURCE TO BE ACCESSED WITHOU AUTHENTICATION
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return httpSecurity.formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("angular")//ANGULAR APP
                .clientSecret("{noop}".concat("@ngul@r0"))//ANGULAR APP SECRET
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .scope("read")
                .scope("write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30)).build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * BEAN TO SET ISSUER OF TOKEN , OTHERWISE WHO ARE CREATOR/EMITTER OF THIS TOKEN
     * issuer - Our Oauth2 authorization server.
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8080")
                .build();
    }


    /**
     * Customizing JWT , we should pass user and pass data. Authorities and roles too
     */
 /*   @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtBuildCustomizer() {
        return context -> {
            context.getClaims().claim("nome", "userName");
            context.getClaims().claim("authorities", new HashSet<String>());// ROLES / AUTHORITIES
        };
    }
*/

    /**
     * BEAN GENERATE KEYS TO DO A SINGNATURE OF TOKEN
     * DISCLAIMER -> THE DEPRECATED WAY WIHT ALGORITM ENCODER H256 DON'T WORK, MINIMUM SECURITY KEY ARE RSA KEYS
     * <p></p>
     * WE SHOULD GENERATE AND LOAD A SPECIFIC AND REAL KEYS AT PRODUCTION ENVIRONMENT
     */
    @Bean
    public JWKSet jwkSet() throws JOSEException {
        RSAKey rsa = new RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .keyID(UUID.randomUUID().toString())
                .generate();

        return new JWKSet(rsa);
    }

    /**
     * BEAN TO READ JWKSET BEAN AND COMUNICATE TO SPRING WICH SIGNATURE KEY WE ARE USING
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /**
     * ENCODER FOR JWT TOKEN
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * DECODER FOR JWT TOKEN
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

}
