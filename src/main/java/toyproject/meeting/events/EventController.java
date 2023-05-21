package toyproject.meeting.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto, Errors errors) {

        if (errors.hasErrors()) {    // 기본적인 검증, @NotEmpty~ 등의 오류가 있으면
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);   // 논리적인 오류가 있으면

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
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


        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(event);

        eventResource.add(selfLinkBuilder.withRel("query-events"));
//        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);

        var entityModels = assembler.toModel(page);

        return ResponseEntity.ok(entityModels);
    }

//    private ResponseEntity badRequest(Errors errors) {
//        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
//    }
}
