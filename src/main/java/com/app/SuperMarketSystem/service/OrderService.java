package com.app.SuperMarketSystem.service;

import com.app.SuperMarketSystem.dto.ApiResponse;
import com.app.SuperMarketSystem.model.Order;
import com.app.SuperMarketSystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ApiResponse findAllOrders() {
        try {
            List<Order> ordersList = orderRepository.findAll();
            return notFoundListOrder(ordersList);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse addOrder(Order order) {
        try {
            orderRepository.save(order);
            return new ApiResponse("Successfully added order within the database", HttpStatus.OK.value(), order);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse deleteOrder(String orderId) {
        try {
            Order order = orderRepository.getById(orderId);
            ApiResponse response = notFoundOrder(order);
            if (response.getStatus() == HttpStatus.OK.value()) {
                orderRepository.delete(order);
            }
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse updateOrder(Order order) {
        try {
            Order existingOrder = orderRepository.getById(order.getOrderNumber());
            ApiResponse response = notFoundOrder(existingOrder);
            if (response.getStatus() == HttpStatus.OK.value()) {
                orderRepository.save(order);
            }
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse getOrderByOrderNumber(String orderNumber) {
        try {
            Order order = orderRepository.getById(orderNumber);
            return notFoundOrder(order);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    private ApiResponse notFoundOrder(Order order) {
        if (order == null) {
            return new ApiResponse("No such order found within the database", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("Successful", HttpStatus.OK.value(), order);
        }
    }

    private ApiResponse notFoundListOrder(List<Order> ordersList) {
        if (ordersList.isEmpty()) {
            return new ApiResponse("No orders found within the database", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("Successfully fetched orders from the database", HttpStatus.OK.value(), ordersList);
        }
    }
}
