package com.example.demo.config;

import com.example.demo.Respositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserConfigService implements UserDetailsService {
    @Autowired
    private  UserRepo userRepo;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.demo.Domain.User user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Could not find username");
        }
        User user1 = new User(user.getUsername(),user.getPassword(),new User_Detail(user).getAuthorities());

        return user1;

    }
}
