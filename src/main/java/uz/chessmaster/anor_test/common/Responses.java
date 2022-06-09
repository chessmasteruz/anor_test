package uz.chessmaster.anor_test.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uz.chessmaster.anor_test.models.ErrorResponse;

public class Responses {

    public static final ResponseEntity<ErrorResponse> ERROR400 =
            new ResponseEntity<>(new ErrorResponse("Incorrect input data"), HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<?> DELETED =
            new ResponseEntity<>(null, HttpStatus.NO_CONTENT);


}
