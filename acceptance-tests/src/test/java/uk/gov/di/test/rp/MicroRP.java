package uk.gov.di.test.rp;

import org.apache.http.client.utils.URIBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.URISyntaxException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static uk.gov.di.test.rp.Config.getPort;
import static uk.gov.di.test.rp.RequestGenerator.makeTokenRequest;
import static uk.gov.di.test.rp.RequestGenerator.makeUserInfoRequest;
import static uk.gov.di.test.rp.ValidateToken.isValidIdToken;

public class MicroRP {

    public void start() {
        stop();
        Spark.port(getPort());
        Spark.get("/callback", this::callback);
        Spark.get("/", this::dispatchOneLoginRequest);
        Spark.init();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String dispatchOneLoginRequest(Request request, Response res)
            throws URISyntaxException {

        var authRequest =
                new URIBuilder(System.getenv("OP_URL"))
                        .setPath("authorize")
                        .addParameter("state", randomAlphanumeric(20))
                        .addParameter("nonce", randomAlphanumeric(20))
                        .addParameter("scope", "openid")
                        .addParameter("redirect_uri", "http://localhost:" + getPort() + "/callback")
                        .addParameter("client_id", System.getenv("RP_CLIENT_ID"))
                        .addParameter("response_type", "code")
                        .addParameter("vtr", "[\"Cl\"]")
                        .build();
        System.out.println(authRequest.toString());
        res.redirect(authRequest.toString());
        return "";
    }

    private Object callback(Request request, Response response) throws Exception {
        var tokenResponse = makeTokenRequest(request.queryParams("code"));

        if (isValidIdToken(tokenResponse.get("id_token"))) {
            var userInfoResponse = makeUserInfoRequest(tokenResponse.get("access_token"));
            return responsePage(userInfoResponse.get("sub"));
        } else {
            throw new RuntimeException("Invalid ID token signature");
        }
    }

    private static String responsePage(String sub) {
        return """
        <html>
            <head>
                <title>Micro RP Landing Page</title>
            <body>
                <span>Subject Identifier: %s</span>

                <a name="logout" href="%s">Logout</a>
            </body>
        </html>
        """
                .formatted(sub, System.getenv("LOGOUT_URL"));
    }
}
