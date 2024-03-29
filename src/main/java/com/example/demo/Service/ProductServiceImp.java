package com.example.demo.Service;

import com.example.demo.Domain.Product;
import com.example.demo.Respositories.ProductRepos;
import com.example.demo.Utils.ImageUpload;
import com.example.demo.dto.Productdt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {


    private final ProductRepos productRepos;
    private final ImageUpload imageUpload;

    @Autowired
    public ProductServiceImp(ProductRepos productRepos, ImageUpload imageUpload) {
        this.productRepos = productRepos;
        this.imageUpload = imageUpload;
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

                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));


        }
        return productRepos.save(product);
    }

    @Override
    public Product update(MultipartFile imageProduct, Productdt productdt) {
        Product productUpdate = productRepos.getReferenceById(productdt.getId());
        try {
            if (imageProduct.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct)) {
                    productUpdate.setImage(productUpdate.getImage());
                } else {
                    imageUpload.uploadFile(imageProduct);
                    productUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productUpdate.setName(productdt.getName());
        productUpdate.setInfo(productdt.getInfo());
        productUpdate.setPrice(productdt.getPrice());
        return productRepos.save(productUpdate);

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
    @Override
    public void enableById(String id) {
        Product product = productRepos.getById(id);
        product.setDeleted(false);
        productRepos.save(product);
    }
    @Override
    public Productdt getById(String id){
        Product p = productRepos.getById(id);
        Productdt p1 = Productdt.builder()
                                .id(p.getId())
                                .name(p.getName())
                                .price(p.getPrice())
                                .image(p.getImage())
                                .info(p.getInfo())
                                .deleted(p.getDeleted())
                                .build();
        return p1;
    }

    @Override
    public Product getProductById(String id) {
        return productRepos.getReferenceById(id);
    }

    @Override
    public List<Product> getTop6Productfeedback() {
        return productRepos.findTop6ProductFeedback();
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepos.findProductByName(name);
    }

    @Override
    public List<String> getAllProductName() {
        return productRepos.findAllProductName();
    }

    @Override
    public boolean checkID(String id) {
        Optional<Product> product = productRepos.findById(id);
        if(product.isPresent()) return true;
        return false;
    }

}
