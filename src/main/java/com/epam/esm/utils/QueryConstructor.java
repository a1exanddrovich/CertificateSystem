package com.epam.esm.utils;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

@Component
public class QueryConstructor {

    private static final String ORDER_BY_NAME_STATEMENT = " ORDER BY name";
    private static final String ORDER_BY_DATE_STATEMENT = " ORDER BY create_date";
    private static final String DESCENDING_ORDER = " DESC";
    private static final String COMMA = ", ";
    private static final String LIMIT = " LIMIT ";
    private static final String SELECT_CERTIFICATE_ID_BY_TAG_NAME = "(SELECT gc.id FROM gift_certificate gc JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id JOIN tag t ON gct.tag_id = t.id WHERE t.name = ";

    public String constructGiftCertificateQuery(String[] tagNames, String giftCertificateName, String description, String sortByName, String sortByDate, Integer page, Integer pageSize) {




        //StringBuilder query = new StringBuilder(SqlQueries.FIND_ALL_CERTIFICATES);
        StringBuilder query = new StringBuilder("Select * from gift_certificate");

        if (giftCertificateName != null) {
            query.append(" WHERE gift_certificate.name LIKE '%").append(giftCertificateName).append("%'");

            if (description != null) {
                query.append(" AND ");
            }
        }

        if (description != null) {
            if (giftCertificateName == null) {
                query.append(" WHERE");
            }

            query.append(" gift_certificate.description LIKE '%").append(description).append("%'");
        }

        if (tagNames != null && tagNames.length != 0) {
            query.append(" HAVING gift_certificate.id IN ");
            for (String tagName : tagNames) {
                query.append(SELECT_CERTIFICATE_ID_BY_TAG_NAME).append("'").append(tagName).append("')").append(" AND gc.id IN ");
            }
            query.delete(query.length() - 14, query.length());
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

        if (page != null && pageSize != null) {
            query.append(LIMIT).append(pageSize * (page - 1)).append(COMMA).append(pageSize);
        }

        return query.toString();
    }

    public String constructGiftCertificateUpdateQuery(long id, GiftCertificate giftCertificate) {
        StringBuilder query = new StringBuilder("UPDATE gift_certificate SET ");

        if (giftCertificate.getName() != null) {
            query.append("name = '").append(giftCertificate.getName()).append("', ");
        }

        if (giftCertificate.getDescription() != null) {
            query.append("description = '").append(giftCertificate.getDescription()).append("', ");
        }

        if (giftCertificate.getPrice() != null) {
            query.append("price = ").append(giftCertificate.getPrice()).append(COMMA);
        }

        if (giftCertificate.getDuration() != null) {
            query.append("duration = ").append(giftCertificate.getDuration().toDays()).append(COMMA);
        }

        query.append("last_update_date = '").append(giftCertificate.getLastUpdateDate()).append("' ");
        query.append(" WHERE id = ").append(id);

        return query.toString();
    }

    public String constructPaginatedQuery(Integer page, Integer pageSize, String initialQuery) {
        StringBuilder query = new StringBuilder(initialQuery);

        if (page != null && pageSize != null) {
            query.append(LIMIT).append(pageSize * (page - 1)).append(COMMA).append(pageSize);
        }

        return query.toString();
    }

}
