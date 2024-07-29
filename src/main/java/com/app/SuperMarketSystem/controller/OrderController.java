package com.app.SuperMarketSystem.controller;

import com.app.SuperMarketSystem.dto.ApiResponse;
import com.app.SuperMarketSystem.model.Order;
import com.app.SuperMarketSystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController implements ICrudController<Order, String> {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ApiResponse list() {
        return orderService.findAllOrders();
    }

    @Override
    public ApiResponse save(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @Override
    public ApiResponse update(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    @Override
    public ApiResponse delete(@PathVariable(name = "id") String orderId) {
        return orderService.deleteOrder(orderId);
    }

    @Override
    public ApiResponse getById(@PathVariable(name = "id") String orderId) {
        return orderService.getOrderByOrderNumber(orderId);
    }
}
