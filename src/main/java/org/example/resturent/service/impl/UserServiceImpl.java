package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.user.UserDTO;
import org.example.resturent.exeptions.custom.UserNotFoundException;
import org.example.resturent.model.User;
import org.example.resturent.repository.UserRepository;
import org.example.resturent.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllByActiveTrue().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        
        // Update fields that are allowed to be updated
        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        
        if (userDTO.getRole() != null) {
            existingUser.setRole(userDTO.getRole());
        }
        
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        if (!userRepository.existsByIdAndActiveTrue(id, true)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deactivateUser(id);
    }
}
