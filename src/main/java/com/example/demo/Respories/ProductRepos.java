package com.example.demo.Respories;

import com.example.demo.Domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepos extends CrudRepository<Product,String> {

}
