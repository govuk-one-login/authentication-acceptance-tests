package uk.gov.di.test.utils;

import org.json.JSONObject;
import org.openqa.selenium.Cookie;

import java.util.Set;

public class OneLoginSession {
    private String sessionId;
    private String clientSessionID;
    private String persistentSessionId;
    private String languageFromCookie;

    public OneLoginSession(Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "gs":
                    String[] split = cookie.getValue().split("\\.");
                    sessionId = split[0];
                    clientSessionID = split[1];
                    break;
                case "di-persistent-session-id":
                    persistentSessionId = cookie.getValue();
                    break;
                case "lng":
                    languageFromCookie = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
    }

    public JSONObject toJSONObject() {
        var language = languageFromCookie == null ? "MISSING" : languageFromCookie.toUpperCase();
        JSONObject json = new JSONObject();
        json.put("sessionId", sessionId);
        json.put("clientSessionId", clientSessionID);
        json.put("persistentSessionId", persistentSessionId);
        json.put("languageFromCookie", language);
        return json;
    }
}
