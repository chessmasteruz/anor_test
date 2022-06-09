package uz.chessmaster.anor_test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import uz.chessmaster.anor_test.common.Responses;
import uz.chessmaster.anor_test.models.*;
import uz.chessmaster.anor_test.services.LogService;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Tag(name="Контроллер логирования путешествий", description="В этом контроллере реализованы 4 эндпоинта для добавления, удаления, обновления и получения записей о путешествий")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MainController {

    private LogService logService;

    @Operation(
            summary = "Добавление лога",
            description = "Метод добавляет запись в БД. Все поля в теле запроса обязательны!"
    )
    @PostMapping("/log")
    public ResponseEntity<?> createLog(@Valid @RequestBody LogRequestDto logRequestDto, Errors errors){
        if(errors.hasErrors())
            return Responses.ERROR400;

        Optional<LogEntity> optLog = logService.createLog(logRequestDto);
        if(optLog.isEmpty())
            return Responses.ERROR400;

        return new ResponseEntity<>(optLog.get(), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Удаление лога",
            description = "Передается id записи которую необходимо удалить"
    )
    @DeleteMapping("/log/{id}")
    public ResponseEntity<?> deleteLog(@PathVariable long id){

        if(logService.deleteLog(id))
            return Responses.DELETED;
        else
            return Responses.ERROR400;
    }

    @Operation(
            summary = "Обновление лога",
            description = "Метод обновляет запись в БД. Передается id записи которую необходимо обновить. В теле запроса дотаточно передать лишь те паля, которые необходимо обновить!"
    )
    @PatchMapping("/log/{id}")
    public ResponseEntity<?> createLog(@PathVariable long id, @Valid @RequestBody LogPatchRequestDto LogPatchRequestDto, Errors errors){
        if(errors.hasErrors())
            return Responses.ERROR400;

        Optional<LogEntity> optLog = logService.updateLog(id, LogPatchRequestDto);

        if(optLog.isEmpty())
            return Responses.ERROR400;

        return new ResponseEntity<>(optLog.get(), HttpStatus.ACCEPTED);
    }

    @Operation(
            summary = "Получение суммарной дистанции и записей в группированном по дате виде",
            description = "Можно указать параметры поиска (период даты, имя владельца и номер регистрации транспорта. Фильтры применяются либо все либо ни один из них."
    )
    @GetMapping("/logs")
    public ResponseEntity<?> getLog(
            @RequestParam(required = false) @Parameter(description = "Имя владельца транспорта") String owner_name,
            @RequestParam(required = false) @Parameter(description = "Номер регистрации транспорта") String reg_num,
            @RequestParam(value = "begin", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") @Parameter(description = "Дата от") Date begin,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") @Parameter(description = "Дата до")  Date end
    ){
        Optional<ReportDto> reportDto = logService.getLog(owner_name, reg_num, begin, end);
        if(reportDto.isEmpty())
            return Responses.ERROR400;
        return new ResponseEntity<>(reportDto.get(), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(){
        return Responses.ERROR400;
    }
}
