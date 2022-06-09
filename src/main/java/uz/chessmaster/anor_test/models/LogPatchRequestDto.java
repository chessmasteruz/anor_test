package uz.chessmaster.anor_test.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Min;
import java.util.Date;

@Schema(description = "Тела запроса для обновления лога: достаточно заполнить поля которые необхдимо обновить")
@Builder
@Getter
@Setter
@Data
@Jacksonized
public class LogPatchRequestDto {

    @JsonFormat(pattern="dd.MM.yyyy")
    private Date date;

    @JsonProperty("vehicle_regnum")
    private String vehRegNum;

    @JsonProperty("vehicle_owner_name")
    private String vehOwnerName;

    @Min(value = 0)
    @JsonProperty("odometer_begin")
    private Double odometerBegin;

    @Min(value = 0)
    @JsonProperty("odometer_end")
    private Double odometerEnd;

    private String route;

    private String description;
}
