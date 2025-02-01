package com.alfarays.user.service;

import com.alfarays.user.model.RegistrationRequest;
import com.alfarays.user.model.UserResponse;
import com.alfarays.util.GlobalResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserService {

    GlobalResponse<Boolean> register(RegistrationRequest request, MultipartFile profile) throws MethodArgumentNotValidException, IOException;

    GlobalResponse<UserResponse> findByUsername(String username);

    GlobalResponse<UserResponse> findByEmail(String email);

    GlobalResponse<UserResponse> findByUserId(String userId);

    GlobalResponse<UserResponse> findById(Long id);

    GlobalResponse<Boolean> update(RegistrationRequest request, String userId, MultipartFile profile) throws IOException;

    GlobalResponse<Boolean> delete(String userId) throws IOException;

    GlobalResponse<Boolean> assignRole(String userId, String[] names);

    GlobalResponse<Boolean> removeRole(String userId, String[] names);

}
