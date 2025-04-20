package com.examplejjwt.jwtauth.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorPayService {
    @Value("${razorpay.api.key}")
    private String apiKey;
    @Value("${razorpay.api.secret}")
    private String apiSecret;

    public String createOrder(long amount, String currency,String receiptId) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecret);
        JSONObject orderreq = new JSONObject();
        orderreq.put("amount",amount);
        orderreq.put("currency",currency);
        orderreq.put("receipt",receiptId);
//        orderreq.put("message","payment successfully");
        Order order = razorpayClient.orders.create(orderreq);
        return order.toString();
    }
}
