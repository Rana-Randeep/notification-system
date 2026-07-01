package com.rana.notification_system.service;

import com.rana.notification_system.dto.UserRequestDTO;
import com.rana.notification_system.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO registerUser(UserRequestDTO request);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
}
