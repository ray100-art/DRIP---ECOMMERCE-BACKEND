package com.drip.store.config;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MpesaConfig {

    // ── Sandbox Credentials ──────────────────────────────────────
    public static final String CONSUMER_KEY    = "GzkGNBHCsxoFBObIBxNqCmfPdDNbLFvN";
    public static final String CONSUMER_SECRET = "oAmalNKoFfBKRWbS";
    public static final String SHORTCODE       = "174379";
    public static final String PASSKEY         = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";

    // ── Sandbox Endpoints ────────────────────────────────────────
    public static final String AUTH_URL     = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    public static final String STK_PUSH_URL = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";

    // ── Callback URL (update with your public URL or ngrok URL) ──
    public static final String CALLBACK_URL = "https://your-ngrok-url.ngrok.io/api/mpesa/callback";

    private final HttpClient  httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper    = new ObjectMapper();

    /**
     * Fetches a fresh OAuth access token from Safaricom.
     * Token expires after 1 hour — for production, cache this with a scheduler.
     */
    public String getAccessToken() throws IOException, InterruptedException {
        String credentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;
        String encoded     = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AUTH_URL))
                .header("Authorization", "Basic " + encoded)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("M-Pesa auth failed: " + response.statusCode() + " — " + response.body());
        }

        JsonNode json = mapper.readTree(response.body());
        return json.get("access_token").asText();
    }
}