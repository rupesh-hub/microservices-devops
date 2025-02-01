package com.alfarays.user.model;

import com.alfarays.profile.ProfileResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponse {

    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<String> roles;
    private boolean enabled;
    private ProfileResponse profile;

}