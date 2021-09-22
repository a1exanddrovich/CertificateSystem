package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDto extends RepresentationModel<UserDto> {

    private long id;
    private String name;
    private BigDecimal balance;
    private List<OrderDto> orders = new ArrayList<>();

    public UserDto(long id, String name, BigDecimal balance, List<OrderDto> orders) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.orders = orders;
    }

    public UserDto() { }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<OrderDto> getOrder() {
        return orders;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setOrder(List<OrderDto> order) {
        this.orders = order;
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

        return id == userDto.id && Objects.equals(name, userDto.name) && Objects.equals(balance, userDto.balance) && Objects.equals(orders, userDto.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, balance, orders);
    }
}
