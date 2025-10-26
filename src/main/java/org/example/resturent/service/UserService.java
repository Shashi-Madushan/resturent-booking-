package org.example.resturent.service;

import org.example.resturent.dto.user.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deactivateUser(Long id);
}
