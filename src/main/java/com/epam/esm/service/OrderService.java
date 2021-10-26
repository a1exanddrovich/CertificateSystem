package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dtomapper.OrderDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.dto.OrderRequestDto;
import com.epam.esm.exception.GiftCertificateNotExistsException;
import com.epam.esm.exception.UserNotExistsException;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.OrderRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;
    private final PaginationValidator paginationValidator;
    private final OrderDtoMapper mapper;
    private final OrderRequestValidator validator;

    @Autowired
    public OrderService(UserDao userDao, OrderDao orderDao, GiftCertificateDao giftCertificateDao, PaginationValidator paginationValidator, OrderDtoMapper mapper, OrderRequestValidator validator) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
        this.paginationValidator = paginationValidator;
        this.mapper = mapper;
        this.validator = validator;
    }

    public OrderDto createOrder(OrderRequestDto orderRequestDto) {
        if (!validator.validate(orderRequestDto)) {
            throw new BadEntityException();
        }

        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(orderRequestDto.getGiftCertificateId());
        Optional<User> optionalUser = userDao.findById(orderRequestDto.getUserId());

        if (optionalGiftCertificate.isEmpty()) {
            throw new GiftCertificateNotExistsException();
        }

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException();
        }

        GiftCertificate certificate = optionalGiftCertificate.get();
        User user = optionalUser.get();

        Order order = new Order();
        order.setGiftCertificate(certificate);
        order.setUser(user);
        order.setPrice(certificate.getPrice());
        order.setTimeStamp(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));

        long createdOrderId = orderDao.create(order);

        return findById(createdOrderId);
    }

    public OrderDto findById(long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);

        if (optionalOrder.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return mapper.map(optionalOrder.get());
    }

    public List<OrderDto> getUsersOrders(long id, Integer page, Integer pageSize) {
        Optional<User> optionalUser = userDao.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotExistsException();
        }

        List<Order> result = orderDao.findAllByUserId(optionalUser.get(), page, paginationValidator.paginate(page, pageSize, orderDao.countById(optionalUser.get())));

        return result.stream().map(mapper::map).collect(Collectors.toList());
    }

}
