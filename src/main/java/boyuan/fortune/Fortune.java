package boyuan.fortune;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record Fortune(@NotBlank String message, Optional<String> author) {
}
