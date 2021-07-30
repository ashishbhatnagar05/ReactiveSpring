package com.reactiveExample.ReactiveSpring;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ExchangeService {

    public Integer getRates(String currency) {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(10) * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 73;
    }
}
