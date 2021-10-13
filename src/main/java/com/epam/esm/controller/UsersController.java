package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The class that represents an API for basic operations with the application concerned to users.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final String ID = "id";
    private static final String ALL_USERS = "users";

    private final UserService service;

    @Autowired
    public UsersController(UserService service) {
        this.service = service;
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
    public ResponseEntity<CollectionModel<UserDto>> getUsers(@RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) Integer pageSize) {
        List<UserDto> users = service.getUsers(page, pageSize);
        users.forEach(user -> user.add(linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel()));
        return ResponseEntity.ok(CollectionModel.of(users));
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
        user.add(linkTo(methodOn(UsersController.class).getUser(id)).withSelfRel());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a list of {@link Order} orders of specified user.
     *
     * @param id       id of {@link User} which orders have to be retrieved from database.
     * @param page     specifies page of the result list.
     * @param pageSize specifies number of tags to be displayed in a single page. In case page were passed without page size
     *                 default page size applies.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and a list of {@link Order} orders of specified user
     */
    @GetMapping("/{id}/orders")
    public ResponseEntity<CollectionModel<OrderDto>> getUsersOrders(@PathVariable(ID) long id,
                                                         @RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer pageSize) {
        List<OrderDto> orders = service.getUsersOrders(id, page, pageSize);
        Integer initialPage = page == null ? 4 : page;
        Integer initialPageSize = pageSize == null ? 4 : pageSize;
        Link previousPage = linkTo(methodOn(UsersController.class).getUsersOrders(id, initialPage - 1, initialPageSize)).withSelfRel();
        Link nextPage = linkTo(methodOn(UsersController.class).getUsersOrders(id, initialPage - + 1, initialPageSize)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(orders, previousPage, nextPage));

    }

}
