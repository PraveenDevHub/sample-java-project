package com.happiest.APIGatewayJWT2.service;

import com.happiest.APIGatewayJWT2.constants.JWTConstants;
import com.happiest.APIGatewayJWT2.exception.TokenException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import java.util.logging.Logger;

@Service
@Component
public class JWTService {

    private static final Logger logger = Logger.getLogger(JWTService.class.getName());

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String username) {
        try {
            JWSSigner signer = new MACSigner(jwtSecret.getBytes());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + JWTConstants.TOKEN_EXPIRATION_TIME_MS))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            String token = signedJWT.serialize(); // This line remains unchanged
            return token;
        } catch (JOSEException e) {
            throw new TokenException(JWTConstants.TOKEN_GENERATION_ERROR, e);
        }
    }

    public String extractUserName(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

    private <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimResolver) {
        final JWTClaimsSet claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public JWTClaimsSet extractAllClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.verify(new MACVerifier(jwtSecret.getBytes()))) {
                return signedJWT.getJWTClaimsSet();
            } else {
                throw new TokenException(JWTConstants.INVALID_TOKEN_SIGNATURE);
            }
        } catch (ParseException | JOSEException e) {
            throw new TokenException(JWTConstants.TOKEN_EXTRACTION_ERROR, e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, JWTClaimsSet::getExpirationTime);
    }
}
