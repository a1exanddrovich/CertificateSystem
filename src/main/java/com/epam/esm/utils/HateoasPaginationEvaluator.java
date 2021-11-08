package com.epam.esm.utils;

import org.springframework.stereotype.Component;

@Component
public class HateoasPaginationEvaluator {

    public Integer evaluatePreviousPage(Integer page) {
        int initialPage = getInitialPage(page);
        return initialPage - 1 == 0 ? 1 : initialPage;
    }

    public Integer evaluateNextPage(Integer page) {
        int initialPage = getInitialPage(page);
        return initialPage + 1;
    }

    public Integer evaluatePageSize(Integer pageSize) {
        return pageSize == null ? Constants.DEFAULT_PAGE_SIZE : pageSize;
    }

    private int getInitialPage(Integer page) {
        return page == null ? Constants.DEFAULT_FIRST_PAGE : page;
    }

}
