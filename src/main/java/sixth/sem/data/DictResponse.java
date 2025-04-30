package sixth.sem.data;

import java.util.Objects;

/**
 * DictResponse
 */
/**
 * DictResponse
 */
public record DictResponse(Integer response_code, String response_text, String secret) {
    public DictResponse {
        Objects.requireNonNull(response_code);
        Objects.requireNonNull(response_text);
        if (response_text.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
