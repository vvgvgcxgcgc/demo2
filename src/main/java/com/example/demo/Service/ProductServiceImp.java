package com.example.demo.Service;

import com.example.demo.Domain.Product;
import com.example.demo.Respories.ProductRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImp implements ProductService{
    private final ProductRepos productRepos;

    @Autowired
    public ProductServiceImp(ProductRepos productRepos) {
        this.productRepos = productRepos;
    }
    @Override
    public List<Product> getAllProducts() {
        return (List<Product>) productRepos.findAll();
    }
}
