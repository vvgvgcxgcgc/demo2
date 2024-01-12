package com.example.demo.Service;

import com.example.demo.Domain.Defaultaddress;
import com.example.demo.Domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DefaultAddressService {
    public Defaultaddress save(String address);
    public List<Defaultaddress> getAllAddress();
}
