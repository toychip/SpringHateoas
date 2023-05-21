package toyproject.meeting.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {
        super(event, Arrays.asList(links));
//        add(new Link, "http://localhso:8080/api/events/" + event.getId());
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
