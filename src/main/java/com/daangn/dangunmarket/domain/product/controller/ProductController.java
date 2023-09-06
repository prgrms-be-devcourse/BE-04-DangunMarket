package com.daangn.dangunmarket.domain.product.controller;

import com.daangn.dangunmarket.domain.product.controller.dto.ProductCreateApiRequest;
import com.daangn.dangunmarket.domain.product.controller.dto.ProductCreateApiResponse;
import com.daangn.dangunmarket.domain.product.controller.mapper.ProductApiMapper;
import com.daangn.dangunmarket.domain.product.facade.ProductFacade;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/products",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private ProductFacade productFacade;
    private ProductApiMapper mapper;

    public ProductController(ProductFacade productFacade, ProductApiMapper mapper) {
        this.productFacade = productFacade;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ProductCreateApiResponse> createProduct(@RequestBody @Valid ProductCreateApiRequest request) {
        Long productId = productFacade.createProduct(mapper.toProductCreateRequest(request));
        ProductCreateApiResponse response = ProductCreateApiResponse.of(productId);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

}
