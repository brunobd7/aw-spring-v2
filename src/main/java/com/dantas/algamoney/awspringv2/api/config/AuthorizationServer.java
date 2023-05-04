package com.dantas.algamoney.awspringv2.api.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.apache.logging.log4j.spi.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.time.Duration;
import java.util.HashSet;
import java.util.UUID;

@Configuration
@Import(OAuth2AuthorizationServerConfiguration.class) //SET TOKEN INTROSPECTION ENDPOINTS AND DEFAULT CONFIGURATION
public class AuthorizationServer {

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    //TODO AFTER WE WILL CREATE A CLASS TO MANAGE ALL PROPERTY OF API
    //	@Autowired
    //	private AlgamoneyAPIProperty algamoneyAPIProperty;

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)//ANGULAR APP
                .clientSecret("{noop}".concat(clientSecret))//ANGULAR APP SECRET
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)//PASSWORD FLOW
                .scope("read")
                .scope("write")
                .redirectUris(uri -> uri.add("http://localhost:4200/authorized"))//FRONTEND PAGE TO REDIRECT AFTER AUTHENTICATION SUCCESSFULL
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30)).build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     *  HOW EXIST N SECURITY FILTERS WE MUST DEFINE HIGHEST PRIORITY OF OUR OAUTH SERVER FILTER
     *  Annotation @Order define priority of this filter.
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain oauthServerFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        return httpSecurity.formLogin(Customizer.withDefaults()).build();// NOW LOGIN FORM IS HANDLE BY OUR AUTHORIZATION SERVER
    }


    /**
     * Customizing JWT , we should pass user and pass data. Authorities and roles too
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtBuildCustomizer() {
        return context -> {
            context.getClaims().claim("nome", "userName");
            context.getClaims().claim("authorities", new HashSet<String>());// ROLES / AUTHORITIES
        };
    }


    /**
     * BEAN GENERATE KEYS TO DO A SINGNATURE OF TOKEN
     * DISCLAIMER -> THE DEPRECATED WAY WIHT ALGORITM ENCODER H256 DON'T WORK, MINIMUM SECURITY KEY ARE RSA KEYS
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

    /**
     * BEAN TO SET ISSUER OF TOKEN , OTHERWISE WHO ARE CREATOR/EMITTER OF THIS TOKEN
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8080/authServerUrl")
                .build();
    }

}
