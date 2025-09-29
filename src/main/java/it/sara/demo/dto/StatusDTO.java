package it.sara.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {
    private int code;
    private String message;
    private String traceId;

    public StatusDTO(int code, String message) {
        this.code = code;
        this.message = message;
        this.traceId = java.util.UUID.randomUUID().toString();
    }

    public static StatusDTO success(String message) {
        return new StatusDTO(200, message != null ? message : "Success");
    }
    public static StatusDTO success(){return success(null);}

    public static StatusDTO error(int code, String message) {
        return new StatusDTO(code, message != null ? message : "Error");
    }
}
