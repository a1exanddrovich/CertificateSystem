package com.epam.esm.dtomapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper {

    private final UserDtoMapper userDtoMapper;
    private final GiftCertificateDtoMapper giftCertificateDtoMapper;

    public OrderDtoMapper(UserDtoMapper userDtoMapper, GiftCertificateDtoMapper giftCertificateDtoMapper) {
        this.userDtoMapper = userDtoMapper;
        this.giftCertificateDtoMapper = giftCertificateDtoMapper;
    }

    public OrderDto map(Order order) {
        return new OrderDto(order.getId(),
                            userDtoMapper.map(order.getUser()),
                            giftCertificateDtoMapper.map(order.getGiftCertificate()),
                            order.getTimeStamp(),
                            order.getPrice());
    }

    public Order unmap(OrderDto orderDto) {
        return new Order(orderDto.getId(),
                        userDtoMapper.unmap(orderDto.getUser()),
                        giftCertificateDtoMapper.unmap(orderDto.getGiftCertificate()),
                        orderDto.getTimeStamp(),
                        orderDto.getPrice());
    }

}
