package uz.chessmaster.anor_test.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Schema(description = "Суммарная дистанция и сгруппированная по датам списки логов")
@Builder
@Getter
@Setter
@Jacksonized
@Data
public class ReportDto {
    @JsonProperty("summary_distance")
    Double summaryDistance;

    Map<Date, List<LogEntity>> report;
}
