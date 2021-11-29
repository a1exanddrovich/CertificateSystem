package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dtomapper.UserDtoMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.validator.PaginationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao dao;
    private final PaginationValidator paginationValidator;
    private final UserDtoMapper mapper;

    @Autowired
    public UserService(UserDao dao, PaginationValidator paginationValidator, UserDtoMapper mapper) {
        this.dao = dao;
        this.paginationValidator = paginationValidator;
        this.mapper = mapper;
    }

    public List<UserDto> getUsers(Integer page, Integer pageSize) {
        return dao
                .findAll(paginationValidator.calculateFirstPage(page), paginationValidator.paginate(page, pageSize, dao.countUsers()))
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public UserDto getUser(long id) throws EntityNotExistsException {
        User user = dao.findById(id).orElseThrow(EntityNotExistsException::new);

        return mapper.map(user);
    }

    public boolean hasNextPage(Integer page, Integer pageSize) {
        return (page + 1) * pageSize <= dao.countUsers();
    }
}
