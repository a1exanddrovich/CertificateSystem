package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderRequestDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
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
 * The class that represents an API for basic operations with the application concerned to orders.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService service;

    @Autowired
    public OrdersController(OrderService service) {
        this.service = service;
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
    @GetMapping
    public ResponseEntity<CollectionModel<OrderDto>> getUsersOrders(@RequestParam long id,
                                                                    @RequestParam(required = false) Integer page,
                                                                    @RequestParam(required = false) Integer pageSize) {
        List<OrderDto> orders = service.getUsersOrders(id, page, pageSize);
        int initialPage = page == null ? 4 : page;
        int initialPageSize = pageSize == null ? 4 : pageSize;
        Link previousPage = linkTo(methodOn(OrdersController.class).getUsersOrders(id, initialPage - 1, initialPageSize)).withSelfRel();
        Link nextPage = linkTo(methodOn(OrdersController.class).getUsersOrders(id, initialPage + 1, initialPageSize)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(orders, previousPage, nextPage));

    }

    /**
     * Creates new {@link Order} object and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and create {@link Order} object.
     *
     * @param orderRequestDto contains essential data for creating order e.g. user's and purchased gift certificate's id
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and created {@link Order} object.
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderDto createdOrder = service.createOrder(orderRequestDto);
        createdOrder.add(linkTo(methodOn(OrdersController.class).getUsersOrders(createdOrder.getUser().getId(), 1, 4)).withSelfRel());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

}
