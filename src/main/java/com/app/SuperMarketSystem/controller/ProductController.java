package com.app.SuperMarketSystem.controller;

import com.app.SuperMarketSystem.dto.ApiResponse;
import com.app.SuperMarketSystem.dto.ProductDTO;
import com.app.SuperMarketSystem.model.Product;
import com.app.SuperMarketSystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController implements ICrudController<Product, String> {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ApiResponse list() {
        return productService.findAllProducts();
    }

    @Override
    public ApiResponse save(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @Override
    public ApiResponse update(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @Override
    public ApiResponse delete(@PathVariable(name = "id") String productId) {
        return productService.deleteProduct(productId);
    }

    @Override
    public ApiResponse getById(@PathVariable(name = "id") String productId) {
        return productService.getProductById(productId);
    }

    @PostMapping("/purchase")
    public ApiResponse purchase(@RequestParam(name = "userId") Integer userId, @RequestBody List<ProductDTO> productList) {
        return productService.purchaseProducts(userId, productList);
    }
}
