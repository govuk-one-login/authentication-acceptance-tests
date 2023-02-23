package uk.gov.di.test.rp;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.security.interfaces.RSAPublicKey;

import static com.auth0.jwt.algorithms.Algorithm.RSA256;

public class ValidateToken {

    public static boolean isValidIdToken(String idToken) throws JwkException {
        var tokenJwt = JWT.decode(idToken);

        var jwks = new JwkProviderBuilder(System.getenv("OP_URL")).build();

        var idTokenSigningKey = jwks.get(tokenJwt.getKeyId()).getPublicKey();

        var verifier =
                JWT.require(RSA256((RSAPublicKey) idTokenSigningKey))
                        .withAudience(System.getenv("RP_CLIENT_ID"))
                        .withIssuer(System.getenv("OP_URL"))
                        .acceptExpiresAt(10)
                        .acceptIssuedAt(10)
                        .build();

        try {
            verifier.verify(tokenJwt);
            return true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
