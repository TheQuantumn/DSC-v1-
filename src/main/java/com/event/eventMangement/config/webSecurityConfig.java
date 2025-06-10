// Package and imports
package com.event.eventMangement.config;

// This is your custom user service that loads user data from the DB (like usernames and passwords)
import com.event.eventMangement.security.CustomUserDetailService;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.nimbusds.jose.jwk.RSAKey;


@Configuration // This tells Spring that this class has configuration settings (like beans)
public class webSecurityConfig {

    // Injecting (auto-wiring) your custom service that loads users from the database
    @Autowired
    private CustomUserDetailService userDetailService;

    // This method sets up how security should work in your app
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Enables Cross-Origin Resource Sharing (optional, good for frontend-backend on different ports)
                .cors(Customizer.withDefaults())

                // Disables CSRF protection (useful for APIs, but not recommended for web apps with sessions)
                .csrf(csrf -> csrf.disable())

                // All incoming HTTP requests must be authenticated
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login","/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                // Set session policy to STATELESS: no server-side sessions will be created (good for token-based auth)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                // Enables HTTP Basic Auth (sends username/password with every request in the header)
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth->oauth.jwt(Customizer.withDefaults()))

                // Finally, build the filter chain
                .build();
    }

    // This bean creates and provides the AuthenticationManager, which handles the login process
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Get the AuthenticationManager from Spring’s configuration
        return config.getAuthenticationManager();
    }

    // This method sets up how we fetch user details and how we verify passwords

    public AuthenticationProvider authProvider() {
        // DaoAuthenticationProvider uses a custom user details service + password encoder
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // Tell it to use your custom user details service
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        // Set the password encoder (e.g., BCrypt) to check password hashes
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    // This bean encodes passwords using BCrypt (which hashes them safely)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair(){
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    @Bean
    public RSAKey rsaKey(){
        KeyPair keyPair = keyPair();
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwtSource(){
        JWKSet jwkSet = new JWKSet(rsaKey());
        return (((jwkSelector, Context) -> jwkSelector.select(jwkSet) ));
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource){
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return  NimbusJwtDecoder.withPublicKey(rsaKey().toRSAPublicKey()).build();
    }
}
