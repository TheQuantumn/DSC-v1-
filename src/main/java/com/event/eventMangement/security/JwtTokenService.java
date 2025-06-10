package com.event.eventMangement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        var roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 30)) // 30 minutes
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        return this.jwtEncoder
                .encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
