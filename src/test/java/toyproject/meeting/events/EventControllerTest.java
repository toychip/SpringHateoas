package toyproject.meeting.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.restdocs.RestDocumentationExtension;

import java.time.LocalDateTime;
import java.util.stream.IntStream;


import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "meeting.apimanual.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    EventValidator eventValidator;

    private RequestSpecification spec;

//    @BeforeEach
//    public void setUp() {
//        this.spec = new RequestSpecBuilder().addFilter(documentationConfiguration(this.restDocumentation))
//                .build();
//    }

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest Api")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 5, 18, 22, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 5, 19, 23, 30))
                .beginEventDateTime(LocalDateTime.of(2023, 5, 18, 22, 20))
                .endEventDateTime(LocalDateTime.of(2023, 5, 19, 23, 30))
//                .basePrice(100)
//                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2스타일 펙토리")
//                .free(true)     // 기본 가격이 있으면 free(false)가 되어야함. 직접 입력할 수 없어야 함
//                .offline(false) // location이 있으면 offline = true가 되야한다. 직접 입력할 수 없어야 함
//                .eventStatus(EventStatus.PUBLISHED)
                .build();


        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsBytes(event)))    // acceptHeader)
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("create-event",
                                links(
                                        linkWithRel("self").description("link to self"),
                                        linkWithRel("query-events").description("link to query events"),
                                        linkWithRel("update-event").description("link to update an existing event")
//                                , linkWithRel("profile").description("link to profile")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                                ),
                                requestFields(
                                        fieldWithPath("name").description("Name of new event"),
                                        fieldWithPath("description").description("description of new event"),
                                        fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                        fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                        fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                        fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                        fieldWithPath("location").description("location of end of new event"),
                                        fieldWithPath("basePrice").description("base price of new event"),
                                        fieldWithPath("maxPrice").description("max price of new event"),
                                        fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("identifier of new event"),
                                        fieldWithPath("name").description("Name of new event"),
                                        fieldWithPath("description").description("description of new event"),
                                        fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                        fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                        fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                        fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                        fieldWithPath("location").description("location of end of new event"),
                                        fieldWithPath("basePrice").description("base price of new event"),
                                        fieldWithPath("maxPrice").description("max price of new event"),
                                        fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                        fieldWithPath("offline").description("it tells is the event is offline metting or not"),
                                        fieldWithPath("free").description("it tells is the event is free or not"),
                                        fieldWithPath("eventStatus").description("event status"),
                                        fieldWithPath("_links.self.href").description("link to self"),
                                        fieldWithPath("_links.query-events.href").description("link to query events list"),
                                        fieldWithPath("_links.update-event.href").description("link to update event list")
                                )))
        ;

    }

    @Test
    @DisplayName("입력값들이 비어있을때 Bad_Request")
    void createEvent_Bad_Request_Empty_Input() throws Exception {

        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("입력값들이 들어오는데 날짜가 이상한경우(이벤트 끝나는 날짜가 더 빠름, basePrice > maxPrice)Bad_Request")
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

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].defaultMessage").exists())
//                .andExpect(jsonPath("$[0].code").exists())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    void queryEvents() throws Exception{

        //Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        //When
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
        ;
    }

    private void generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .build();

        this.eventRepository.save(event);

    }
}


