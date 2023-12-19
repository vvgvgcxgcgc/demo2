package com.example.demo.Service;

import com.example.demo.Domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public List<Product> getAllProducts();

}
