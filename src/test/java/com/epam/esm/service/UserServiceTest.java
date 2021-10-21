package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dtomapper.UserDtoMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.utils.Paginator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class UserServiceTest {


    private static UserDao userDao;
    private static Paginator paginator;
    private static UserDtoMapper userDtoMapper;
    private static UserService service;

    @BeforeAll
    public static void init() {
        userDao = Mockito.mock(UserDao.class);
        paginator = Mockito.mock(Paginator.class);
        userDtoMapper = Mockito.mock(UserDtoMapper.class);
        service = new UserService(userDao, paginator, userDtoMapper);
    }

    @Test
    void testShouldReturnAllUsers() {
        //given
        User user = new User(1);
        UserDto userDto = new UserDto(1);
        List<User> expected = Arrays.asList(user, user);

        //when
        when(userDtoMapper.map(user)).thenReturn(userDto);
        when(userDtoMapper.unmap(userDto)).thenReturn(user);
        when(userDao.countUsers()).thenReturn(2);
        when(paginator.paginate(1,1, 2)).thenReturn(1);
        when(userDao.findAll(1, 1)).thenReturn(expected);
        List<User> actual = service.getUsers(1, 1).stream().map(userDtoMapper::unmap).collect(Collectors.toList());

        //then
        assertEquals(expected, actual);
    }

    @Test
    void testShouldFindById() throws EntityNotExistsException {
        //given
        long id = 1L;
        User testClause = new User(id);
        UserDto testClauseDto = new UserDto(id);

        //when
        when(userDao.findById(anyLong())).thenReturn(Optional.of(testClause));
        when(userDtoMapper.map(testClause)).thenReturn(testClauseDto);
        when(userDtoMapper.unmap(testClauseDto)).thenReturn(testClause);
        User actual = userDtoMapper.unmap(service.getUser(id));


        //then
        assertEquals(testClause, actual);
    }

}
