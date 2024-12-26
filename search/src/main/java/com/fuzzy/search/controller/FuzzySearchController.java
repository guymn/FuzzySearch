package com.fuzzy.search.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuzzy.search.response.Response;
import com.fuzzy.search.service.FuzzySearchService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/fuzzySearch")
public class FuzzySearchController {
    @Autowired
    private FuzzySearchService fuzzySearchService;

    @GetMapping("fuzzySearch")
    public ResponseEntity<Response> fuzzySearch(String productName, Pageable pageable) {
        long startTime = System.nanoTime(); // บันทึกเวลาเริ่มต้น

        Response response = new Response();
        response.setTimestamp(new Date());
        try {
            response.setPayload(fuzzySearchService.fuzzySearch(productName, pageable));
            response.setStatus(HttpStatus.OK.value());
            response.setCode(HttpStatus.OK.getReasonPhrase());

            long endTime = System.nanoTime(); // บันทึกเวลาสิ้นสุด
            long durationInMillis = (endTime - startTime) / 1_000_000; // แปลงเป็นมิลลิวินาที
            System.out.println("Time taken for fuzzySearch: " + durationInMillis + " ms");
            response.setTimeTaken(durationInMillis);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("fuzzySearchByRedis")
    public ResponseEntity<Response> fuzzySearchByRedis(String productName, Pageable pageable) {
        long startTime = System.nanoTime(); // บันทึกเวลาเริ่มต้น

        Response response = new Response();
        response.setTimestamp(new Date());
        try {
            response.setPayload(fuzzySearchService.fuzzySearchByRedis(productName, pageable));
            response.setStatus(HttpStatus.OK.value());
            response.setCode(HttpStatus.OK.getReasonPhrase());

            long endTime = System.nanoTime(); // บันทึกเวลาสิ้นสุด
            long durationInMillis = (endTime - startTime) / 1_000_000; // แปลงเป็นมิลลิวินาที
            System.out.println("Time taken for fuzzySearch: " + durationInMillis + " ms");
            response.setTimeTaken(durationInMillis);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<Response> findAllProductTemp(Pageable pageable) {
        Response response = new Response();
        response.setTimestamp(new Date());
        try {
            response.setPayload(fuzzySearchService.findAllProductTemp(pageable));
            response.setStatus(HttpStatus.OK.value());
            response.setCode(HttpStatus.OK.getReasonPhrase());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
