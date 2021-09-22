package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.utils.InitialOrderDataHolder;
import com.epam.esm.entity.Order;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    private static final String JSON = "application/json";

    private final OrderService service;

    @Autowired
    public OrdersController(OrderService service) {
        this.service = service;
    }

    /**
     * Creates new {@link Order} object and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and create {@link Order} object.
     *
     * @param holder contains essential data for creating order e.g. user's and purchased gift certificate's id
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and created {@link Order} object.
     */
    @PostMapping(produces = JSON)
    public ResponseEntity<OrderDto> createOrder(@RequestBody InitialOrderDataHolder holder) {
        OrderDto createdOrder = service.createOrder(holder);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

}
