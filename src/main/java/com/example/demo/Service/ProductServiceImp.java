package com.example.demo.Service;

import com.example.demo.Domain.Product;
import com.example.demo.Respories.ProductRespo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImp implements ProductService{
    private final ProductRespo productRespo;

    @Autowired
    public ProductServiceImp(ProductRespo productRespo){
        this.productRespo = productRespo;
    }
    @Override
    public List<Product> getAllProducts() {
        return (List<Product>) productRespo.findAll();

    }
}
