package com.example.demo.Service;

import com.example.demo.Domain.Product;
import com.example.demo.dto.Productdt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    public List<Product> getAllProducts();
    public Product save(MultipartFile imageProduct, Productdt productdt) throws IOException;
    public Product update(MultipartFile imageProduct, Productdt productdt);
    public String getProductImageById(String productId);
    public void deleteById(String id);
    public void enableById(String id);
    public Productdt getById(String id);
    public Product getProductById(String id);
}
