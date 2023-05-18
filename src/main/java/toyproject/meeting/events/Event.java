package toyproject.meeting.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")   // 연관관계 매핑한 것은 넣으면 안돼

public class Event {

    private Integer id; // 식별자 id


    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임 private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;

    private boolean offline;        // 오프라인 여부
    private boolean free;           // 무료 여부
    private EventStatus eventStatus;

}
