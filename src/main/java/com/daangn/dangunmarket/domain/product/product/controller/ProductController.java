package com.daangn.dangunmarket.domain.product.product.controller;

import com.daangn.dangunmarket.domain.product.product.controller.dto.ProductCreateApiRequest;
import com.daangn.dangunmarket.domain.product.product.controller.mapper.ProductApiMapper;
import com.daangn.dangunmarket.domain.product.product.facade.ProductFacade;
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
    public ResponseEntity<Long> createProduct(@RequestBody @Valid ProductCreateApiRequest request){
        Long productId = productFacade.createProduct(mapper.toProductCreateRequest(request));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(uri).body(productId);
    }

}
