package com.maxprojects.maxmessenger.services.impl;

import com.maxprojects.maxmessenger.DTO.UserDTO;
import com.maxprojects.maxmessenger.Entities.Role;
import com.maxprojects.maxmessenger.Entities.User;
import com.maxprojects.maxmessenger.repositories.UserRepository;
import com.maxprojects.maxmessenger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService { //security

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public boolean save(UserDTO userDTO) {
        if(!Objects.equals(userDTO.getPassword(), userDTO.getMatchPassword()))
        {
             throw new RuntimeException("Passwords is not equals");
        }
        User user = User.builder()
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .mail(userDTO.getMail())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println(login);
        User user = userRepository.findUserByLogin(login);

        if(user == null)
        {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                roles
        );
    }
}
