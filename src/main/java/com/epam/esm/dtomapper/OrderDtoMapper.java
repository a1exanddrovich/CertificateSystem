package com.epam.esm.dtomapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.utils.Constants;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                            order.getGiftCertificate() == null ? null :
                            giftCertificateDtoMapper.map(order.getGiftCertificate()),
                            order.getTimeStamp().toString(),
                            order.getPrice());
    }

    public Order unmap(OrderDto orderDto) {
        return new Order(orderDto.getId(),
                        userDtoMapper.unmap(orderDto.getUser()),
                        giftCertificateDtoMapper.unmap(orderDto.getGiftCertificate()),
                        LocalDateTime.parse(orderDto.getTimeStamp(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)),
                        orderDto.getPrice());
    }

}
