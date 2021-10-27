package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderRequestDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dtomapper.OrderDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.OrderRequestValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static UserDao userDao;
    private static OrderDao orderDao;
    private static GiftCertificateDao giftCertificateDao;
    private static PaginationValidator paginationValidator;
    private static OrderDtoMapper mapper;
    private static OrderRequestValidator validator;
    private static OrderService service;


    @BeforeAll
    public static void init() {
        userDao = Mockito.mock(UserDao.class);
        orderDao = Mockito.mock(OrderDao.class);
        giftCertificateDao = Mockito.mock(GiftCertificateDao.class);
        paginationValidator = Mockito.mock(PaginationValidator.class);
        mapper = Mockito.mock(OrderDtoMapper.class);
        validator = Mockito.mock(OrderRequestValidator.class);
        service = new OrderService(userDao, orderDao, giftCertificateDao, paginationValidator, mapper, validator);
    }

    @Test
    void testShouldReturnAllOrders() {
        //given
        User user = new User(1);
        Order testFirst = new Order();
        OrderDto testDtoFirst = new OrderDto();
        List<Order> expected = Arrays.asList(testFirst, testFirst);

        //when
        when(mapper.map(testFirst)).thenReturn(testDtoFirst);
        when(mapper.unmap(testDtoFirst)).thenReturn(testFirst);
        when(orderDao.countById(any())).thenReturn(2);
        when(paginationValidator.paginate(1,1, 2)).thenReturn(1);
        when(paginationValidator.calculateFirstPage(1)).thenReturn(1);
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        when(orderDao.findAllByUserId(user, 1, 1)).thenReturn(expected);
        List<Order> actual = service.getUsersOrders(1, 1, 1).stream().map(mapper::unmap).collect(Collectors.toList());

        //then
        assertEquals(expected, actual);
    }

    @Test
    void testShouldFindById() throws EntityNotExistsException {
        //given
        long id = 1L;
        OrderDto testClauseDto = new OrderDto(id, new UserDto(), new GiftCertificateDto(), ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))), new BigDecimal("20"));
        Order testClause = new Order(id, new User(), new GiftCertificate(), ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))), new BigDecimal("20"));

        //when
        when(orderDao.findById(id)).thenReturn(Optional.of(testClause));
        when(mapper.map(testClause)).thenReturn(testClauseDto);
        when(mapper.unmap(testClauseDto)).thenReturn(testClause);
        Order actual = mapper.unmap(service.findById(id));


        //then
        assertEquals(testClause, actual);
    }

    @Test
    void testShouldCreateOrder() throws EntityNotExistsException, EntityAlreadyExistsException, BadEntityException {
        //given
        long id = 1L;
        OrderDto expectedDto = new OrderDto(id, new UserDto(), new GiftCertificateDto(), ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))), new BigDecimal("20"));
        Order expectedOrder = new Order(id, new User(), new GiftCertificate(), ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))), new BigDecimal("20"));
        User user = new User();
        GiftCertificate giftCertificate = new GiftCertificate();
        OrderRequestDto orderRequestDto = new OrderRequestDto(id, id);

        //when
        when(validator.validate(orderRequestDto)).thenReturn(true);
        when(mapper.unmap(expectedDto)).thenReturn(expectedOrder);
        when(mapper.map(expectedOrder)).thenReturn(expectedDto);
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        when(orderDao.findById(0)).thenReturn(Optional.of(expectedOrder));
        Order actual = mapper.unmap(service.createOrder(orderRequestDto));

        //then
        assertEquals(expectedOrder, actual);
    }

}
