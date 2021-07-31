package com.reactiveExample.ReactiveSpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

@RestController
public class ProductCatalogueCompletableFutureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCatalogueCompletableFutureController.class);

    @Autowired
    GetProductDetails getProductDetails;
    @Autowired
    ExchangeService exchangeService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(100);

    @GetMapping(path = "/product/completablefuture/{productName}")
    public ResponseEntity<Integer> productDetailsConcurrent(@PathVariable String productName) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        CompletableFuture<Product> productFuture = CompletableFuture.supplyAsync(() -> getProductDetails.getProductDetails(productName), executorService);
        CompletableFuture<Integer> rateFuture = CompletableFuture.supplyAsync((() -> exchangeService.getRates("USD")), executorService);
        LOGGER.debug("ProductId: {} , exchange Rate: {}", productFuture.join().id, rateFuture.join());
        LOGGER.debug("time taken: {} ms", System.currentTimeMillis() - start);
        return ResponseEntity.ok(456);
    }

}
