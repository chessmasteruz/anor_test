package uz.chessmaster.anor_test.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "Тела запроса для добавления лога: все поля обязательны")
@Builder
@Getter
@Setter
@Data
@Jacksonized
public class LogRequestDto {
    @NotNull
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date date;

    @NotBlank
    @JsonProperty("vehicle_regnum")
    private String vehRegNum;

    @NotBlank
    @JsonProperty("vehicle_owner_name")
    private String vehOwnerName;

    @Min(value = 0)
    @JsonProperty("odometer_begin")
    private Double odometerBegin;

    @Min(value = 0)
    @JsonProperty("odometer_end")
    private Double odometerEnd;

    @NotBlank
    private String route;

    @NotBlank
    private String description;
}
