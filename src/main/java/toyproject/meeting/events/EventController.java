package toyproject.meeting.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {    // 기본적인 검증, @NotEmpty~ 등의 오류가 있으면
            return ResponseEntity.badRequest().build();
        }

        eventValidator.validate(eventDto, bindingResult);   // 논리적인 오류가 있으면

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

//        아래의 과정을 ModelMapper를 이용하여 생략
//        Event.builder()
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
//                .build()

        Event event = modelMapper.map(eventDto, Event.class);
        event.update(); // 유무료 판단 여부
        event.update_location(); // offline 판단 여부
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }
}
