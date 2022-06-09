package uz.chessmaster.anor_test.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.chessmaster.anor_test.models.*;
import uz.chessmaster.anor_test.repositories.LogRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class LogService {
    private LogRepository logRepository;
    private ModelMapper modelMapper;

    public Optional<LogEntity> createLog(LogRequestDto logRequestDto){
        LogEntity log = modelMapper.map(logRequestDto, LogEntity.class);
        if(log.getOdometerBegin() > log.getOdometerEnd())
            return Optional.empty();
        logRepository.save(log);
        return Optional.of(log);
    }

    public boolean deleteLog(long id){
        Optional<LogEntity> optLog =  logRepository.findById(id);
        if(optLog.isEmpty()) return false;

        logRepository.delete(optLog.get());
        return true;
    }

    public Optional<LogEntity> updateLog(long id, LogPatchRequestDto logDto){
        Optional<LogEntity> optLog =  logRepository.findById(id);
        if(optLog.isEmpty())
            return Optional.empty();

        LogEntity log = optLog.get();
        log.setId(id);
        if(logDto.getDate() != null)
            log.setDate(logDto.getDate());
        if(logDto.getVehRegNum() != null)
            log.setVehRegNum(logDto.getVehRegNum());
        if(logDto.getVehOwnerName() != null)
            log.setVehOwnerName(logDto.getVehOwnerName());
        if(logDto.getOdometerBegin() != null)
            log.setOdometerBegin(logDto.getOdometerBegin());
        if(logDto.getOdometerEnd() != null)
            log.setOdometerEnd(logDto.getOdometerEnd());
        if(logDto.getRoute() != null)
            log.setRoute(logDto.getRoute());
        if(logDto.getDescription() != null)
            log.setDescription(logDto.getDescription());

        if(log.getOdometerBegin() > log.getOdometerEnd())
            return Optional.empty();

        logRepository.save(log);
        return Optional.of(log);
    }

    public Optional<ReportDto> getLog(String ownerName, String regNum, Date begin, Date end){

        ReportDto reportDto = ReportDto.builder().summaryDistance(0.0).build();
        List<LogEntity> logs;

        if(end != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end);
            calendar.add(Calendar.DATE, 1);
            end = calendar.getTime();
        }

        if(ownerName != null && regNum != null && begin != null && end != null)
            logs = logRepository.findAllByVehOwnerNameAndVehRegNumAndDateBetweenOrderByDateAscOdometerBeginAsc(ownerName, regNum, begin, end);
        else logs = logRepository.findAllByOrderByDateAscOdometerBeginAsc();

        Map<Date, List<LogEntity>> map = new TreeMap<>();

        logs.forEach(log -> {
            if(map.containsKey(log.getDate())){
                map.get(log.getDate()).add(log);
            } else{
                List<LogEntity> tmpList = new ArrayList<>();
                tmpList.add(log);
                map.put(log.getDate(), tmpList);
            }
            reportDto.setSummaryDistance(reportDto.getSummaryDistance() + log.getOdometerEnd() - log.getOdometerBegin());
        });

        reportDto.setReport(map);

        return Optional.of(reportDto);
    }
}
