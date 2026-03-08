package com.drip.store.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StkPushResponse {

    // ── Safaricom response fields ────────────────────────────────
    @JsonProperty("MerchantRequestID")
    private String merchantRequestId;

    @JsonProperty("CheckoutRequestID")
    private String checkoutRequestId;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @JsonProperty("CustomerMessage")
    private String customerMessage;

    // ── Error response fields ────────────────────────────────────
    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    // ── Internal field ───────────────────────────────────────────
    private String rawResponse;

    public boolean isSuccess() {
        return "0".equals(responseCode);
    }

    // Getters & Setters
    public String getMerchantRequestId()                       { return merchantRequestId;   }
    public void   setMerchantRequestId(String v)               { this.merchantRequestId = v; }

    public String getCheckoutRequestId()                       { return checkoutRequestId;   }
    public void   setCheckoutRequestId(String v)               { this.checkoutRequestId = v; }

    public String getResponseCode()                            { return responseCode;        }
    public void   setResponseCode(String v)                    { this.responseCode = v;      }

    public String getResponseDescription()                     { return responseDescription; }
    public void   setResponseDescription(String v)             { this.responseDescription = v; }

    public String getCustomerMessage()                         { return customerMessage;     }
    public void   setCustomerMessage(String v)                 { this.customerMessage = v;   }

    public String getErrorCode()                               { return errorCode;           }
    public void   setErrorCode(String v)                       { this.errorCode = v;         }

    public String getErrorMessage()                            { return errorMessage;        }
    public void   setErrorMessage(String v)                    { this.errorMessage = v;      }

    public String getRawResponse()                             { return rawResponse;         }
    public void   setRawResponse(String v)                     { this.rawResponse = v;       }
}