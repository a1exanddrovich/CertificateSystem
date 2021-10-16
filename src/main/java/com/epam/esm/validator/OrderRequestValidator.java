package com.epam.esm.validator;

import com.epam.esm.dto.OrderRequestDto;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestValidator {

    public boolean validate(OrderRequestDto orderRequestDto) {
        return orderRequestDto.getUserId() >= 1 && orderRequestDto.getGiftCertificateId() >= 1;
    }

}
