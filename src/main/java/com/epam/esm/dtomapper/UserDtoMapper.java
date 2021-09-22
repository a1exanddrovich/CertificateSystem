package com.epam.esm.dtomapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    private final OrderDtoMapper orderDtoMapper;

    public UserDtoMapper(OrderDtoMapper orderDtoMapper) {
        this.orderDtoMapper = orderDtoMapper;
    }

    public UserDto map(User user) {
        return new UserDto(user.getId(), user.getName(), user.getBalance(), user.getOrders().stream().map(orderDtoMapper::map).collect(Collectors.toList()));
    }

}
