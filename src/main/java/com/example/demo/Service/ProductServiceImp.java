package com.example.demo.Service;

import com.example.demo.Domain.Product;
import com.example.demo.Respories.ProductRepos;
import com.example.demo.dto.Productdt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
@Service
public class ProductServiceImp implements ProductService {


    private final ProductRepos productRepos;

    @Autowired
    public ProductServiceImp(ProductRepos productRepos) {
        this.productRepos = productRepos;
    }

    @Override
    public List<Product> getAllProducts() {
        return (List<Product>) productRepos.findAll();
    }

    @Override
    public Product save(MultipartFile imageProduct, Productdt productdt) throws IOException {
        Product product = Product.builder()
                                .id(productdt.getId())
                                .Name(productdt.getName())
                                .Price(productdt.getPrice())
                                .Info(productdt.getInfo())
                                .Image("")
                                .deleted(false)
                                .build();
        if (imageProduct == null) {
            product.setImage(null);
        } else {
            product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
        }
        return productRepos.save(product);
    }
    @Override
    public String getProductImageById(String productId){
        return productRepos.findProductImageById(productId);
    }
    @Override
    public void deleteById(String id) {
        Product product = productRepos.getById(id);
        product.setDeleted(true);

        productRepos.save(product);
    }

}
