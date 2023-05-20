package toyproject.meeting.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    EventValidator eventValidator;

    @Test
    void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest Api")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 5, 18, 22, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 5, 19, 23, 30))
                .beginEventDateTime(LocalDateTime.of(2023, 5, 18, 22, 30))
                .endEventDateTime(LocalDateTime.of(2023, 5, 18, 23, 30))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2스타일 펙토리")
//                .free(true)     // 기본 가격이 있으면 free(false)가 되어야함. 직접 입력할 수 없어야 함
//                .offline(false) // location이 있으면 offline = true가 되야한다. 직접 입력할 수 없어야 함
//                .eventStatus(EventStatus.PUBLISHED)
                .build();

//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsBytes(event)))    // acceptHeader)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string("Content-Type", MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    @DisplayName("createEvent 값들이 비어있을때 Bad_Request")
    void createEvent_Bad_Request_Empty_Input() throws Exception {

        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("createEvent 값들이 들어오는데 날짜가 이상한경우(이벤트 끝나는 날짜가 더 빠름, basePrice > maxPrice)")
    void createEvent_Bad_Request_Wrong_Input() throws Exception {

        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest Api")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 5, 18, 22, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 5, 19, 23, 30))
                .beginEventDateTime(LocalDateTime.of(2023, 5, 18, 22, 30))
                .endEventDateTime(LocalDateTime.of(2023, 5, 18, 21, 30))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2스타일 펙토리")
                .build();

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }
}
