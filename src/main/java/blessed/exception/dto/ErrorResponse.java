package blessed.exception.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    // Detalhes opcionais (param, campos, etc)
    private Map<String, Object> details;

    public ErrorResponse(String message, int status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
