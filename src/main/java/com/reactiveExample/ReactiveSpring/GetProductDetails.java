package com.reactiveExample.ReactiveSpring;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GetProductDetails {

    public Product getProductDetails(String productName) {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(10) * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Product(1, "mobile");
    }
}

