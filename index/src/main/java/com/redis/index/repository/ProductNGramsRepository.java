package com.redis.index.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.redis.index.entity.ProductNGrams;

public interface ProductNGramsRepository extends CrudRepository<ProductNGrams, String> {
    Optional<ProductNGrams> findByNgram(String ngram);
}
