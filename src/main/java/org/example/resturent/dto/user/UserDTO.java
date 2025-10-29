package org.example.resturent.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone is required")
    @Size(min = 8, max = 20, message = "Phone must be between 8 and 20 characters")
    private String phone;
    @NotBlank(message = "Role is required")
    @Size(min = 3, max = 20, message = "Role must be between 3 and 20 characters")
    private String role;
    //private boolean verified;
}