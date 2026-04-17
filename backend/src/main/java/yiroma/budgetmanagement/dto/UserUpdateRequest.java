package yiroma.budgetmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères.")
        String name,

        @Email(message = "L'adresse email est invalide.")
        @Size(max = 255, message = "L'adresse email ne doit pas dépasser 255 caractères.")
        String email,

        @Size(min = 12, max = 255, message = "Le mot de passe doit contenir au moins 12 caractères.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).+$",
                message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial."
        )
        String password
) {
}
