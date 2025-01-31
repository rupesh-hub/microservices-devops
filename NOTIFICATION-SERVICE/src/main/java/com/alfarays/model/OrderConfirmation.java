package com.alfarays.model;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderConfirmation {

    private String confirmationNumber;
    private String orderId;
    private String produceName;
    private Double totalSpending;
    private LocalDateTime orderDate;
    private String orderBy;
    private String confirmationMessage;

}
