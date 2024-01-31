//package com.promantus.hireprous.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
//
//
//@Configuration
//public class OAuth2Config {
//
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(
//            // Define your OAuth 2.0 client registration here
//            ClientRegistration.withRegistrationId("your-registration-id")
//                .clientId("76e6940e-36db-4538-8afe-fb459ac2ebf3")
//                .clientSecret("61a3086f-066d-48f5-b6e3-f5e6d5934906")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
//                .scope("scope1", "scope2")
//                .authorizationUri("your-authorization-uri")
//                .tokenUri("your-token-uri")
//                .userInfoUri("your-user-info-uri")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .clientName("Your Client Name")
//                .build()
//        );
//    }
//}
