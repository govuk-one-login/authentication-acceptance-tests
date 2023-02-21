package uk.gov.di.test.rp;

import org.apache.http.client.utils.URIBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static uk.gov.di.test.utils.Unchecked.unchecked;

public class MicroRP {

    public void start() {
        Spark.stop();
        Spark.awaitStop();

        Spark.port(3031);

        Spark.get("/callback", this::callback);
    }

    public static String startJourneyUrl() {

        var authRequest =
                unchecked(() -> new URIBuilder(System.getenv("OP_URL")))
                        .setPath("authorize")
                        .addParameter("state", random(20))
                        .addParameter("nonce", random(20))
                        .addParameter("scope", "openid")
                        .addParameter("redirect_uri", "http://localhost:3031/callback")
                        .addParameter("client_id", System.getenv("RP_CLIENT_ID"))
                        .addParameter("response_type", "code")
                        .addParameter("vtr", "[\"Cl\"]");

        return unchecked(authRequest::build).toString();
    }

    private Object callback(Request request, Response response) {
        // Noop
        return "";
    }
}
