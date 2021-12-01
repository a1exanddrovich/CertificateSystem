package com.epam.esm.validator;

import com.epam.esm.exception.PaginationException;
import com.epam.esm.utils.Constants;
import org.springframework.stereotype.Component;

/**
 * The class that performs all the pagination logic e.g. checks for having all the parameters
 *
 * @author Aliaksei Bliznichenka
 */
@Component
public class PaginationValidator {

    /**
     * Calculates if there is an opportunity to perform pagination
     *
     * @param page the page that is going to be displayed
     * @param pageSize represents how many items is displayed at a time
     * @param maximumData represents how many items stored in the data storage
     * @return applied page size
     */
    public Integer paginate(Integer page, Integer pageSize, Integer maximumData) {
        page = calculateFirstPage(page);

        if (page < 1 && getCalculatedDataCount(page, pageSize) > maximumData) {
            throw new PaginationException();
        }

        return pageSize == null ? Integer.parseInt(Constants.DEFAULT_PAGE_SIZE) : pageSize;

    }

    public Integer calculateFirstPage(Integer page) {
        return page < 1 ? Integer.parseInt(Constants.DEFAULT_FIRST_PAGE) : page;
    }

    private Integer setPageSize(Integer pageSize) {
        return pageSize < 1 ? Integer.parseInt(Constants.DEFAULT_PAGE_SIZE) : pageSize;
    }

    private int getCalculatedDataCount(Integer page, Integer pageSize) {
        return setPageSize(pageSize) * (page - 1) + 1;
    }

}
