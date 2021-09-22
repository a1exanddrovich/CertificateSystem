package com.epam.esm.utils;

import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Component;

@Component
public class Paginator {

    private static final Integer DEFAULT_PAGE_SIZE = 4;

    public Integer paginate(Integer page, Integer pageSize, Integer maximumData) {
        if (page == null && pageSize != null) {
            throw new PaginationException();
        }

        if (page != null && page != 1 && (((pageSize == null ? DEFAULT_PAGE_SIZE : pageSize) * (page - 1) + 1) > maximumData)) {
            throw new PaginationException();
        }

        return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
    }

}
