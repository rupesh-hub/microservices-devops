package com.alfarays.cart.resource;

import com.alfarays.shared.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartResource {

    @GetMapping
    public ResponseEntity<Object> ping(Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> userClaims = jwt.getClaim("user");

        String username = userClaims != null ? (String) userClaims.get("username") : null;
        String firstName = userClaims != null ? (String) userClaims.get("first_name") : null;
        String lastName = userClaims != null ? (String) userClaims.get("last_name") : null;

        // Extract authorities
        List<String> authoritiesList = jwt.getClaim("authorities");
        String[] authorities = authoritiesList != null ? authoritiesList.toArray(new String[0]) : new String[0];

        AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                .username(username)
                .fullName(firstName + " " + lastName)
                .authorities(authorities)
                .build();

        return ResponseEntity.ok(authenticatedUser);
    }

}
