package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.service.RazorPayService;
import com.razorpay.RazorpayException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final RazorPayService service;

    public PaymentController(RazorPayService service) {
        this.service = service;
    }

    @PostMapping("/create-order")
    public String createOrder(@RequestParam long amount ,@RequestParam String currency) throws RazorpayException {
        try {
            return service.createOrder(amount, currency, "receipt_2100");
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }
}
