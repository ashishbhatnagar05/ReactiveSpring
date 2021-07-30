package com.reactiveExample.ReactiveSpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCatalogueThreadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCatalogueThreadController.class);

    @Autowired
    GetProductDetails getProductDetails;
    @Autowired
    ExchangeService exchangeService;

    @GetMapping(path = "/product/thread/{productName}")
    public ResponseEntity<Integer> productDetailsConcurrent(@PathVariable String productName) throws InterruptedException {
        //LOGGER.debug("Processing");
        long start = System.currentTimeMillis();
        ProductServiceRunnable productServiceRunnable = new ProductServiceRunnable(productName);
        ExchangeServiceRunnable exchangeServiceRunnable = new ExchangeServiceRunnable("USD");
        Thread thread1 = new Thread(productServiceRunnable);
        thread1.start();
        Thread thread2 = new Thread(exchangeServiceRunnable);
        thread2.start();

        thread1.join();
        thread2.join();
        LOGGER.debug("ProductId: {} , exchange Rate: {}", productServiceRunnable.product.id, exchangeServiceRunnable.rate);
        LOGGER.debug("time taken: {} ms", System.currentTimeMillis() - start);
        return ResponseEntity.ok(456);
    }


    class ProductServiceRunnable implements Runnable {
        String productName;
        Product product;

        public ProductServiceRunnable(String productName) {
            this.productName = productName;
        }

        @Override
        public void run() {
            this.product = getProductDetails.getProductDetails(productName);
        }
    }

    class ExchangeServiceRunnable implements Runnable {
        String currency;
        Integer rate;

        public ExchangeServiceRunnable(String currency) {
            this.currency = currency;
        }

        @Override
        public void run() {
            this.rate = exchangeService.getRates(currency);
        }
    }
}
