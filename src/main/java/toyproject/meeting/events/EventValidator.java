package toyproject.meeting.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, BindingResult errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0){
            errors.rejectValue("basePrice", "wrongValue", "defaultPrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
        }

        @NotNull LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())||    // 접수 종료 시간보다 이전
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())    // 시작도 전에 끝나면 안됨
        ) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrongValue");
        }   // beginEventDateTime, CloseEnrollmentDateTime 둘 다 검증 해야함
    }
}
