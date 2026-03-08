package com.drip.store.controller;

import com.drip.store.dto.StkPushRequest;
import com.drip.store.dto.StkPushResponse;
import com.drip.store.model.Order;
import com.drip.store.repository.OrderRepository;
import com.drip.store.service.MpesaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mpesa")
public class MpesaController {

    @Autowired
    private MpesaService mpesaService;

    @Autowired
    private OrderRepository orderRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    // ─────────────────────────────────────────────────────────────
    // POST /api/mpesa/stkpush
    // Called by frontend when customer clicks "Pay with M-Pesa"
    // ─────────────────────────────────────────────────────────────
    @PostMapping("/stkpush")
    public ResponseEntity<Map<String, Object>> initiateStk(@RequestBody StkPushRequest req) {
        Map<String, Object> result = new HashMap<>();

        // Basic validation
        if (req.getPhone() == null || req.getPhone().isBlank()) {
            result.put("success", false);
            result.put("message", "Phone number is required");
            return ResponseEntity.badRequest().body(result);
        }
        if (req.getAmount() < 1) {
            result.put("success", false);
            result.put("message", "Amount must be at least KES 1");
            return ResponseEntity.badRequest().body(result);
        }

        try {
            StkPushResponse stkResponse = mpesaService.initiateStkPush(
                    req.getPhone(),
                    req.getAmount(),
                    req.getOrderId() != null ? req.getOrderId() : "ORDER"
            );

            if (stkResponse.isSuccess()) {
                result.put("success",           true);
                result.put("message",           stkResponse.getCustomerMessage());
                result.put("checkoutRequestId", stkResponse.getCheckoutRequestId());
                result.put("merchantRequestId", stkResponse.getMerchantRequestId());
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", stkResponse.getResponseDescription() != null
                        ? stkResponse.getResponseDescription()
                        : "STK Push failed — please try again");
                result.put("raw", stkResponse.getRawResponse());
                return ResponseEntity.status(400).body(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "M-Pesa service error: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // POST /api/mpesa/callback
    // Safaricom calls this URL after customer enters PIN.
    // Must be publicly reachable (use ngrok in dev).
    // ─────────────────────────────────────────────────────────────
    @PostMapping("/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(@RequestBody String rawBody) {
        Map<String, Object> ack = new HashMap<>();

        try {
            System.out.println("=== M-PESA CALLBACK RECEIVED ===");
            System.out.println(rawBody);

            JsonNode root    = mapper.readTree(rawBody);
            JsonNode stkCb   = root.path("Body").path("stkCallback");
            int    resultCode = stkCb.path("ResultCode").asInt(-1);
            String checkoutId = stkCb.path("CheckoutRequestID").asText("");
            String merchantId = stkCb.path("MerchantRequestID").asText("");

            if (resultCode == 0) {
                // ── Payment SUCCESSFUL ──
                JsonNode items = stkCb.path("CallbackMetadata").path("Item");

                String mpesaReceiptNumber = "";
                String transactionDate    = "";
                String phoneNumber        = "";
                double amount             = 0;

                if (items.isArray()) {
                    for (JsonNode item : items) {
                        String name = item.path("Name").asText();
                        switch (name) {
                            case "Amount"            -> amount             = item.path("Value").asDouble();
                            case "MpesaReceiptNumber"-> mpesaReceiptNumber = item.path("Value").asText();
                            case "TransactionDate"   -> transactionDate    = item.path("Value").asText();
                            case "PhoneNumber"       -> phoneNumber        = item.path("Value").asText();
                        }
                    }
                }

                System.out.println("✅ Payment SUCCESS — Receipt: " + mpesaReceiptNumber
                        + " | Amount: KES " + amount
                        + " | Phone: " + phoneNumber);

                // Update order status to 'confirmed' if we can match by orderId
                // The AccountReference was set to "DRIP-{orderId}" when pushing
                String accountRef = stkCb.path("CallbackMetadata").path("AccountReference").asText("");
                if (accountRef.startsWith("DRIP-")) {
                    String orderIdStr = accountRef.replace("DRIP-", "").trim();
                    try {
                        Long orderId = Long.parseLong(orderIdStr);
                        Optional<Order> orderOpt = orderRepository.findById(orderId);
                        if (orderOpt.isPresent()) {
                            Order order = orderOpt.get();
                            order.setStatus("confirmed");
                            orderRepository.save(order);
                            System.out.println("✅ Order #" + orderId + " status updated to confirmed");
                        }
                    } catch (NumberFormatException ignored) {}
                }

            } else {
                // ── Payment FAILED or CANCELLED ──
                String resultDesc = stkCb.path("ResultDesc").asText("Unknown error");
                System.out.println("❌ Payment FAILED — Code: " + resultCode + " | " + resultDesc);
            }

            // Always acknowledge Safaricom with 200 + specific body
            ack.put("ResultCode",        0);
            ack.put("ResultDesc",        "Accepted");
            return ResponseEntity.ok(ack);

        } catch (Exception e) {
            e.printStackTrace();
            ack.put("ResultCode", 1);
            ack.put("ResultDesc", "Failed");
            return ResponseEntity.status(500).body(ack);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/mpesa/test-auth
    // Quick endpoint to verify credentials work (dev only)
    // ─────────────────────────────────────────────────────────────
    @GetMapping("/test-auth")
    public ResponseEntity<Map<String, Object>> testAuth() {
        Map<String, Object> result = new HashMap<>();
        try {
            // Re-instantiate to avoid circular dependency in test
            com.drip.store.config.MpesaConfig cfg = new com.drip.store.config.MpesaConfig();
            String token = cfg.getAccessToken();
            result.put("success", true);
            result.put("token",   token.substring(0, 12) + "...");  // partial for security
            result.put("message", "M-Pesa credentials are working ✅");
        } catch (Exception e) {
            result.put("success", false);
            result.put("error",   e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}