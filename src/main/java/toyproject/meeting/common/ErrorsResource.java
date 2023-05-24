package toyproject.meeting.common;

import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import toyproject.meeting.index.IndexController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
public class ErrorsResource extends EntityModel<Errors> {

    private List<FieldError> fieldErrors;
    private List<ObjectError> globalErrors;

    public ErrorsResource(Errors errors, Link... links) {
        this.fieldErrors = errors.getFieldErrors();
        this.globalErrors = errors.getGlobalErrors();
        add(Arrays.asList(links));
        add(linkTo(methodOn(IndexController.class).toIndex()).withRel("index"));
    }
}
