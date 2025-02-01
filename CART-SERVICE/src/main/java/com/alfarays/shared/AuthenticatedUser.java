package com.alfarays.shared;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AuthenticatedUser {

    @JsonProperty("username")
    private String username;

    @JsonProperty("fullName")
    private String fullName;

    private String[] authorities;
}