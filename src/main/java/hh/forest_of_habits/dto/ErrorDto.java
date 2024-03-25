package hh.forest_of_habits.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorDto {
    private int status;
    private String message;
    private Date timestamp;

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
