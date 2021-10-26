package com.epam.esm.validator;

import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Component;

/**
 * The class that performs all the pagination logic e.g. checks for having all the parameters
 *
 * @author Aliaksei Bliznichenka
 */
@Component
public class PaginationValidator {

    private static final Integer DEFAULT_PAGE_SIZE = 4;

    /**
     * Calculates if there is an opportunity to perform pagination
     *
     * @param page the page that is going to be displayed
     * @param pageSize represents how many items is displayed at a time
     * @param maximumData represents how many items stored in the data storage
     * @return applied page size
     */
    public Integer paginate(Integer page, Integer pageSize, Integer maximumData) {
        if (page == null && pageSize != null) {
            throw new PaginationException();
        }

        if (page != null && page < 1 && ((((pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize) * (page - 1) + 1) > maximumData)) {
            throw new PaginationException();
        }

        return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;

    }

}
