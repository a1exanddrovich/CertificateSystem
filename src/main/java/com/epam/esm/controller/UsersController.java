package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.hateoas.UsersHateoasIssuer;
import com.epam.esm.service.UserService;
import com.epam.esm.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class that represents an API for basic operations with the application concerned to users.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final String ID = "id";

    private final UserService service;
    private final UsersHateoasIssuer hateoasIssuer;

    @Autowired
    public UsersController(UserService service, UsersHateoasIssuer hateoasIssuer) {
        this.service = service;
        this.hateoasIssuer = hateoasIssuer;
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link List} list of {@link User}
     * users retrieved from database.
     *
     * @param page     specifies page of the result list.
     * @param pageSize specifies number of tags to be displayed in a single page. In case page were passed without page size
     *                 default page size applies.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link List} of {@link User} users.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<UserDto>> getUsers(@RequestParam(required = false, defaultValue = Constants.DEFAULT_FIRST_PAGE) Integer page,
                                                             @RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        List<UserDto> users = service.getUsers(page, pageSize);
        return ResponseEntity.ok(hateoasIssuer.addUserLinks(users, page, pageSize, service.hasNextPage(page, pageSize)));
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link User} object from database.
     *
     * @param id id of {@link User} that has to be retrieved from database.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and an {@link User} object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(ID) long id) {
        UserDto user = service.getUser(id);
        hateoasIssuer.addUserLink(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
