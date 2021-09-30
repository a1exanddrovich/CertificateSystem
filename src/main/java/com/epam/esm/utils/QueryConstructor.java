package com.epam.esm.utils;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.sql.SqlQueries;
import org.springframework.stereotype.Component;

@Component
public class QueryConstructor {

    private static final String ORDER_BY_NAME_STATEMENT = " ORDER BY name";
    private static final String ORDER_BY_DATE_STATEMENT = " ORDER BY create_date";
    private static final String DESCENDING_ORDER = " DESC";

    public String constructQuery(String tagName, String giftCertificateName, String description, String sortByName, String sortByDate) {
        StringBuilder query = new StringBuilder(SqlQueries.FIND_ALL_CERTIFICATES);

        if (giftCertificateName != null) {
            query.append(" WHERE gc.name LIKE '%").append(giftCertificateName).append("%'");

            if (description != null) {
                query.append(" AND ");
            }
        }

        if (description != null) {
            if (giftCertificateName == null) {
                query.append(" WHERE");
            }

            query.append(" gc.description LIKE '%").append(description).append("%'");
        }

        if (tagName != null) {
            query.append(" HAVING gc.id IN ").append(SqlQueries.SELECT_CERTIFICATE_ID_BY_TAG_NAME).append("'").append(tagName).append("')");
        }

        if (sortByName != null && sortByDate == null) {
            if (sortByName.equals("asc")) {
                query.append(ORDER_BY_NAME_STATEMENT);
            } else if (sortByName.equals("desc")) {
                query.append(ORDER_BY_NAME_STATEMENT).append(DESCENDING_ORDER);
            }
        }

        if (sortByDate != null && sortByName == null) {
            if (sortByDate.equals("asc")) {
                query.append(ORDER_BY_DATE_STATEMENT);
            } else if (sortByDate.equals("desc")) {
                query.append(ORDER_BY_DATE_STATEMENT).append(DESCENDING_ORDER);
            }
        }

        return query.toString();
    }

    public String constructUpdateQuery(long id, GiftCertificate giftCertificate) {
        StringBuilder query = new StringBuilder("UPDATE gift_certificate SET ");

        if (giftCertificate.getName() != null) {
            query.append("name = '").append(giftCertificate.getName()).append("', ");
        }

        if (giftCertificate.getDescription() != null) {
            query.append("description = '").append(giftCertificate.getDescription()).append("', ");
        }

        if (giftCertificate.getPrice() != null) {
            query.append("price = ").append(giftCertificate.getPrice()).append(", ");
        }

        if (giftCertificate.getDuration() != null) {
            query.append("duration = ").append(giftCertificate.getDuration().toDays()).append(", ");
        }

        query.append("last_update_date = '").append(giftCertificate.getLastUpdateDate()).append("' ");
        query.append(" WHERE id = ").append(id);

        return query.toString();
    }

}
