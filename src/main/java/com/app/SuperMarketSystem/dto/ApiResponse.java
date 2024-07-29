package com.app.SuperMarketSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ApiResponse {
    Integer status;
    String message;
    Object data = new Object();

    public ApiResponse(String message, int status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}