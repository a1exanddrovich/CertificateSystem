package com.epam.esm.hateoas;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AbstractHateoasIssuer<T extends RepresentationModel<T>> {
    protected static final String SELF_LINK = "self";

    protected void addLink(Class<?> controllerClass, T entity, Long id, String linkName) {
        entity.add(linkTo(controllerClass).slash(id).withRel(linkName));
    }

    protected void addLinks(Class<?> controllerClass, T entity, Long id, String... linkNames) {
        for (String linkName : linkNames) {
            addLink(controllerClass, entity, id, linkName);
        }
    }

    protected Integer evaluatePreviousPage(Integer page) {
        return page - 1 == 0 ? 1 : page;
    }

    protected Integer evaluateNextPage(Integer page, boolean hasNextPage) {
        return hasNextPage ? page + 1 : page;
    }

}
