package org.example.resturent.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private double amount;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime paymentDate;
    private Long bookingId;
    private boolean success;
}
