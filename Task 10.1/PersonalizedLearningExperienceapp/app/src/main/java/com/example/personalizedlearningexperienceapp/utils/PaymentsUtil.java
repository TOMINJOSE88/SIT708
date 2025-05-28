package com.example.personalizedlearningexperienceapp.utils;


import android.content.Context;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class PaymentsUtil {
    private static final List<String> SUPPORTED_NETWORKS = Arrays.asList(
            "AMEX",
            "DISCOVER",
            "JCB",
            "MASTERCARD",
            "VISA"
    );

    private static final List<String> SUPPORTED_METHODS = Arrays.asList(
            "PAN_ONLY",
            "CRYPTOGRAM_3DS"
    );

    private static final String COUNTRY_CODE = "US";
    private static final String CURRENCY_CODE = "USD";

    // Test merchant name
    private static final String MERCHANT_NAME = "Example Merchant";

    public static PaymentsClient createPaymentsClient(Context context) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build();

        return Wallet.getPaymentsClient(context, walletOptions);
    }

    public static JSONObject isReadyToPayRequest() {
        try {
            JSONObject request = new JSONObject();
            request.put("apiVersion", 2);
            request.put("apiVersionMinor", 0);
            request.put("allowedPaymentMethods", new JSONArray().put(baseCardPaymentMethod()));
            return request;
        } catch (JSONException e) {
            throw new RuntimeException("Failed to create isReadyToPayRequest", e);
        }
    }

    private static JSONObject baseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");

        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", new JSONArray(SUPPORTED_METHODS));
        parameters.put("allowedCardNetworks", new JSONArray(SUPPORTED_NETWORKS));
        parameters.put("billingAddressRequired", true);

        JSONObject billingAddressParameters = new JSONObject();
        billingAddressParameters.put("format", "FULL");
        parameters.put("billingAddressParameters", billingAddressParameters);

        cardPaymentMethod.put("parameters", parameters);

        return cardPaymentMethod;
    }

    public static JSONObject getPaymentDataRequest(String price) {
        try {
            JSONObject request = new JSONObject();
            request.put("apiVersion", 2);
            request.put("apiVersionMinor", 0);

            JSONObject merchantInfo = new JSONObject();
            merchantInfo.put("merchantName", MERCHANT_NAME);
            request.put("merchantInfo", merchantInfo);

            JSONObject transactionInfo = new JSONObject();
            transactionInfo.put("totalPrice", price);
            transactionInfo.put("totalPriceStatus", "FINAL");
            transactionInfo.put("countryCode", COUNTRY_CODE);
            transactionInfo.put("currencyCode", CURRENCY_CODE);
            request.put("transactionInfo", transactionInfo);

            request.put("allowedPaymentMethods", new JSONArray().put(cardPaymentMethod()));

            return request;
        } catch (JSONException e) {
            throw new RuntimeException("Failed to create PaymentDataRequest", e);
        }
    }

    private static JSONObject cardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = baseCardPaymentMethod();

        JSONObject tokenizationSpecification = new JSONObject();
        tokenizationSpecification.put("type", "PAYMENT_GATEWAY");
        JSONObject parameters = new JSONObject();
        parameters.put("gateway", "example");
        parameters.put("gatewayMerchantId", "exampleGatewayMerchantId");
        tokenizationSpecification.put("parameters", parameters);

        cardPaymentMethod.put("tokenizationSpecification", tokenizationSpecification);

        return cardPaymentMethod;
    }
}