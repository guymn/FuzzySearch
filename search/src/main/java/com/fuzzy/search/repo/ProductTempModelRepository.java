package com.fuzzy.search.repo;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fuzzy.search.model.ProductTempModel;

public interface ProductTempModelRepository extends JpaRepository<ProductTempModel, Integer> {

    @Query("SELECT p FROM ProductTempModel p WHERE p.productId IN :productIds")
    Page<ProductTempModel> findByIds(@Param("productIds") Set<Integer> sortedProductIds, Pageable pageable);

}