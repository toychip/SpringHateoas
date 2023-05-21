package toyproject.meeting.events;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("paramsForTestFree")
    void testFree(int basePrice, int maxPrice, boolean isFree) {

        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Stream<Arguments> paramsForTestFree() {
        return Stream.of(
                Arguments.of(0, 0, true),
                Arguments.of(100, 0, false),
                Arguments.of(0, 100, false),
                Arguments.of(100, 200, false)
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForTestoffline")
    void testOffline(String location, boolean isOffline) {

        //Given
        Event event = Event.builder()
                .location(location)
                .build();

        //when
        event.update_location();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private static Stream<Arguments> parametersForTestoffline() {
        return Stream.of(
                Arguments.of("강남", true),
                Arguments.of(null, false),
                Arguments.of("          ", false)
        );
    }
}