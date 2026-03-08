package com.drip.store.service;

import com.drip.store.config.MpesaConfig;
import com.drip.store.dto.StkPushRequest;
import com.drip.store.dto.StkPushResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class MpesaService {

    private final MpesaConfig  config     = new MpesaConfig();
    private final HttpClient   httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper     = new ObjectMapper();

    /**
     * Initiates an STK Push (Lipa Na M-Pesa Online).
     *
     * @param phone  Kenyan phone number — accepts formats: 07xx, 01xx, +2547xx, 2547xx
     * @param amount Amount in KES (must be >= 1)
     * @param orderId Your internal order reference shown to customer
     */
    public StkPushResponse initiateStkPush(String phone, int amount, String orderId)
            throws IOException, InterruptedException {

        String token     = config.getAccessToken();
        String timestamp = generateTimestamp();
        String password  = generatePassword(timestamp);
        String safePhone = formatPhone(phone);

        // Build STK Push payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("BusinessShortCode", MpesaConfig.SHORTCODE);
        payload.put("Password",          password);
        payload.put("Timestamp",         timestamp);
        payload.put("TransactionType",   "CustomerPayBillOnline");
        payload.put("Amount",            amount);
        payload.put("PartyA",            safePhone);
        payload.put("PartyB",            MpesaConfig.SHORTCODE);
        payload.put("PhoneNumber",       safePhone);
        payload.put("CallBackURL",       MpesaConfig.CALLBACK_URL);
        payload.put("AccountReference",  "DRIP-" + orderId);
        payload.put("TransactionDesc",   "DRIP Fashion Order " + orderId);

        String body = mapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MpesaConfig.STK_PUSH_URL))
                .header("Authorization",  "Bearer " + token)
                .header("Content-Type",   "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        StkPushResponse result = mapper.readValue(response.body(), StkPushResponse.class);
        result.setRawResponse(response.body());
        return result;
    }

    /**
     * Generates the Lipa Na M-Pesa password.
     * Formula: Base64(ShortCode + Passkey + Timestamp)
     */
    private String generatePassword(String timestamp) {
        String raw = MpesaConfig.SHORTCODE + MpesaConfig.PASSKEY + timestamp;
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    /**
     * Generates timestamp in yyyyMMddHHmmss format required by Safaricom.
     */
    private String generateTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * Normalises any Kenyan phone format to 2547XXXXXXXX.
     * Handles: 0712345678 → 254712345678
     *          +254712345678 → 254712345678
     *          254712345678 → 254712345678 (unchanged)
     */
    public String formatPhone(String phone) {
        // Strip spaces, dashes, brackets
        String cleaned = phone.replaceAll("[\\s\\-()]+", "");

        if (cleaned.startsWith("+")) {
            cleaned = cleaned.substring(1);
        }
        if (cleaned.startsWith("0")) {
            cleaned = "254" + cleaned.substring(1);
        }
        if (!cleaned.startsWith("254")) {
            cleaned = "254" + cleaned;
        }
        return cleaned;
    }
}