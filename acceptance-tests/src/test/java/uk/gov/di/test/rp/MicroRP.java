package uk.gov.di.test.rp;

import org.apache.http.client.utils.URIBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.URISyntaxException;

import static org.apache.commons.lang3.RandomStringUtils.random;

public class MicroRP {

    public void start() {
        Spark.stop();
        Spark.awaitStop();

        Spark.port(3031);

        Spark.get("/authorize", this::callback);
        Spark.get("/", this::dispatchOneLoginRequest);
    }

    private String dispatchOneLoginRequest(Request request, Response res)
            throws URISyntaxException {

        var authRequest =
                new URIBuilder(System.getenv("OP_URL"))
                        .setPath("authorize")
                        .addParameter("state", random(20))
                        .addParameter("nonce", random(20))
                        .addParameter("scope", "openid")
                        .addParameter("redirect_uri", "http://localhost:3031/callback")
                        .addParameter("client_id", System.getenv("RP_CLIENT_ID"))
                        .addParameter("response_type", "code")
                        .addParameter("vtr", "[\"Cl\"]")
                        .build();

        res.redirect(authRequest.toString());
        return "";
    }

    private Object callback(Request request, Response response) {
        // Noop
        return "";
    }
}
