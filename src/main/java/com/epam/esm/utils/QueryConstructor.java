package com.epam.esm.utils;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SqlQueries;
import org.springframework.stereotype.Component;

@Component
public class QueryConstructor {

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
            query.append(" HAVING gc.id IN (SELECT gc.id FROM gift_certificate gc JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id JOIN tag t ON gct.tag_id = t.id WHERE t.name = '").append(tagName).append("')");
        }

        if (sortByName != null && sortByDate == null) {
            if (sortByName.equals("asc")) {
                query.append(" ORDER BY name");
            } else if (sortByName.equals("desc")) {
                query.append(" ORDER BY name DESC");
            }
        }

        if (sortByDate != null && sortByName == null) {
            if (sortByDate.equals("asc")) {
                query.append(" ORDER BY create_date");
            } else if (sortByDate.equals("desc")) {
                query.append(" ORDER BY create_date DESC");
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
