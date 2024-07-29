package com.app.SuperMarketSystem.controller;

import com.app.SuperMarketSystem.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ICrudController<T, ID> {
    @GetMapping("/list")
    ApiResponse list();

    @PostMapping("/save")
    ApiResponse save(@RequestBody T entity);

    @PutMapping("/update")
    ApiResponse update(@RequestBody T entity);

    @DeleteMapping("/delete/{id}")
    ApiResponse delete(@PathVariable(name = "id") ID id);

    @GetMapping("/getBy/{id}")
    ApiResponse getById(@PathVariable(name = "id") ID id);
}
