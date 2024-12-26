package com.redis.index.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redis.index.entity.ProductNGrams;
import com.redis.index.entity.ProductTempModel;
import com.redis.index.service.ProductNGramsService;

@RestController
@RequestMapping("/ngrams")
public class ProductNGramsController {
    @Autowired
    private ProductNGramsService peoductNGramsService;

    @GetMapping("getProductNGramByNGram")
    public ResponseEntity<?> getProductNgramByNgram(String ngram) {
        ProductNGrams nGrams = peoductNGramsService.retrieveNGramsByNgram(ngram);
        return ResponseEntity.ok(nGrams);
    }

    @PostMapping()
    public ResponseEntity<?> createProductNGrams(@RequestBody ProductTempModel body, int N) {
        peoductNGramsService.createProductNGramsByProductTempModel(body, N);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("addAll")
    public ResponseEntity<?> createProductNGramsWithJSON(Pageable pageable) {
        Object productNGrams = peoductNGramsService.createProductNGramsFromAllProduct(pageable);
        return ResponseEntity.status(HttpStatus.CREATED).body(productNGrams);
    }
}
