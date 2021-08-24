package com.epam.esm.utils;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QueryConstructor {

    public String constructQuery(String tagName, String giftCertificateName, String description, String sortByName, String sortByDate) {
        String query = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, gct.gift_certificate_id, t.name AS tagName FROM gift_certificate gc JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id JOIN tag t ON gct.tag_id = t.id";

        if (giftCertificateName != null) {
            query += " WHERE gc.name LIKE '%" + giftCertificateName + "%'";

            if (description != null) {
                query += " AND ";
            }
        }

        if (description != null) {
            if (giftCertificateName == null) {
                query += " WHERE";
            }

            query += " gc.description LIKE '%" + description + "%'";
        }

        if (tagName != null) {
            query += " HAVING gc.id IN (SELECT gc.id FROM gift_certificate gc JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id JOIN tag t ON gct.tag_id = t.id WHERE t.name = '" + tagName + "')";
        }

        if (sortByName != null && sortByDate == null) {
            if (sortByName.equals("asc")) {
                query += " ORDER BY name";
            } else if (sortByName.equals("desc")) {
                query += " ORDER BY name DESC";
            }
        }

        if (sortByDate != null && sortByName == null) {
            if (sortByDate.equals("asc")) {
                query += " ORDER BY create_date";
            } else if (sortByDate.equals("desc")) {
                query += " ORDER BY create_date DESC";
            }
        }

        return query;
    }

    public String constructUpdateQuery(long id, GiftCertificate giftCertificate) {
        String query = "UPDATE gift_certificate SET ";

        if (giftCertificate.getName() != null) {
            query += "name = '" + giftCertificate.getName() + "', ";
        }

        if (giftCertificate.getDescription() != null) {
            query += "description = '" + giftCertificate.getDescription() + "', ";
        }

        //if (giftCertificate.getPrice() > 0) {
        if (giftCertificate.getPrice().compareTo(BigDecimal.ZERO) >= 1) {
            query += "price = " + giftCertificate.getPrice() + ", ";
        }

        if (giftCertificate.getDuration() > 0) {
            query += "duration = " + giftCertificate.getDuration() + ", ";
        }

        query += "last_update_date = '" + giftCertificate.getLastUpdateDate() + "' ";
        query += " WHERE id = " + id;

        return query;
    }

}
