package toyproject.meeting.events;

import lombok.*;

import jakarta.persistence.*;
import toyproject.meeting.accounts.Account;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")   // 연관관계 매핑한 것은 넣으면 안돼
@Entity

public class Event {

    @Id @GeneratedValue
    private Integer id; // 식별자 id

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임 private int basePrice; // (optional)
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;  // 최대 인원

    private boolean offline;        // 오프라인 여부
    private boolean free;           // 무료 여부

    @Enumerated(EnumType.STRING)    // 추후에 바뀔 수 있기 때문에
    @Builder.Default
    private EventStatus eventStatus = EventStatus.DRAFT;
    @ManyToOne
    private Account manager;

    public void update() {
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;} else {
            this.free = false;
        }
    }

    public void update_location() {
        if (this.location == null || this.location.isBlank()) {
            offline = false;
        }else {
            offline = true;
        }
    }
}
