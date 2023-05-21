//package toyproject.meeting.events;
//
//
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.Link;
//import org.springframework.hateoas.RepresentationModel;
//import org.springframework.validation.Errors;
//import toyproject.meeting.IndexController;
//
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//
//public class ErrorsResource extends RepresentationModel<Errors> {
//
//    public ErrorsResource(Errors content, Link... links){
//        super(content, links);
//        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
//    }
//}
