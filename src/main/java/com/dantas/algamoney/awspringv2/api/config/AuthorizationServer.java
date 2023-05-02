package com.dantas.algamoney.awspringv2.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.time.Duration;
import java.util.UUID;

@Configuration
@Import(OAuth2AuthorizationServerConfiguration.class) //SET TOKEN INTROSPECTION ENDPOINTS AND DEFAULT CONFIGURATION
public class AuthorizationServer {

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId(clientId)//ANGULAR APP
				.clientSecret(clientSecret)//ANGULAR APP SECRET
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)//PASSWORD FLOW
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.scope("read")
				.scope("write")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30)).build())
				.build();

		return new InMemoryRegisteredClientRepository(registeredClient);
	}


}
