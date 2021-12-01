package com.epam.esm.service;

import com.epam.esm.utils.Constants;
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
import com.epam.esm.utils.DateTimeUtils;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.OrderRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

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
        GiftCertificate certificate = giftCertificateDao.findById(orderRequestDto.getGiftCertificateId()).orElseThrow(GiftCertificateNotExistsException::new);
        User user = userDao.findById(orderRequestDto.getUserId()).orElseThrow(UserNotExistsException::new);
        Order order = initOrder(certificate, user);

        return findById(orderDao.create(order));
    }

    public List<OrderDto> getUsersOrders(long id, Integer page, Integer pageSize) {
        User user = userDao.findById(id).orElseThrow(UserNotExistsException::new);
        List<Order> result = orderDao.findAllByUserId(user, paginationValidator.calculateFirstPage(page),paginationValidator.paginate(page, pageSize, orderDao.countById(user)));

        return result.stream().map(mapper::map).collect(Collectors.toList());
    }

    public OrderDto findById(long id) {
        Order order = orderDao.findById(id).orElseThrow(EntityNotExistsException::new);
        return mapper.map(order);
    }

    private Order initOrder(GiftCertificate certificate, User user) {
        Order order = new Order();
        order.setGiftCertificate(certificate);
        order.setUser(user);
        order.setPrice(certificate.getPrice());
        order.setTimeStamp(DateTimeUtils.now(Constants.DATE_FORMAT));

        return order;
    }

    public boolean hasNextPage(Integer page, Integer pageSize, Long id) {
        return (page + 1) * pageSize <= orderDao.countById(userDao.findById(id).orElseThrow(UserNotExistsException::new));
    }
}