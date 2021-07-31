package com.reactiveExample.ReactiveSpring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ProductCatalogueCompletableFuture2Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCatalogueCompletableFuture2Controller.class);

    @Autowired
    GetProductDetails getProductDetails;
    @Autowired
    ExchangeService exchangeService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(100);//need bigger thread pool size for testing

    @GetMapping(path = "/product/completablefuture2/{productName}")
    public ResponseEntity<Integer> productDetailsConcurrent(@PathVariable String productName) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        getProduct(productName, executorService)
                .thenCombineAsync(getRates("USD", executorService), this::getMessage, executorService)//get rates and get product will run concurrently at a time as they are independent
                .thenAcceptAsync((message) -> {
                    LOGGER.debug(message);
                    LOGGER.debug("time taken: {} ms", System.currentTimeMillis() - start);
                }, executorService)
                .join();
        return ResponseEntity.ok(456);
    }


    private CompletableFuture<Product> getProduct(String productName, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> getProductDetails.getProductDetails(productName), executorService);
    }

    private CompletableFuture<Integer> getRates(String currency, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> exchangeService.getRates(currency), executorService);
    }

    private String getMessage(Product product, Integer rate) {
        return "ProductId: " + product.id + " , exchange Rate: " + rate;
    }

}
