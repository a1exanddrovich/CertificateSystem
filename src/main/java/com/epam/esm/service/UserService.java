package com.epam.esm.service;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.dtomapper.OrderDtoMapper;
import com.epam.esm.utils.Paginator;
import com.epam.esm.dtomapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final Paginator paginator;
    private final OrderDtoMapper orderDtoMapper;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserService(UserDao userDao, OrderDao orderDao, Paginator paginator, OrderDtoMapper dtoMapper, UserDtoMapper userDtoMapper) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.paginator = paginator;
        this.orderDtoMapper = dtoMapper;
        this.userDtoMapper = userDtoMapper;
    }

    public List<UserDto> getUsers(Integer page, Integer pageSize) {
        return userDao.findAll(page, paginator.paginate(page, pageSize, userDao.countUsers())).stream().map(userDtoMapper::map).collect(Collectors.toList());
    }

    public UserDto getUser(long id) throws EntityNotExistsException {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return userDtoMapper.map(optionalUser.get());
    }

    public List<OrderDto> getUsersOrders(long id, Integer page, Integer pageSize) {
        return orderDao.findAllByUserId(id, page, paginator.paginate(page, pageSize, orderDao.countById(id))).stream().map(orderDtoMapper::map).collect(Collectors.toList());
    }
}
