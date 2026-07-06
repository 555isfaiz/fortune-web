package boyuan.fortune;

import java.util.Optional;

public record Fortune(String message, Optional<String> author) {
}
