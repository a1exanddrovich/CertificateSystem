package com.epam.esm.dtomapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public UserDto map(User user) {
        return new UserDto(user.getId());
    }

    public User unmap(UserDto userDto) {
        return new User(userDto.getId());
    }

}
