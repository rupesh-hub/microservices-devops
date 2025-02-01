package com.alfarays.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("review-service", r -> r.path("/review-service/**")
                        .filters(f -> f.rewritePath("/review-service/(?<segment>.*)", "/api/v1.0.0/${segment}"))
                        .uri("lb://review-service"))
                .route("cart-service", r -> r.path("/cart-service/**")
                        .filters(f -> f.rewritePath("/cart-service/(?<segment>.*)", "/api/v1.0.0/${segment}"))
                        .uri("lb://cart-service"))
                .route("authorization-server", r -> r.path("/authorization-server/**")
                        .filters(f -> f.rewritePath("/authorization-server/(?<segment>.*)", "/${segment}"))
                        .uri("lb://authorization-server"))
                .route("chat-service", r->r.path("/chat-service/**")
                        .filters(f -> f.rewritePath("/chat-service/(?<segment>.*)","/api/v1.0.0/chat-service/${segment}"))
                        .uri("lb://chat-service")
                )
                .route("inventory-service", r->r.path("/inventory-service/**")
                        .filters(f -> f.rewritePath("/inventory-service/(?<segment>.*)","/api/v1.0.0/inventory-service/${segment}"))
                        .uri("lb://inventory-service")
                )
                .route("notification-service", r->r.path("/notification-service/**")
                        .filters(f -> f.rewritePath("/notification-service/(?<segment>.*)","/api/v1.0.0/notification-service/${segment}"))
                        .uri("lb://notification-service")
                )
                .route("order-service", r->r.path("/order-service/**")
                        .filters(f -> f.rewritePath("/order-service/(?<segment>.*)","/api/v1.0.0/order-service/${segment}"))
                        .uri("lb://order-service")
                )
                .route("payment-service", r->r.path("/payment-service/**")
                        .filters(f -> f.rewritePath("/payment-service/(?<segment>.*)","/api/v1.0.0/payment-service/${segment}"))
                        .uri("lb://payment-service")
                )
                .route("shipping-service", r->r.path("/shipping-service/**")
                        .filters(f -> f.rewritePath("/shipping-service/(?<segment>.*)","/api/v1.0.0/shipping-service/${segment}"))
                        .uri("lb://shipping-service")
                )
                .route("user-service", r->r.path("/user-service/**")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)","/api/v1.0.0/user-service/${segment}"))
                        .uri("lb://user-service")
                )
                .build();
    }

}
