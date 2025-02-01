package com.alfarays.user.service;

import com.alfarays.exception.AuthorizationException;
import com.alfarays.profile.Profile;
import com.alfarays.profile.ProfileRepository;
import com.alfarays.role.entity.Role;
import com.alfarays.role.repository.RoleRepository;
import com.alfarays.user.entity.User;
import com.alfarays.user.mapper.UserMapper;
import com.alfarays.user.model.PrincipleUser;
import com.alfarays.user.model.RegistrationRequest;
import com.alfarays.user.model.UserResponse;
import com.alfarays.user.repository.UserRepository;
import com.alfarays.util.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import io.micrometer.common.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private static final String IMAGE_FOLDER = "assets/images/";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(PrincipleUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User by username %s not found.", username)
                ));
    }

    @Override
    public GlobalResponse<Boolean> register(RegistrationRequest request, MultipartFile image) throws MethodArgumentNotValidException, IOException {
        var user = UserMapper.toEntity(request);

        List<FieldError> fieldErrors = new ArrayList<>();

        // Validate username
        var optionalUserByUsername = userRepository.findByUsername(request.getUsername());
        if (optionalUserByUsername.isPresent()) {
            fieldErrors.add(new FieldError("user", "username", String.format("User with username '%s' already exists.", request.getUsername())));
        }

        // Validate email
        var optionalUserByEmail = userRepository.findByEmail(request.getEmail());
        if (optionalUserByEmail.isPresent()) {
            fieldErrors.add(new FieldError("user", "email", String.format("User with email '%s' already exists.", request.getEmail())));
        }

        // Validate userId
        var optionalUserByUserId = userRepository.findByUserId(user.getUserId());
        if (optionalUserByUserId.isPresent()) {
            fieldErrors.add(new FieldError("user", "userId", String.format("User with user id '%s' already exists.", user.getUserId())));
        }

        if (!fieldErrors.isEmpty()) {
            BindingResult bindingResult = new BeanPropertyBindingResult(user, "user");
            fieldErrors.forEach(bindingResult::addError);

            // Use MethodParameter to get the method parameter
            MethodParameter methodParameter = new MethodParameter(
                    Arrays.stream(this.getClass().getDeclaredMethods())
                            .filter(method -> method.getName().equals("register"))
                            .findFirst()
                            .orElseThrow(), 0
            );

            throw new MethodArgumentNotValidException(null, methodParameter, bindingResult);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var role = roleRepository.findByName("user")
                .orElseThrow(() -> new RuntimeException("Role 'user' does not exist."));

        user.setRoles(List.of(role));

        Profile profile = saveProfile(image);
        user.setProfile(profile);
        userRepository.save(user);
        return GlobalResponse.success(Boolean.TRUE);
    }

    @Override
    public GlobalResponse<UserResponse> findByUsername(String username) {

        var response = userRepository.findByUsername(username)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));

        return GlobalResponse.success(response);
    }

    @Override
    public GlobalResponse<UserResponse> findByEmail(String email) {
        var response = userRepository.findByEmail(email)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email: " + email));

        return GlobalResponse.success(response);
    }

    @Override
    public GlobalResponse<UserResponse> findByUserId(String userId) {
        var response = userRepository.findByUserId(userId)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by user id: " + userId));

        return GlobalResponse.success(response);
    }

    @Override
    public GlobalResponse<UserResponse> findById(Long id) {
        var response = userRepository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by id."));

        return GlobalResponse.success(response);
    }


    @Override
    public GlobalResponse<Boolean> update(RegistrationRequest request, String userId, MultipartFile profile) throws IOException {
        var user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by user id: " + userId));

        if (StringUtils.isNotBlank(request.getFirstName()) || StringUtils.isNotEmpty(request.getLastName())) {
            user.setFirstName(request.getFirstName());
        }

        if (StringUtils.isNotBlank(request.getLastName()) || StringUtils.isNotEmpty(request.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if (Objects.nonNull(profile)) {
            Profile updatedProfile = updateProfile(profile, user.getProfile());
            user.setProfile(updatedProfile);
        }

        userRepository.save(user);
        return GlobalResponse.success(Boolean.TRUE);
    }

    @Override
    public GlobalResponse<Boolean> delete(String userId) throws IOException {
        var user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by user id: " + userId));
        //keep user logs for future reference

        //delete user profile image
        deleteProfile(user.getProfile());

        userRepository.delete(user);
        return GlobalResponse.success(Boolean.TRUE);
    }

    @Override
    public GlobalResponse<Boolean> assignRole(String userId, String[] names) {
        User user = userByUserId(userId);

        List<Role> roles = new ArrayList<>();
        for (String name : names) {
            var role = roleRepository.findByName(name)
                    .orElseThrow(() -> new AuthorizationException("Role not found by name: " + name));
            roles.add(role);
        }

        user.setRoles(roles);
        userRepository.save(user);

        return GlobalResponse.success(Boolean.TRUE);
    }

    @Override
    public GlobalResponse<Boolean> removeRole(String userId, String[] names) {
        var user = userByUserId(userId);

        List<Role> rolesToRemove = new ArrayList<>();
        for (String name : names) {
            var role = roleRepository.findByName(name)
                    .orElseThrow(() -> new AuthorizationException("Role not found by name: " + name));
            rolesToRemove.add(role);
        }

        user.getRoles().removeAll(rolesToRemove);
        userRepository.save(user);

        return GlobalResponse.success(Boolean.TRUE);
    }

    private User userByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by user id: " + userId));
    }

    private Profile saveProfile(final MultipartFile profile) throws IOException {
        if (profile == null || profile.isEmpty()) {
            return null;
        }

        String fileName = System.currentTimeMillis() + "_" +
                Optional.ofNullable(profile.getOriginalFilename())
                        .orElse("profile.jpg");

        String filePath = IMAGE_FOLDER + fileName;
        Path directoryPath = Paths.get(IMAGE_FOLDER);

        // Create directory if not exists
        Files.createDirectories(directoryPath);

        // Save file
        Path targetLocation = directoryPath.resolve(fileName);
        Files.copy(profile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Create and save image metadata
        Profile image = new Profile();
        image.setName(fileName);
        image.setPath(targetLocation.toString());
        image.setType(profile.getContentType());
        image.setSize(profile.getSize());

        return profileRepository.save(image);
    }

    private Profile updateProfile(final MultipartFile newProfile, final Profile existingImage) throws IOException {
        if (newProfile == null || newProfile.isEmpty()) {
            return existingImage;
        }

        // Delete existing file if it exists
        Path existingFilePath = Paths.get(existingImage.getPath());
        Files.deleteIfExists(existingFilePath);

        // Create new filename
        String fileName = System.currentTimeMillis() + "_" +
                Optional.ofNullable(newProfile.getOriginalFilename())
                        .orElse("profile.jpg");

        Path directoryPath = Paths.get(IMAGE_FOLDER);
        Files.createDirectories(directoryPath);

        // Save new file
        Path targetLocation = directoryPath.resolve(fileName);
        Files.copy(newProfile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Update existing image metadata
        existingImage.setName(fileName);
        existingImage.setPath(targetLocation.toString());
        existingImage.setType(newProfile.getContentType());
        existingImage.setSize(newProfile.getSize());

        return profileRepository.save(existingImage);
    }

    private boolean deleteProfile(final Profile existingImage) throws IOException {
        Path existingFilePath = Paths.get(existingImage.getPath());
        Files.deleteIfExists(existingFilePath);
        profileRepository.delete(existingImage);
        return true;
    }

}