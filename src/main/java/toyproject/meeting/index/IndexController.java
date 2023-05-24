package toyproject.meeting.index;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.meeting.events.EventController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel toIndex() {
        var index = new RepresentationModel();
        index.add(linkTo(EventController.class).withRel("events"));
//        System.out.println("index = " + index);
        return index;
    }
}
