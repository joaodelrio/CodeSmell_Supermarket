package com.app.SuperMarketSystem.service;

import com.app.SuperMarketSystem.dto.ApiResponse;
import com.app.SuperMarketSystem.model.Order;
import com.app.SuperMarketSystem.model.User;
import com.app.SuperMarketSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApiResponse findAllUsers() {
        try {
            List<User> userList = userRepository.findAll();
            return notFoundListUser(userList);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse addNewUser(User user) {
        try {
            userRepository.save(user);
            return new ApiResponse("Successfully added user within the database", HttpStatus.OK.value(), user);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse deleteUserById(Integer userId) {
        try {
            User user = userRepository.getById(userId);
            ApiResponse response = notFoundUser(user);
            if (response.getStatus() == HttpStatus.OK.value()) {
                user.setOrders(null);
                userRepository.delete(user);
            }
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse deleteUser(User user) {
        try {
            userRepository.delete(user);
            return new ApiResponse("Successfully deleted user from within the database", HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse updateUser(User updatedUser) {
        try {
            User existingUser = userRepository.getById(updatedUser.getId());
            ApiResponse response = notFoundUser(existingUser);
            if (response.getStatus() == HttpStatus.OK.value()) {
                userRepository.save(updatedUser);
            }
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse getUserById(Integer userId) {
        try {
            User user = userRepository.getById(userId);
            return notFoundUser(user);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse getOrdersByUserId(Integer userId) {
        try {
            User user = userRepository.getById(userId);
            ApiResponse response = notFoundUser(user);
            if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                return response;
            } else {
                List<Order> orders = user.getOrders();
                return notFoundListOrders(orders);
            }
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    private ApiResponse notFoundUser(User user) {
        if (user == null) {
            return new ApiResponse("No such user found within the database", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("Successful", HttpStatus.OK.value(), user);
        }
    }

    private ApiResponse notFoundListUser(List<User> userList) {
        if (userList.isEmpty()) {
            return new ApiResponse("No users found within the database", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("Successfully fetched users from within database", HttpStatus.OK.value(), userList);
        }
    }

    private ApiResponse notFoundListOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            return new ApiResponse("No orders made yet by this user", HttpStatus.NOT_FOUND.value(), null);
        } else {
            return new ApiResponse("These are the orders made by the user", HttpStatus.OK.value(), orders);
        }
    }
}
