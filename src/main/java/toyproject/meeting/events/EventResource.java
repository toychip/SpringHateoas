package toyproject.meeting.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends EntityModel<Event> {

    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
