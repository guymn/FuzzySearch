package com.redis.index.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.index.entity.ProductNGrams;
import com.redis.index.entity.ProductTempModel;
import com.redis.index.repository.ProductNGramsRepository;

@Service
public class ProductNGramsService {
    @Autowired
    private ProductNGramsRepository productNGramsRepository;

    public ProductNGrams retrieveNGramsByNgram(String nGram) {
        return productNGramsRepository.findByNgram(nGram).orElse(null);
    }

    public void createProductNGramsByProductTempModel(ProductTempModel productTempModel) {
        String cleanedString = productTempModel.getProductName().replaceAll("[^\\p{IsThai}a-zA-Z0-9]",
                "");

        int N = 3;
        List<String> nGramList = generateNGrams(cleanedString, N);

        for (String s : nGramList) {
            ProductNGrams productNGrams;
            ProductNGrams existingProductNGrams = retrieveNGramsByNgram(s);
            if (existingProductNGrams != null) {
                productNGrams = existingProductNGrams;
            } else {
                productNGrams = new ProductNGrams();
                productNGrams.setNgram(s);
                productNGrams.setProductId(new HashSet<>());
            }
            productNGrams.getProductId().add(productTempModel.getProductId());

            productNGramsRepository.save(productNGrams);
        }

    }

    public Object createProductNGramsFromAllProduct(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        String url = String.format("http://localhost:9001/api/test/findById?&page=%d&size=%d", page, size);

        // Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // Create a GET HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parse the JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());

                // Navigate to payload.content
                JsonNode payloadNode = rootNode.path("payload");
                JsonNode contentNode = payloadNode.path("content");

                if (contentNode.isArray()) {
                    // Map the content to a List of ProductTempModel
                    List<ProductTempModel> productList = objectMapper.readValue(
                            contentNode.toString(),
                            new TypeReference<List<ProductTempModel>>() {
                            });

                    for (ProductTempModel productTempModel : productList) {
                        String cleanedString = productTempModel.getProductName().replaceAll("[^\\p{IsThai}a-zA-Z0-9]",
                                "");

                        List<String> nStrings = generateNGrams(cleanedString, 3);

                        for (String s : nStrings) {
                            ProductNGrams productNGrams;
                            ProductNGrams existingProductNGrams = retrieveNGramsByNgram(s);
                            if (existingProductNGrams != null) {
                                productNGrams = existingProductNGrams;
                            } else {
                                productNGrams = new ProductNGrams();
                                productNGrams.setNgram(s);
                                productNGrams.setProductId(new HashSet<>());
                            }
                            productNGrams.getProductId().add(productTempModel.getProductId());

                            productNGramsRepository.save(productNGrams);
                        }

                    }

                    return productList;
                } else {
                    System.out.println("Content is not an array!");
                }
            } else {
                System.out.println("Failed to fetch data. HTTP Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> generateNGrams(String originalString, int n) {
        List<String> nGrams = new ArrayList<>();
        if (n == 0) {
            return nGrams;
        }

        for (int i = 0; i < originalString.length() - (n - 1); i++) {
            nGrams.add(originalString.substring(i, i + n));
        }

        return nGrams;
    }

}
