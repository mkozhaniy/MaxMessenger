package com.maxprojects.maxmessenger.services;

import com.maxprojects.maxmessenger.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean save(UserDTO userDTO);
}
