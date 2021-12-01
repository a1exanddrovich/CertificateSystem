package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderRequestDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.hateoas.OrdersHateoasIssuer;
import com.epam.esm.service.OrderService;
import com.epam.esm.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class that represents an API for basic operations with the application concerned to orders.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService service;
    private final OrdersHateoasIssuer hateoasIssuer;

    @Autowired
    public OrdersController(OrderService service, OrdersHateoasIssuer hateoasIssuer) {
        this.service = service;
        this.hateoasIssuer = hateoasIssuer;
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
                                                                    @RequestParam(required = false, defaultValue = Constants.DEFAULT_FIRST_PAGE) Integer page,
                                                                    @RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        List<OrderDto> orders = service.getUsersOrders(id, page, pageSize);
        return ResponseEntity.ok(hateoasIssuer.addOrderLinks(orders, id, page, pageSize, service.hasNextPage(page, pageSize, id)));

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
        hateoasIssuer.addOrderLink(createdOrder);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

}
