package toyproject.meeting;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import toyproject.meeting.events.EventController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class IndexController {

    @GetMapping("/api")
    public RepresentationModel index() {
        var index = new RepresentationModel();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}
