package uz.chessmaster.anor_test.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import javax.validation.constraints.*;

@Schema(description = "Сущность лога")
@Setter
@Getter
@Entity
@Data
@Table(
        name = "log",
        indexes = {
                @Index(name="byName", columnList="vehicle_owner_name, vehicle_regnum, date, odometer_begin"),
                @Index(name = "date_odometer", columnList = "date, odometer_begin")
        }
)
public class LogEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date")
    private Date date;

    @NotBlank
    @Column(name = "vehicle_regnum")
    private String vehRegNum;

    @NotBlank
    @Column(name = "vehicle_owner_name")
    private String vehOwnerName;

    @Min(value = 0)
    @Column(name = "odometer_begin")
    private Double odometerBegin;

    @Min(value = 0)
    @Column(name = "odometer_end")
    private Double odometerEnd;

    @NotBlank
    @Column(name = "route")
    private String route;

    @NotBlank
    @Column(name = "description")
    private String description;
}

