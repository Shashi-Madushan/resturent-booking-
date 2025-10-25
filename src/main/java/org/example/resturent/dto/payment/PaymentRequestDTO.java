package org.example.resturent.dto.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private double amount;
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    
    @NotBlank(message = "Transaction ID is required")
    private String transactionId;
    
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
}
