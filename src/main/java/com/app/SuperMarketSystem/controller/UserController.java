package com.app.SuperMarketSystem.controller;

import com.app.SuperMarketSystem.dto.ApiResponse;
import com.app.SuperMarketSystem.model.User;
import com.app.SuperMarketSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController implements ICrudController<User, Integer> {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ApiResponse list() {
        return userService.findAllUsers();
    }

    @Override
    public ApiResponse save(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    @Override
    public ApiResponse update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @Override
    public ApiResponse delete(@PathVariable(name = "id") Integer userId) {
        return userService.deleteUserById(userId);
    }

    @Override
    public ApiResponse getById(@PathVariable(name = "id") Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/getOrdersByUserId/{id}")
    public ApiResponse getOrdersByUserId(@PathVariable(name = "id") Integer userId) {
        return userService.getOrdersByUserId(userId);
    }
}
