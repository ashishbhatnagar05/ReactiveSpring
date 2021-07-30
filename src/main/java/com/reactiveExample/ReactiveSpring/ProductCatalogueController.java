package com.reactiveExample.ReactiveSpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCatalogueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCatalogueController.class);

    @Autowired
    GetProductDetails getProductDetails;
    @Autowired
    ExchangeService exchangeService;

    @GetMapping(path = "/product/{productName}")
    public ResponseEntity<Integer> productDetails(@PathVariable String productName) throws InterruptedException {
        long start = System.currentTimeMillis();
        Product product = getProductDetails.getProductDetails(productName);
        Integer rate = exchangeService.getRates("USD");
        LOGGER.debug("ProductId: {} , exchange Rate: {}", product.id, rate);
        LOGGER.debug("time taken: {} ms", System.currentTimeMillis() - start);
        return ResponseEntity.ok(456);
    }


}



