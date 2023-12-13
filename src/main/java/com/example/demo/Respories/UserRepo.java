package com.example.demo.Respories;

import com.example.demo.Domain.Product;
import com.example.demo.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Domain.User;


@Repository

public interface UserRepo extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}
