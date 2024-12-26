package com.fuzzy.search.service;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzzy.search.dto.ProductResponse;
import com.fuzzy.search.model.ProductTempModel;
import com.fuzzy.search.repo.ProductTempModelRepository;
import com.fuzzy.search.utils.NGramsUtils;

@Service
public class FuzzySearchService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductTempModelRepository productTempModelRepository;

    public static final String AND = " AND ";
    public static final String OR = " OR ";

    public List<ProductTempModel> fuzzySearch(String productName, Pageable pageable) {
        int N = 2;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM SCAT.PRODUCT_TEMP P WHERE 1 = 1 ");
        List<String> nGrams = NGramsUtils.generateNGrams(productName, N);
        List<String> listCondition = NGramsUtils.getListConditionString(nGrams, "p.PRODUCT_NAME");

        if (!listCondition.isEmpty()) {
            sql.append(AND);
            sql.append(NGramsUtils.getConditionString(listCondition, OR));
            sql.append("ORDER BY ( " + NGramsUtils.getConditionString(listCondition, "::int + ") + "::int ) DESC ");

        }

        // page
        int pageNumber = pageable.getPageNumber();

        sql.append(" LIMIT ");
        sql.append(pageable.getPageSize() * (pageNumber + 1));
        sql.append(" OFFSET ");
        sql.append(pageable.getPageSize() * pageNumber);

        System.out.println("SQL : {}" + sql);

        // query sql
        List<ProductTempModel> result = jdbcTemplate.query(sql.toString(),
                new BeanPropertyRowMapper<>(ProductTempModel.class));

        return result;
    }

    public Page<ProductTempModel> findByIds(Set<Integer> productIds, Pageable pageable) {
        List<Integer> idList = new ArrayList<>(productIds);

        // Apply pagination to idList (limit the results based on the page size)
        int start = (int) pageable.getOffset(); // Calculate start index based on the page
        int end = Math.min((start + pageable.getPageSize()), idList.size()); // Ensure the index is within bounds

        List<Integer> pagedIdList = idList.subList(start, end); // Create a sublist based on pagination

        List<ProductTempModel> data = productTempModelRepository.findAllById(pagedIdList);

        return new PageImpl<>(data, pageable, idList.size());
    }

    public Object fuzzySearchByRedis(String productName, Pageable pageable) throws Exception {
        int N = 2;
        List<String> nGramList = NGramsUtils.generateNGrams(productName, N);
        List<Set<Integer>> productResponses = new ArrayList<>();

        for (String string : nGramList) {
            String url = "http://localhost:9000/api/ngrams/getProductNGramByNGram?ngram=" + string;
            HttpClient client = HttpClient.newHttpClient();

            // Create a GET HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                // Deserialize the response body to ProductResponse
                ObjectMapper objectMapper = new ObjectMapper();
                ProductResponse productResponse = objectMapper.readValue(response.body(), ProductResponse.class);

                productResponses.add(productResponse.getProductId());
            } catch (ConnectException e) {
                throw new ConnectException("Connection to Redis is fail");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // รวบรวม productId ทั้งหมดและนับจำนวนครั้งที่ปรากฏ
        Map<Integer, Long> frequencyMap = productResponses.stream()
                .flatMap(Set::stream) // แปลง Set<Integer> เป็น Stream<Integer>
                .collect(Collectors.groupingBy(id -> id, Collectors.counting())); // นับจำนวนครั้งที่ปรากฏ

        // เรียงลำดับตามจำนวนครั้งที่ปรากฏ (มาก -> น้อย)
        Set<Integer> sortedProductIdSet = frequencyMap.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // เรียงจากมากไปน้อย
                .map(Map.Entry::getKey) // ดึงเฉพาะ key (ตัวเลข)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // เก็บใน Set ที่รักษาลำดับ

        return findByIds(sortedProductIdSet, pageable);
    }

    public Page<ProductTempModel> findAllProductTemp(Pageable pageable) {
        return productTempModelRepository.findAll(pageable);
    }
}
