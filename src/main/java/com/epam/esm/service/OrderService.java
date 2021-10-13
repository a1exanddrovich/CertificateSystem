package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dtomapper.OrderDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.exception.NotPayableUserException;
import com.epam.esm.dto.OrderRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class OrderService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;
    private final OrderDtoMapper dtoMapper;

    @Autowired
    public OrderService(UserDao userDao, OrderDao orderDao, GiftCertificateDao giftCertificateDao, OrderDtoMapper dtoMapper) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public com.epam.esm.dto.OrderDto createOrder(OrderRequestDto holder) {
        long userId = holder.getUserId();
        long giftCertificateId = holder.getGiftCertificateId();
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(giftCertificateId);
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalGiftCertificate.isEmpty() || optionalUser.isEmpty()) {
            throw new EntityNotExistsException();
        }

        if (optionalGiftCertificate.get().getPrice().compareTo(optionalUser.get().getBalance()) >= 1) {
            throw new NotPayableUserException();
        }

        BigDecimal updatedBalance = optionalUser.get().getBalance().subtract(optionalGiftCertificate.get().getPrice());

        userDao.updateBalanceById(userId, updatedBalance);

        Order order = new Order();
        order.setGiftCertificate(optionalGiftCertificate.get());
        order.setUser(optionalUser.get());
        order.setPrice(optionalGiftCertificate.get().getPrice());
        order.setTimeStamp(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));

        long createdOrderId = orderDao.create(order);

        return findById(createdOrderId);
    }

    public com.epam.esm.dto.OrderDto findById(long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);

        if (optionalOrder.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return dtoMapper.map(optionalOrder.get());
    }

}
