package com.redis.index.entity;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RedisHash("product_ngrams")
public class ProductNGrams implements Serializable {
    @Id
    private String ngram;
    private Set<Integer> productId;
}
