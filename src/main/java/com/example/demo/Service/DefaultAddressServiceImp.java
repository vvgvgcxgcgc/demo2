package com.example.demo.Service;

import com.example.demo.Domain.Defaultaddress;
import com.example.demo.Respories.DefaultAddressRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultAddressServiceImp implements DefaultAddressService{
    private final DefaultAddressRepos defaultAddressRepos;
    @Override
    public Defaultaddress save(String address) {
        Defaultaddress defaultaddress = Defaultaddress.builder()
                .address(address)
                .build();
        return defaultAddressRepos.save(defaultaddress);
    }

    @Override
    public List<Defaultaddress> getAllAddress() {
        return defaultAddressRepos.findAll();
    }
}
