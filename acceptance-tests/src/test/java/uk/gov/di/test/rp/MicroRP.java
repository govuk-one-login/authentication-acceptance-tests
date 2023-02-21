package uk.gov.di.test.rp;

import org.apache.http.client.utils.URIBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.URISyntaxException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static uk.gov.di.test.rp.RequestGenerator.makeTokenRequest;
import static uk.gov.di.test.rp.RequestGenerator.makeUserInfoRequest;

public class MicroRP {

    public void start() {
        Spark.stop();
        Spark.awaitStop();

        Spark.port(3031);

        Spark.get("/callback", this::callback);
        Spark.get("/", this::dispatchOneLoginRequest);
    }

    private String dispatchOneLoginRequest(Request request, Response res)
            throws URISyntaxException {

        var authRequest =
                new URIBuilder(System.getenv("OP_URL"))
                        .setPath("authorize")
                        .addParameter("state", randomAlphanumeric(20))
                        .addParameter("nonce", randomAlphanumeric(20))
                        .addParameter("scope", "openid")
                        .addParameter("redirect_uri", "http://localhost:3031/callback")
                        .addParameter("client_id", System.getenv("RP_CLIENT_ID"))
                        .addParameter("response_type", "code")
                        .addParameter("vtr", "[\"Cl\"]")
                        .build();

        res.redirect(authRequest.toString());
        return "";
    }

    private Object callback(Request request, Response response) throws Exception {
        var tokenResponse = makeTokenRequest(request.queryParams("code"));
        var userInfoResponse = makeUserInfoRequest(tokenResponse.get("access_token"));

        return responsePage(userInfoResponse.get("sub"));
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
