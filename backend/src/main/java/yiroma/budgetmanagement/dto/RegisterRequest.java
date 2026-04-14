package yiroma.budgetmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Pattern(
            regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$",
            message = "L'adresse email est invalide."
        )
        String email,

        @NotBlank
        @Size(min = 12, max = 255, message = "Le mot de passe doit contenir au moins 12 caractères.")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{12,}$",
            message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial."
        )
        String password,

        @NotBlank @Size(min = 2, max = 100)
        String name
) {
}
