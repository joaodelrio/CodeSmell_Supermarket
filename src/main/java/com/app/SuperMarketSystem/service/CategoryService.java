package com.app.SuperMarketSystem.service;

import com.app.SuperMarketSystem.dto.ApiResponse;
import com.app.SuperMarketSystem.model.Category;
import com.app.SuperMarketSystem.model.Product;
import com.app.SuperMarketSystem.repository.CategoryRepository;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ApiResponse findAllCategories() {
        try {
            List<Category> categoriesList = categoryRepository.findAll();
            ApiResponse response = notFoundListCategory(categoriesList);
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse addCategory(Category category) {
        try {
            categoryRepository.save(category);
            return new ApiResponse("Successfully added category in the database", HttpStatus.OK.value(), category);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse deleteCategory(String categoryId) {
        try {
            Category category = categoryRepository.getById(categoryId);
            ApiResponse response = notFoundCategory(category);
            if (response.getStatus() == HttpStatus.OK.value()) {
                category.setProducts(null);
                categoryRepository.delete(category);
            } 
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse updateCategory(Category category) {
        try {
            Category existingCategory = categoryRepository.getById(category.getId());
            ApiResponse response = notFoundCategory(existingCategory);
            if (response.getStatus() == HttpStatus.OK.value()) {
                categoryRepository.save(category);
            } 
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse getCategoryById(String categoryId) {
        try {
            Category category = categoryRepository.getById(categoryId);
            ApiResponse response = notFoundCategory(category);
            return response;
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    public ApiResponse addProductsInCategory(String categoryId, List<Product> productList) {
        try {
            Category category = categoryRepository.getById(categoryId);
            ApiResponse response = notFoundCategory(category);
            if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                return response;
            } else {
                if (productList.isEmpty()) {
                    return new ApiResponse("The product list is empty", HttpStatus.OK.value(), productList);
                } else {
                    category.getProducts().addAll(productList);
                    categoryRepository.save(category);
                    return new ApiResponse("Successfully added products within the category", HttpStatus.OK.value(), category);
                }
            }
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }
    
    private ApiResponse notFoundCategory(Category category) {
        if (category == null) {
            return new ApiResponse("No such category found within the database", HttpStatus.NOT_FOUND.value(), null);
        }else{
            return new ApiResponse("Successful", HttpStatus.OK.value(), category);
        }
    }

    private ApiResponse notFoundListCategory(List<Category> categoriesList) {
        if (categoriesList.isEmpty()) {
            return new ApiResponse("No categories found within the database", HttpStatus.NOT_FOUND.value(), null);
        }else{
            return new ApiResponse("Successful", HttpStatus.OK.value(), categoriesList);
        }
    }
}
