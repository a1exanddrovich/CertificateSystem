package com.epam.esm.hateoas;

import com.epam.esm.controller.UsersController;
import com.epam.esm.dto.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsersHateoasIssuer extends AbstractHateoasIssuer<UserDto> {

    private static final Class<UsersController> USER_CONTROLLER = UsersController.class;

    public void addUserLink(UserDto user) {
        addLinks(USER_CONTROLLER, user, user.getId(), SELF_LINK);
    }

    public CollectionModel<UserDto> addUserLinks(List<UserDto> userDtos, Integer page, Integer pageSize, boolean hasNextPage) {
        for (UserDto userDto : userDtos) {
            addUserLink(userDto);
        }
        return addPagesLinks(userDtos, page, pageSize, hasNextPage);
    }

    private CollectionModel<UserDto> addPagesLinks(List<UserDto> userDtos, Integer page, Integer pageSize, boolean hasNextPage) {
        Link previousPage = linkTo(methodOn(USER_CONTROLLER)
                .getUsers(evaluatePreviousPage(page), pageSize)).withSelfRel();
        Link nextPage = linkTo(methodOn(USER_CONTROLLER)
                .getUsers(evaluateNextPage(page, hasNextPage), pageSize)).withSelfRel();
        return CollectionModel.of(userDtos, previousPage, nextPage);
    }

}
