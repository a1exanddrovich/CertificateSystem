package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;
import java.util.Objects;

public class UserDto extends RepresentationModel<UserDto> {

    private long id;

    public UserDto(long id) {
        this.id = id;
    }

    public UserDto() { }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        UserDto userDto = (UserDto) o;

        return id == userDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
