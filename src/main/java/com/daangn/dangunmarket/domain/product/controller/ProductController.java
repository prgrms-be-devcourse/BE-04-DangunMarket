package com.daangn.dangunmarket.domain.product.controller;

import com.daangn.dangunmarket.domain.product.controller.dto.ProductCreateApiRequest;
import com.daangn.dangunmarket.domain.product.controller.dto.ProductCreateApiResponse;
import com.daangn.dangunmarket.domain.product.controller.dto.ProductFindApiResponse;
import com.daangn.dangunmarket.domain.product.controller.mapper.ProductApiMapper;
import com.daangn.dangunmarket.domain.product.facade.ProductFacade;
import com.daangn.dangunmarket.domain.product.facade.dto.ProductFindResponseParam;
import com.daangn.dangunmarket.domain.product.service.dto.ProductFindResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        ProductCreateApiResponse response = ProductCreateApiResponse.from(productId);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductFindApiResponse> findById(@PathVariable Long productId){
        ProductFindResponseParam responseParam = productFacade.findById(productId);
        ProductFindApiResponse response = ProductFindApiResponse.from(responseParam);

        return ResponseEntity.ok(response);
    }

}
