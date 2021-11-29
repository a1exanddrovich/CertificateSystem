package com.epam.esm.hateoas;

import com.epam.esm.controller.OrdersController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrdersHateoasIssuer extends AbstractHateoasIssuer<OrderDto> {

    private static final Class<OrdersController> ORDER_CONTROLLER = OrdersController.class;

    public void addOrderLink(OrderDto order) {
        addLinks(ORDER_CONTROLLER, order, order.getId(), SELF_LINK);
    }


    public CollectionModel<OrderDto> addOrderLinks(List<OrderDto> orderDtos, long id, Integer page, Integer pageSize, boolean hasNextPage) {
        for (OrderDto orderDto : orderDtos) {
            addOrderLink(orderDto);
        }
        return addPagesLinks(orderDtos, id, page, pageSize, hasNextPage);
    }

    private CollectionModel<OrderDto> addPagesLinks(List<OrderDto> orderDtos, long id, Integer page, Integer pageSize, boolean hasNextPage) {
        Link previousPage = linkTo(methodOn(ORDER_CONTROLLER)
                .getUsersOrders(id, evaluatePreviousPage(page), pageSize)).withSelfRel();
        Link nextPage = linkTo(methodOn(ORDER_CONTROLLER)
                .getUsersOrders(id, evaluateNextPage(page, hasNextPage), pageSize)).withSelfRel();
        return CollectionModel.of(orderDtos, previousPage, nextPage);
    }

}
