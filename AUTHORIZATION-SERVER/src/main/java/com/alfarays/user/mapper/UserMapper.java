package com.alfarays.user.mapper;

import com.alfarays.profile.Profile;
import com.alfarays.profile.ProfileResponse;
import com.alfarays.role.entity.Role;
import com.alfarays.user.entity.User;
import com.alfarays.user.model.RegistrationRequest;
import com.alfarays.user.model.UserResponse;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(RegistrationRequest request) {
        return User
                .builder()
                .userId(randomString())
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse
                .builder()
                .userId(UUID.randomUUID().toString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .roles(
                        user
                                .getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                )
                .profile(
                        Optional.ofNullable(user.getProfile())
                                .map(UserMapper::imageResponse)
                                .orElse(null)
                )
                .build();
    }

    private static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static ProfileResponse imageResponse(Profile image){
        return ProfileResponse
                .builder()
                .imageId(UUID.randomUUID().toString())
                .name(image.getName())
                .type(image.getType())
                .size(image.getSize())
                .createdAt(image.getCreatedAt().toString())
                .updatedAt(image.getUpdatedAt().toString())
                .path(image.getPath())
                .build();
    }
}
