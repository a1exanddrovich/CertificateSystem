package com.epam.esm.dtomapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper {

    private final GiftCertificateDtoMapper giftCertificateDtoMapper;

    public OrderDtoMapper(GiftCertificateDtoMapper giftCertificateDtoMapper) {
        this.giftCertificateDtoMapper = giftCertificateDtoMapper;
    }

    public OrderDto map(Order order) {
        return new OrderDto(order.getId(), giftCertificateDtoMapper.map(order.getGiftCertificate()),order.getTimeStamp().toString(), order.getPrice());
    }

}
