package uz.chessmaster.anor_test.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message){
        this.message = message;
    }
}
