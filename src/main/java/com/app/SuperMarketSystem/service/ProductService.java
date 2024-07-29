package com.app.SuperMarketSystem.service;

import com.app.SuperMarketSystem.dto.ApiResponse;
import com.app.SuperMarketSystem.dto.ProductDTO;
import com.app.SuperMarketSystem.model.Order;
import com.app.SuperMarketSystem.model.Product;
import com.app.SuperMarketSystem.model.User;
import com.app.SuperMarketSystem.repository.OrderRepository;
import com.app.SuperMarketSystem.repository.ProductRepository;
import com.app.SuperMarketSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public ApiResponse findAllProducts() {
        try {
            List<Product> productList = productRepository.findAll();
            return notFoundListProduct(productList);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse addProduct(Product product) {
        try {
            productRepository.save(product);
            return new ApiResponse("Successfully added product within the database", HttpStatus.OK.value(), product);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse deleteProduct(String productId) {
        try {
            Product product = productRepository.getById(productId);
            ApiResponse response = notFoundProduct(product);
            if (response.getStatus() == HttpStatus.OK.value()) {
                productRepository.delete(product);
            }
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse updateProduct(Product updatedProduct) {
        try {
            Product existingProduct = productRepository.getById(updatedProduct.getId());
            ApiResponse response = notFoundProduct(existingProduct);
            if (response.getStatus() == HttpStatus.OK.value()) {
                productRepository.save(updatedProduct);
            }
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse getProductById(String productId) {
        try {
            Product product = productRepository.getById(productId);
            return notFoundProduct(product);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse purchaseProducts(Integer userId, List<ProductDTO> productsList) {
        try {
            User user = userRepository.getById(userId);
            ApiResponse response = notFoundUser(user);
            if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                return response;
            } else {
                Order order = new Order();
                order.setTotalPrice(0.0);
                for (ProductDTO product : productsList) {
                    Product productToBuy = productRepository.getById(product.getProductId());
                    ApiResponse productResponse = notFoundProduct(productToBuy);
                    if (productResponse.getStatus() == HttpStatus.NOT_FOUND.value()) {
                        return productResponse;
                    }
                    order.setTotalPrice(productToBuy.getPrice() * product.getQuantityToPurchase() + order.getTotalPrice());
                    order.getProducts().add(productToBuy);
                }
                order.setDeliveryStatus("Pending");
                order.setOrderTime(LocalDateTime.now());
                orderRepository.save(order);
                user.getOrders().add(order);
                userRepository.save(user);
                return new ApiResponse("Successfully placed the orders", HttpStatus.OK.value(), order);
            }
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    private ApiResponse notFoundProduct(Product product) {
        if (product == null) {
            return new ApiResponse("No such product found within the database", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("Successful", HttpStatus.OK.value(), product);
        }
    }

    private ApiResponse notFoundUser(User user) {
        if (user == null) {
            return new ApiResponse("No such user found within the database", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("Successful", HttpStatus.OK.value(), user);
        }
    }

    private ApiResponse notFoundListProduct(List<Product> productList) {
        if (productList.isEmpty()) {
            return new ApiResponse("No products found within the database", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("Successfully fetched products from the database", HttpStatus.OK.value(), productList);
        }
    }
}
