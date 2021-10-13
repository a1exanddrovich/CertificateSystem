package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.dto.OrderRequestDto;
import com.epam.esm.utils.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;
    private final Paginator paginator;

    @Autowired
    public OrderService(UserDao userDao, OrderDao orderDao, GiftCertificateDao giftCertificateDao, Paginator paginator) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
        this.paginator = paginator;
    }

    public Order createOrder(OrderRequestDto holder) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(holder.getGiftCertificateId());
        Optional<User> optionalUser = userDao.findById(holder.getUserId());

        if (optionalGiftCertificate.isEmpty() || optionalUser.isEmpty()) {
            throw new EntityNotExistsException();
        }

        Order order = new Order();
        order.setGiftCertificate(optionalGiftCertificate.get());
        order.setUser(optionalUser.get());
        order.setPrice(optionalGiftCertificate.get().getPrice());
        order.setTimeStamp(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));

        long createdOrderId = orderDao.create(order);

        return findById(createdOrderId);
    }

    public Order findById(long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);

        if (optionalOrder.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return optionalOrder.get();
    }

    public List<Order> getUsersOrders(long id, Integer page, Integer pageSize) {
        Optional<User> optionalUser = userDao.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return orderDao.findAllByUserId(id, page, paginator.paginate(page, pageSize, orderDao.countById(id)));
    }

}
