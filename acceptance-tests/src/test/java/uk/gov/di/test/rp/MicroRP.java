package uk.gov.di.test.rp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.utils.URIBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.net.http.HttpRequest.newBuilder;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static uk.gov.di.test.rp.SigningKey.signingKey;
import static uk.gov.di.test.utils.Unchecked.unchecked;

public class MicroRP {

    public enum JourneyType {
        LOW_CONFIDENCE("[\"Cl\"]"),
        MED_CONFIDENCE("[\"Cl.Cm\"]");

        private final String vectorOfTrust;

        JourneyType(String vectorOfTrust) {
            this.vectorOfTrust = vectorOfTrust;
        }

        public String vectorOfTrust() {
            return vectorOfTrust;
        }
    }

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    private final String clientId;
    private final String baseUrl;
    private final String privateKey;

    public MicroRP(String clientId, String baseUrl, String privateKey) {
        this.clientId = clientId;
        this.baseUrl = baseUrl;
        this.privateKey = privateKey;
    }

    public MicroRP start() {
        Spark.stop();
        Spark.awaitStop();

        Spark.port(3031);

        Spark.get("/callback", this::callback);

        return this;
    }

    public String startJourneyUrl(JourneyType journeyType) {
        var authRequest =
                unchecked(() -> new URIBuilder(this.baseUrl))
                        .setPath("authorize")
                        .addParameter("state", randomAlphanumeric(20))
                        .addParameter("nonce", randomAlphanumeric(20))
                        .addParameter("scope", "openid")
                        .addParameter("redirect_uri", "http://localhost:3031/callback")
                        .addParameter("client_id", this.clientId)
                        .addParameter("response_type", "code")
                        .addParameter("vtr", journeyType.vectorOfTrust());

        return unchecked(authRequest::build).toString();
    }

    private String callback(Request request, Response response) throws Exception {
        var tokenResponse = makeTokenRequest(request.queryParams("code"));
        var userInfoResponse = makeUserInfoRequest(tokenResponse.get("access_token"));
        return gson.toJson(userInfoResponse);
    }

    private Map<String, String> makeTokenRequest(String authCode) throws Exception {
        var tokenUri = new URIBuilder(this.baseUrl).setPath("token").build();

        var tokenRequestJwt =
                JWT.create()
                        .withAudience(tokenUri.toString())
                        .withIssuer(this.clientId)
                        .withSubject(this.clientId)
                        .withExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                        .withIssuedAt(Instant.now())
                        .withJWTId(randomAlphanumeric(20))
                        .sign(Algorithm.RSA256(signingKey(this.privateKey)));

        var body =
                ofEntries(
                                entry("code", authCode),
                                entry("redirect_uri", "http://localhost:3031/callback"),
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
                        newBuilder(tokenUri).POST(ofString(body)).build(),
                        HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(tokenResponse.body(), new TypeToken<>() {});
    }

    private String makeUserInfoRequest(String accessToken) throws Exception {

        var userInfoResponse =
                httpClient.send(
                        newBuilder(new URIBuilder(this.baseUrl).setPath("userinfo").build())
                                .GET()
                                .header("Authorization", "Bearer " + accessToken)
                                .build(),
                        HttpResponse.BodyHandlers.ofString());

        return userInfoResponse.body();
    }
}
