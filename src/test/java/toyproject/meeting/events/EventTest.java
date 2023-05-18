package toyproject.meeting.events;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builder() {    // 빌더 유무 여부
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean() {   // java bean 스펙 준수하는지 확인, default 생성자로 만들 수 있어야함

        //given
        String eventName = "eventName";
        String spring = "Spring";
        Event event = new Event();

        //when
        event.setName(eventName);
        event.setDescription(spring);

        //then
        assertThat(event.getName()).isEqualTo(eventName);
        assertThat(event.getDescription()).isEqualTo(spring);

    }
}