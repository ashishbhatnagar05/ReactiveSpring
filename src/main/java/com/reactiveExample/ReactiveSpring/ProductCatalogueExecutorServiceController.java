package com.reactiveExample.ReactiveSpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class ProductCatalogueExecutorServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCatalogueExecutorServiceController.class);

    @Autowired
    GetProductDetails getProductDetails;
    @Autowired
    ExchangeService exchangeService;

    @GetMapping(path = "/product/executorservice/{productName}")
    public ResponseEntity<Integer> productDetailsConcurrent(@PathVariable String productName) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Product> productFuture = executorService.submit(() -> getProductDetails.getProductDetails(productName));
        Future<Integer> rateFuture = executorService.submit(() -> exchangeService.getRates("USD"));
        LOGGER.debug("ProductId: {} , exchange Rate: {}", productFuture.get().id, rateFuture.get());
        LOGGER.debug("time taken: {} ms", System.currentTimeMillis() - start);
        executorService.shutdownNow();
        return ResponseEntity.ok(456);
    }

}
