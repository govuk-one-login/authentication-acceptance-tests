package uk.gov.di.test.rp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.utils.URIBuilder;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static uk.gov.di.test.rp.Config.getPort;
import static uk.gov.di.test.rp.SigningKey.signingKey;

public class RequestGenerator {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static Map<String, String> makeTokenRequest(String authCode) throws Exception {
        var tokenRequestJwt =
                JWT.create()
                        .withAudience(System.getenv("OP_URL") + "token")
                        .withIssuer(System.getenv("RP_CLIENT_ID"))
                        .withSubject(System.getenv("RP_CLIENT_ID"))
                        .withExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                        .withIssuedAt(Instant.now())
                        .withJWTId(randomAlphanumeric(20))
                        .sign(Algorithm.RSA256(signingKey()));

        var tokenUri = new URIBuilder(System.getenv("OP_URL")).setPath("token").build();

        var body =
                ofEntries(
                                entry("code", authCode),
                                entry(
                                        "redirect_uri",
                                        "http://localhost:" + getPort() + "/callback"),
                                entry(
                                        "client_assertion_type",
                                        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"),
                                entry("client_assertion", tokenRequestJwt),
                                entry("grant_type", "authorization_code"))
                        .entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining("&"));

        var tokenResponse =
                httpClient.send(
                        HttpRequest.newBuilder(tokenUri)
                                .POST(HttpRequest.BodyPublishers.ofString(body))
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(tokenResponse.body(), new TypeToken<>() {});
    }

    public static Map<String, String> makeUserInfoRequest(String accessToken) throws Exception {

        var userInfoResponse =
                httpClient.send(
                        HttpRequest.newBuilder(
                                        new URIBuilder(System.getenv("OP_URL"))
                                                .setPath("userinfo")
                                                .build())
                                .GET()
                                .header("Authorization", "Bearer " + accessToken)
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(userInfoResponse.body(), new TypeToken<>() {});
    }
}
