package uz.chessmaster.anor_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uz.chessmaster.anor_test.models.LogEntity;
import uz.chessmaster.anor_test.models.LogPatchRequestDto;
import uz.chessmaster.anor_test.models.LogRequestDto;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createLog() throws Exception{

        LogRequestDto log = LogRequestDto.builder()
                        .date(new Date())
                        .vehRegNum("AB222")
                        .vehOwnerName("Kuzya")
                        .odometerBegin(30.0)
                        .odometerEnd(55.6)
                        .description("Bada-bum cha cha!")
                                .build();
        //Не хватает поля route
        mockMvc.perform( post("/api/log")
                        .content(new ObjectMapper().writeValueAsString(log))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        log.setRoute("Moscow-New-york");
        mockMvc.perform( post("/api/log")
                        .content(new ObjectMapper().writeValueAsString(log))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //Нельзя отрицательный показатель давать
        log.setOdometerBegin(-5.1);
        mockMvc.perform( post("/api/log")
                        .content(new ObjectMapper().writeValueAsString(log))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //Стартовый показатель не может быть больше финальной
        log.setOdometerBegin(56.0);
        mockMvc.perform( post("/api/log")
                        .content(new ObjectMapper().writeValueAsString(log))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteLog() throws Exception {
        long id;
        String ResponseBody;
        LogRequestDto logRequestDto = LogRequestDto.builder()
                .date(new Date())
                .vehRegNum("AB222")
                .vehOwnerName("Danya")
                .odometerBegin(30.0)
                .odometerEnd(55.6)
                .route("Moscow-Tashkent")
                .description("Bada-bum cha cha!")
                .build();

        ResponseBody = mockMvc.perform( post("/api/log")
                .content(new ObjectMapper().writeValueAsString(logRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        id = new ObjectMapper().readValue(ResponseBody, LogEntity.class).getId();

        //Попытка удалить несуществующую запись
        mockMvc.perform( delete("/api/log/" + id+1000000)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //Успешное удаление
        mockMvc.perform( delete("/api/log/" + id))
                .andExpect(status().isNoContent());

        //Попытка удалить уже удаленную (несуществующую) запись
        mockMvc.perform( delete("/api/log/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }

    @Test
    public void updateLog() throws Exception{

        long id;
        String ResponseBody;
        LogRequestDto logRequestDto = LogRequestDto.builder()
                .date(new Date())
                .vehRegNum("AB222")
                .vehOwnerName("Danya")
                .odometerBegin(30.0)
                .odometerEnd(55.6)
                .route("Moscow-Tashkent")
                .description("Bada-bum cha cha!")
                .build();

        ResponseBody = mockMvc.perform( post("/api/log")
                        .content(new ObjectMapper().writeValueAsString(logRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        id = new ObjectMapper().readValue(ResponseBody, LogEntity.class).getId();

        LogPatchRequestDto logPatchRequestDto = LogPatchRequestDto.builder().route("New Route").build();
        mockMvc.perform( patch("/api/log/" + id)
                .content(new ObjectMapper().writeValueAsString(logPatchRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("New Route")));

        //Попытка обновить несуществующую запись
        mockMvc.perform( patch("/api/log/" + id+1000000)
                        .content(new ObjectMapper().writeValueAsString(logPatchRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        //Стартовый показатель не может быть больше финальной
        logPatchRequestDto.setOdometerBegin(21.0);
        logPatchRequestDto.setOdometerEnd(20.0);
        mockMvc.perform( patch("/api/log/" + id)
                        .content(new ObjectMapper().writeValueAsString(logPatchRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getLog() throws Exception{
        mockMvc.perform( get("/api/logs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform( get("/api/logs?owner_name=Jamshid&reg_num=AA121&begin=8.06.2022&end=8.06.2022")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}